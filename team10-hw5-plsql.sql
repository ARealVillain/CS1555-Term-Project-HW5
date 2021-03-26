/* Team 10 */
/* Krutarth Patel - kkp19 */
/* Erik Nordby - ern23 */
/* Kyle Tissue - kjt32 */

/* Task/Question 2 */
CREATE OR REPLACE FUNCTION SEARCH_MUTUAL_FUNDS (arg1 varchar(30), arg2 varchar(30))
    RETURNS TEXT
    AS $$
    DECLARE
    fund_list TEXT := '[';
    fund_cursor CURSOR FOR SELECT * FROM MUTUAL_FUND;
    fund_rec MUTUAL_FUND%ROWTYPE;
    BEGIN
        OPEN fund_cursor;
        LOOP
            FETCH fund_cursor INTO fund_rec;
            IF NOT FOUND THEN
                EXIT;
            END IF;

            IF (fund_rec.description LIKE '%' || arg1 || '%' ) AND (fund_rec.description LIKE '%' || arg2 || '%' ) THEN
                fund_list := fund_list || ' ' || fund_rec.symbol;
            end if;
        END LOOP;
        fund_list := fund_list || ']';
        return fund_list;
    END;
    $$ LANGUAGE plpgsql;

SELECT SEARCH_MUTUAL_FUNDS('stock', 'bond');

/* Task/Question 3 */
CREATE OR REPLACE PROCEDURE deposit_for_investment (cur_login varchar(10), amount dec(10,2))
    LANGUAGE plpgsql
    AS $$
    DECLARE
        cur_amount int := 0;
        shares_to_buy int;
        cur_percent dec(3,2);
        cur_symbol varchar(20);
        cur_price decimal(10,2);
        cur_allocation record;
        /*query to determine allocation number that has the same login as the parameter with max date*/
        allocation_number int := (select allocation_no from allocation where login=cur_login order by P_DATE desc limit 1);
        preference_cursor CURSOR FOR SELECT allocation_no,symbol,percentage FROM PREFERS WHERE allocation_no=allocation_number;
    BEGIN
        OPEN preference_cursor;
        /* loops through the cursor that has stored every allocation_no that matches the login given*/
        LOOP
            FETCH preference_cursor INTO cur_allocation;
            if not found then
                exit;
            end if;
            /*get the symbol percent and price that correspond to the current allocation_no*/
            cur_symbol := cur_allocation.symbol;
            cur_percent := cur_allocation.percentage;
            cur_price := (select price from closing_price where symbol=cur_symbol order by p_date desc limit 1);
            /* calculate the amount of shares that should be purchased*/
            shares_to_buy:=FLOOR(cur_percent*amount/cur_price);
            /* use the buy_shares function to purchase the shares*/
            if shares_to_buy >0 then
                INSERT INTO TRXLOG (trx_id,login,symbol,t_date,action,num_shares,price,amount) VALUES ((SELECT MAX(trx_id) FROM trxlog)+1, cur_login, cur_symbol, CURRENT_DATE, 'deposit',  shares_to_buy, cur_price, shares_to_buy*cur_price);
                perform shares from owns where login = cur_login and symbol = cur_symbol;
                if found then
                    update owns
                    set shares = shares + shares_to_buy
                    where login=cur_login and symbol=cur_symbol;
                else
                    INSERT INTO OWNS(login,symbol,shares) VALUES(cur_login,cur_symbol,shares_to_buy);
                end if;
                /*sum the amount spent*/
                cur_amount = cur_amount +shares_to_buy*cur_price;
            end if;
        END LOOP;
        amount:=amount-cur_amount;
        close preference_cursor;
        /* adds the remaining deposit to the customers balance*/
        UPDATE customer
        SET balance = balance + amount
        WHERE login = cur_login;

        commit;
    END;
    $$;
CALL deposit_for_investment('mike', 100);


/* Task/Question 4 */
DROP FUNCTION buy_shares(login varchar, symb varchar, num_shares integer);
CREATE OR REPLACE FUNCTION BUY_SHARES (log varchar(30), symb varchar(30), numb_shares int)
    RETURNS BOOLEAN
    AS $$
    DECLARE
    shares_val decimal(10, 2);
    total_shares_val decimal(10, 2);
    buyer_cap decimal(10, 2);
    new_buyer_cap decimal(10, 2);
    last_trx int;
    BEGIN
    /*Get the most recent day's value for the stock*/
    shares_val := (SELECT PRICE FROM closing_price
            WHERE symbol LIKE symb
            ORDER BY P_DATE DESC
            FETCH FIRST ROW ONLY);

    total_shares_val := shares_val*numb_shares;

    buyer_cap := (SELECT AMOUNT FROM TRXLOG
            WHERE login LIKE log
            ORDER BY trx_id DESC
            FETCH FIRST ROW ONLY);

    last_trx := (SELECT MAX(trx_id) FROM trxlog);

    new_buyer_cap := buyer_cap - total_shares_val;

    IF new_buyer_cap < 0 THEN return False;
    END IF;

    INSERT INTO TRXLOG (trx_id,login,symbol,t_date,action,num_shares,price,amount) VALUES (last_trx+1, log, symb, CURRENT_DATE, 'buy',  numb_shares, shares_val, new_buyer_cap);
    /* updates the owns table to list the newly purchased shares*/
    perform shares from owns where login = log and symbol = symb;
    if found then
        update owns
        set shares = shares + numb_shares
        where login=log and symbol=symb;
    else
        INSERT INTO OWNS(login,symbol,shares) VALUES(log,symb,numb_shares);
    end if;
    RETURN TRUE;

    END;
    $$ LANGUAGE plpgsql;


