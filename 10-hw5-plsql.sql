/* Question 2 */
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

/*Question 3*/


/* Question 4 */
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

/* Question 5 */

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

/* CODE FOR FIRING TRIGGER???????? */

/* Question 6 */

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

/* CODE FOR FIRING TRIGGER???????? */