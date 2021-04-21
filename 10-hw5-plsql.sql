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

    total_shares_val := shares_val * numb_shares;

    buyer_cap := (SELECT BALANCE FROM CUSTOMER
            WHERE login LIKE log
            FETCH FIRST ROW ONLY);

    last_trx := (SELECT MAX(trx_id) FROM trxlog);

    new_buyer_cap := buyer_cap - total_shares_val;

    IF new_buyer_cap < 0 THEN return False;
    END IF;

    UPDATE CUSTOMER
        SET balance = balance - total_shares_val
        WHERE login= log;

    INSERT INTO TRXLOG (trx_id,login,symbol,t_date,action,num_shares,price,amount) VALUES (last_trx+1, log, symb, CURRENT_DATE, 'buy',  numb_shares, shares_val, total_shares_val);

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

/*For proj- price intilization*/
CREATE OR REPLACE FUNCTION price_initialization()
    RETURNS TRIGGER AS
    $$
    DECLARE
    lowestPrice decimal(10, 2);
    BEGIN
        lowestPrice := (SELECT price
            FROM closing_price
            ORDER BY p_date DESC, price ASC
            FETCH FIRST ROW ONLY);
        raise notice 'Value: %', lowestPrice;
        INSERT INTO closing_price(symbol, price, p_date) VALUES(NEW.symbol, lowestPrice, current_date);
        raise notice 'Value: %', lowestPrice;
        RETURN NULL;
    END;
    $$
    LANGUAGE 'plpgsql';

DROP TRIGGER IF EXISTS price_initialization ON mutual_fund;
CREATE TRIGGER price_initialization
    AFTER INSERT ON mutual_fund
    FOR EACH ROW
    EXECUTE FUNCTION price_initialization();


/*Sell rebalance*/
DROP FUNCTION sell_rebalance();
CREATE OR REPLACE FUNCTION sell_rebalance()
    RETURNS TRIGGER AS
    $$
    DECLARE
    shares_val decimal(10, 2);
    total_shares_val decimal(10, 2);

    BEGIN
    /*Get the most recent day's value for the stock*/
    shares_val := (SELECT PRICE FROM closing_price
            WHERE symbol LIKE NEW.symbol
            ORDER BY P_DATE DESC
            FETCH FIRST ROW ONLY);

    total_shares_val := shares_val*NEW.num_shares;

    /* updates the customer table*/
    UPDATE CUSTOMER
        SET balance = balance + total_shares_val
        WHERE login=NEW.login;
    END;

    $$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS sell_rebalance ON trxlog;
CREATE TRIGGER price_initialization
    AFTER INSERT ON trxlog
    FOR EACH ROW
        WHEN (NEW.action = 'sell')
    EXECUTE FUNCTION sell_rebalance();

INSERT INTO TRXLOG (trx_id,login,symbol,t_date,action,num_shares,price,amount) VALUES (18, 'mike', NULL , TO_DATE('2020-03-29', 'YYYY-MM-DD'), 'sell',  NULL, NULL, 1000.00);