SELECT BUY_SHARES('mike', 'MM', 5);

/*CREATE OR REPLACE VIEW doView AS
    SELECT owns.login, symbol, c.balance, m.p_date
    FROM owns JOIN customer c ON owns.login = c.login NATURAL JOIN mutual_date m
    ORDER BY shares ASC
    FETCH FIRST ROW ONLY;

CREATE OR REPLACE VIEW costView AS
    SELECT price, div(d.balance, price) AS purchasable, d.login, d.symbol, d.balance, d.p_date AS mutDATE, closing_price.p_date AS closeDATE
    FROM closing_price JOIN doview d on closing_price.symbol = d.symbol
    ORDER BY closeDATE DESC
    FETCH NEXT 2 ROWS ONLY;*/

/* Task/Question 5 */

CREATE OR REPLACE FUNCTION buy_on_date()
    RETURNS TRIGGER AS
    $$
    DECLARE
    userLog varchar(10);
    symbol varchar(20);
    purchasable int;
    mutDate date;
    currDate date;
    BEGIN
        userLog := (SELECT login
            FROM owns JOIN customer c ON owns.login = c.login NATURAL JOIN mutual_date m
            ORDER BY shares ASC
            FETCH FIRST ROW ONLY);

        symbol := (SELECT symbol
            FROM owns JOIN customer c ON owns.login = c.login NATURAL JOIN mutual_date m
            ORDER BY shares ASC
            FETCH FIRST ROW ONLY);

        purchasable := (SELECT div(balance, price)
            FROM closing_price JOIN owns o on closing_price.symbol = o.symbol JOIN customer c2 on c2.login = o.login
            ORDER BY p_date DESC
            FETCH FIRST 2 ROWS ONLY);

        mutDate := (SELECT p_date
            FROM mutual_date);

        currDate := CAST(CURRENT_DATE AS DATE);

        IF mutDate = currDate THEN EXECUTE BUY_SHARES(userLog, symbol, purchasable);
        END IF;

    END;
    $$
    LANGUAGE 'plpgsql';

DROP TRIGGER IF EXISTS buy_on_date ON mutual_date;
CREATE TRIGGER buy_on_date
    AFTER UPDATE ON mutual_date
    EXECUTE FUNCTION buy_on_date();

/* Task/Question 6 */

CREATE OR REPLACE FUNCTION buy_on_price()
    RETURNS TRIGGER AS
    $$
    DECLARE
    userLog varchar(10);
    symbol varchar(20);
    purchasable int;
    prevPrice decimal(10, 2);
    currPrice decimal(10, 2);
    BEGIN
        userLog := (SELECT login
            FROM owns JOIN customer c ON owns.login = c.login NATURAL JOIN mutual_date m
            ORDER BY shares ASC
            FETCH FIRST ROW ONLY);

        symbol := (SELECT symbol
            FROM owns JOIN customer c ON owns.login = c.login NATURAL JOIN mutual_date m
            ORDER BY shares ASC
            FETCH FIRST ROW ONLY);

        purchasable := (SELECT div(balance, price)
            FROM closing_price JOIN owns o on closing_price.symbol = o.symbol JOIN customer c2 on c2.login = o.login
            ORDER BY p_date DESC
            FETCH FIRST 2 ROWS ONLY);

        prevPrice := (SELECT price
            FROM closing_price
            WHERE closing_price.symbol = symbol
            ORDER BY p_date DESC
            FETCH NEXT 1 ROW ONLY);

        currPrice := (SELECT price
            FROM closing_price
            WHERE closing_price.symbol = symbol
            ORDER BY p_date DESC
            FETCH FIRST ROW ONLY);

        IF prevPrice != currPrice THEN EXECUTE BUY_SHARES(userLog, symbol, purchasable);
        END IF;

    END;
    $$
    LANGUAGE 'plpgsql';

DROP TRIGGER IF EXISTS buy_on_price ON closing_price;
CREATE TRIGGER buy_on_price
    AFTER UPDATE ON closing_price
    EXECUTE FUNCTION buy_on_price();