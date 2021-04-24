/* Team 10 */
/* Krutarth Patel - kkp19 */
/* Erik Nordby - ern23 */
/* Kyle Tissue - kjt32 */

/* Mutual Fund Information */
DROP TABLE IF EXISTS MUTUAL_FUND CASCADE;
DROP TABLE IF EXISTS CLOSING_PRICE CASCADE;

CREATE TABLE MUTUAL_FUND(
    symbol varchar(20),
    name varchar(30),
    description varchar(100),
    category varchar(10),
    c_date date,
    CONSTRAINT pk_mutualfund PRIMARY KEY (symbol)
);

CREATE TABLE CLOSING_PRICE(
    symbol varchar(20),
    price decimal(10, 2),
    p_date date,
    CONSTRAINT pk_closingprice PRIMARY KEY (symbol, p_date),
    CONSTRAINT fk_closingprice FOREIGN KEY (symbol) REFERENCES MUTUAL_FUND(symbol) DEFERRABLE INITIALLY DEFERRED
);

/* Customers Information */

/* Creation of Customers Information Tables*/
DROP TABLE IF EXISTS CUSTOMER CASCADE;
DROP TABLE IF EXISTS ALLOCATION CASCADE;
DROP TABLE IF EXISTS PREFERS CASCADE;
DROP TABLE IF EXISTS OWNS CASCADE;

CREATE TABLE CUSTOMER
(
    login         varchar(10) UNIQUE,
    name          varchar(20),
    email         varchar(30),
    address       varchar(10),
    password      varchar(50),
    balance       numeric(10, 2) DEFAULT 0,
    CONSTRAINT CUSTOMER_PK PRIMARY KEY (login),
    CONSTRAINT EMAIL_UQ UNIQUE (email)
);

CREATE TABLE ALLOCATION
(
    allocation_no         int,
    login                 varchar(10),
    p_date                date,
    CONSTRAINT ALLOCATION_PK PRIMARY KEY (allocation_no),
    CONSTRAINT ALLOCATION_FK FOREIGN KEY (login) REFERENCES CUSTOMER (login)
);

CREATE TABLE PREFERS
(
    allocation_no         int,
    symbol                varchar(20),
    percentage            numeric(3, 2),
    CONSTRAINT PREFERS_PK PRIMARY KEY (allocation_no, symbol),
    CONSTRAINT PREFERS_FK1 FOREIGN KEY (allocation_no) REFERENCES ALLOCATION (allocation_no),
    CONSTRAINT PREFERS_FK2 FOREIGN KEY (symbol) REFERENCES MUTUAL_FUND (symbol)
);

CREATE TABLE OWNS
(
    login                 varchar(10),
    symbol                varchar(20),
    shares                int,
    CONSTRAINT OWNS_PK PRIMARY KEY (login, symbol),
    CONSTRAINT OWNS_FK1 FOREIGN KEY (login) REFERENCES CUSTOMER (login),
    CONSTRAINT OWNS_FK2 FOREIGN KEY (symbol) REFERENCES MUTUAL_FUND (symbol)
);


/* Administrators Information */
DROP TABLE IF EXISTS ADMINISTRATOR CASCADE;
CREATE TABLE ADMINISTRATOR(
    login varchar(10),
    name varchar(20),
    email varchar(30),
    address varchar(30),
    password varchar(10),
    CONSTRAINT pk_administrator PRIMARY KEY (login),
    CONSTRAINT unique_email UNIQUE (email)
);

/* Transactions History Information */
DROP TABLE IF EXISTS TRXLOG CASCADE;
CREATE TABLE TRXLOG(
    trx_id serial,
    login varchar(10),
    symbol varchar(20),
    t_date date,
    action varchar(10),
    num_shares int,
    price decimal(10,2),
    amount decimal(10,2),
    CONSTRAINT pk_trxlog PRIMARY KEY (trx_id),
    CONSTRAINT fk_trxlog_login FOREIGN KEY (login) REFERENCES CUSTOMER(login),
    CONSTRAINT fk_trxlog_symbol FOREIGN KEY (symbol) REFERENCES MUTUAL_FUND(symbol)

);

/* Pseudo Date Information */
DROP TABLE IF EXISTS MUTUAL_DATE CASCADE;
CREATE TABLE MUTUAL_DATE(
    p_date date,
    CONSTRAINT pk_mutualdate PRIMARY KEY (p_date)
);

/* Team 10 */
/* Krutarth Patel - kkp19 */
/* Erik Nordby - ern23 */
/* Kyle Tissue - kjt32 */




/* Task/Question 2 */
DROP FUNCTION search_mutual_funds(arg1 varchar, arg2 varchar);
CREATE OR REPLACE FUNCTION SEARCH_MUTUAL_FUNDS (arg1 varchar(30), arg2 varchar(30))
    RETURNS VARCHAR(10000000) AS $$
    DECLARE
    fund_list VARCHAR(10000000) := '[';
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

/*SELECT SEARCH_MUTUAL_FUNDS('stock', 'bond');*/

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
        bought boolean;

        /*query to determine allocation number that has the same login as the parameter with max date*/
        allocation_number int := (select allocation_no from allocation where login=cur_login order by P_DATE desc limit 1);
        preference_cursor CURSOR FOR SELECT allocation_no,symbol,percentage FROM PREFERS WHERE allocation_no=allocation_number;
    BEGIN
        OPEN preference_cursor;
        /* loops through the cursor that has stored every allocation_no that matches the login given*/
        INSERT INTO TRXLOG (login,symbol,t_date,action,amount) VALUES ( cur_login, cur_symbol, (select * from mutual_date), 'deposit', amount);
        LOOP
            FETCH preference_cursor INTO cur_allocation;
            if not found then
                exit;
            end if;
            /*get the symbol percent and price that correspond to the current allocation_no*/
            cur_symbol := cur_allocation.symbol;
            cur_percent := cur_allocation.percentage;

            cur_price := (select price from closing_price where symbol=cur_symbol and p_date=(select * from mutual_date) order by p_date desc limit 1);
            if cur_price is null then cur_price := (select price from closing_price where symbol=cur_symbol order by p_date desc limit 1); end if;

            /* calculate the amount of shares that should be purchased*/
            shares_to_buy:=FLOOR(cur_percent*amount/cur_price);


            /* use the buy_shares function to purchase the shares*/
            if shares_to_buy > 0 then
                bought := buy_shares(cur_login, cur_symbol, shares_to_buy);
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
            WHERE symbol LIKE symb and p_date = (select * from mutual_date)
            ORDER BY P_DATE DESC
            FETCH FIRST ROW ONLY);

    if shares_val is null then
        shares_val := (SELECT PRICE FROM closing_price
            WHERE symbol LIKE symb
            ORDER BY P_DATE DESC
            FETCH FIRST ROW ONLY);
    end if;

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

    INSERT INTO TRXLOG (login,symbol,t_date,action,num_shares,price,amount) VALUES ( log, symb, (select * from mutual_date), 'buy',  numb_shares, shares_val, total_shares_val);

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


/*SELECT BUY_SHARES('mike', 'IMS', 1);*/


/* Task/Question 4 */
DROP FUNCTION buy_shares_by_amount(login varchar, symb varchar, num_shares integer);
CREATE OR REPLACE FUNCTION BUY_SHARES_by_amount (log varchar(30), symb varchar(30), amount int)
    RETURNS BOOLEAN
    AS $$
    DECLARE
    shares_val decimal(10, 2);
    total_shares_to_buy decimal(10, 2);
    buyer_cap decimal(10, 2);
    new_buyer_cap decimal(10, 2);
    last_trx int;
    BEGIN
    /*Get the most recent day's value for the stock*/
    shares_val := (SELECT PRICE FROM closing_price
            WHERE symbol LIKE symb and p_date = (select * from mutual_date)
            ORDER BY P_DATE DESC
            FETCH FIRST ROW ONLY);

    if shares_val is null then
        shares_val := (SELECT PRICE FROM closing_price
            WHERE symbol LIKE symb
            ORDER BY P_DATE DESC
            FETCH FIRST ROW ONLY);
    end if;



    buyer_cap := (SELECT BALANCE FROM CUSTOMER
            WHERE login LIKE log
            FETCH FIRST ROW ONLY);

    total_shares_to_buy := FLOOR(amount/shares_val);

    new_buyer_cap := buyer_cap - amount;

    IF new_buyer_cap < 0 THEN return False;
    END IF;

    UPDATE CUSTOMER
        SET balance = balance - total_shares_to_buy*shares_val
        WHERE login= log;

    INSERT INTO TRXLOG (login,symbol,t_date,action,num_shares,price,amount) VALUES (log, symb, (select * from mutual_date), 'buy',  total_shares_to_buy, shares_val, total_shares_to_buy*shares_val);

    /* updates the owns table to list the newly purchased shares*/
    perform shares from owns where login = log and symbol = symb;
    if found then
        update owns
        set shares = shares + total_shares_to_buy
        where login=log and symbol=symb;
    else
        INSERT INTO OWNS(login,symbol,shares) VALUES(log,symb,total_shares_to_buy);
    end if;
    RETURN TRUE;

    END;
    $$ LANGUAGE plpgsql;

/*SELECT BUY_SHARES_BY_AMOUNT('erik', 'MM', 1);*/


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
        userLog := (SELECT owns.login
            FROM owns JOIN customer c ON owns.login = c.login NATURAL JOIN mutual_date m
            ORDER BY shares ASC
            FETCH FIRST ROW ONLY);

        symbol := (SELECT owns.symbol
            FROM owns JOIN customer c ON owns.login = c.login NATURAL JOIN mutual_date m
            ORDER BY shares ASC
            FETCH FIRST ROW ONLY);

        purchasable := (SELECT div(balance, price)
            FROM closing_price JOIN owns o on closing_price.symbol = o.symbol JOIN customer c2 on c2.login = o.login
            ORDER BY p_date DESC
            FETCH FIRST ROW ONLY);

        mutDate := (SELECT p_date
            FROM mutual_date);

        currDate := CAST(CURRENT_DATE AS DATE);

        IF mutDate = currDate THEN EXECUTE BUY_SHARES(userLog, symbol, purchasable);
        END IF;
    RETURN NULL;
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

        if lowestPrice IS NULL THEN RETURN NULL; end if;

        INSERT INTO closing_price(symbol, price, p_date) VALUES(NEW.symbol, lowestPrice, (select * from mutual_date));
        RETURN NULL;
    END;
    $$
    LANGUAGE 'plpgsql';

DROP TRIGGER IF EXISTS price_initialization ON mutual_fund;
CREATE TRIGGER price_initialization
    AFTER INSERT ON mutual_fund
    FOR EACH ROW
    EXECUTE FUNCTION price_initialization();

/*Sell rebalance --Assumption: We should also update owns since there are shares being sold*/
CREATE OR REPLACE FUNCTION sell_rebalance()
    RETURNS TRIGGER AS
    $$
    DECLARE
    shares_val decimal(10, 2);
    total_shares_val decimal(10, 2);
    BEGIN

    /*Get the most recent day's value for the stock*/
    shares_val := (SELECT PRICE FROM closing_price
            WHERE symbol LIKE NEW.symbol and p_date = NEW.t_date
            ORDER BY P_DATE DESC
            FETCH FIRST ROW ONLY);

    if shares_val is null then
        shares_val := (SELECT PRICE FROM closing_price
            WHERE symbol LIKE NEW.symbol
            ORDER BY P_DATE DESC
            FETCH FIRST ROW ONLY);
    end if;

    total_shares_val := shares_val*NEW.num_shares;

    /* updates the customer table*/
    UPDATE CUSTOMER
        SET balance = balance + total_shares_val
        WHERE login=NEW.login;
    return NULL;
    END;

    $$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS sell_rebalance ON trxlog;
CREATE TRIGGER sell_rebalance
    AFTER INSERT ON trxlog
    FOR EACH ROW
        WHEN (NEW.action = 'sell')
    EXECUTE FUNCTION sell_rebalance();


/*price_jump trigger*/
CREATE OR REPLACE FUNCTION price_jump()
    RETURNS TRIGGER AS
    $$
    DECLARE
    owners record;
    curSymbol varchar(20);
    prevPrice decimal(10, 2);
    curPrice decimal(10, 2);
    ownedShares int;
    owns_cursor CURSOR FOR SELECT login,shares from owns where symbol like NEW.symbol;
    BEGIN
        /* if the price change was large enough continue otherwise do nothing*/
        curPrice:=NEW.price;
        curSymbol:=NEW.symbol;

        prevPrice:=(SELECT price
            FROM closing_price
            WHERE closing_price.symbol = curSymbol
            ORDER BY p_date DESC
            LIMIT 1 OFFSET 1);

        IF prevPrice + 10 < curPrice THEN
            /*loop through everyone who owns the current symbol and sell that symbol*/
            OPEN owns_cursor;
            LOOP
                FETCH owns_cursor INTO owners;
                if not found then
                    exit;
                end if;
                ownedShares:=owners.shares;
                /*insert the current transaction which will update the balance of the user with the above trigger*/
                INSERT INTO TRXLOG (login,symbol,t_date,action,num_shares,price,amount) VALUES (owners.login, curSymbol , (select * from mutual_date), 'sell',  ownedShares, curPrice, curPrice*ownedShares);
                /* delete the shares from the owns table to sell them*/
                DELETE FROM owns where symbol like curSymbol and login like owners.Login;
            END LOOP;
        END IF;
        return NULL;
    END;
    $$
    LANGUAGE 'plpgsql';

DROP TRIGGER IF EXISTS price_jump ON closing_price;
CREATE TRIGGER price_jump
    AFTER INSERT ON closing_price
    for each row
    EXECUTE FUNCTION price_jump();

/*INSERT INTO owns (login,symbol,shares) VALUES ('mike','MM',8);
INSERT INTO closing_price (symbol,price,p_date) VALUES ('MM',30, (select * from mutual_date));
*/

/*Sell shares*/
CREATE OR REPLACE FUNCTION SELL_SHARES (log varchar(30), symb varchar(30), numb_shares int)
    RETURNS BOOLEAN
    AS $$
    DECLARE
    shares_val decimal(10, 2);
    total_shares_val decimal(10, 2);

    buyer_shares decimal(10, 2);
    new_buyer_shares decimal(10, 2);
    BEGIN

    shares_val := (SELECT PRICE FROM closing_price
            WHERE symbol LIKE symb and p_date = (select * from mutual_date)
            ORDER BY P_DATE DESC
            FETCH FIRST ROW ONLY);

    if shares_val is null then
        shares_val := (SELECT PRICE FROM closing_price
            WHERE symbol LIKE symb
            ORDER BY P_DATE DESC
            FETCH FIRST ROW ONLY);
    end if;

    total_shares_val := shares_val * numb_shares;

    buyer_shares := (SELECT shares FROM OWNS
            WHERE login LIKE log and symbol like symb
            FETCH FIRST ROW ONLY);

    IF buyer_shares is null THEN return False;
    END IF;

    new_buyer_shares := buyer_shares - numb_shares;

    IF new_buyer_shares < 0 THEN return False;
    END IF;

    /*Updates the owns table*/
    UPDATE OWNS
        SET shares = shares - numb_shares
        WHERE login=login and symbol=symb;

    DELETE FROM owns where shares=0;


    /*This calls the trigger that we had to make*/
    INSERT INTO TRXLOG (login,symbol,t_date,action,num_shares,price,amount) VALUES (log, symb, (select * from mutual_date), 'sell',  numb_shares, shares_val, total_shares_val);

    RETURN TRUE;

    END;
    $$ LANGUAGE plpgsql;

/*SELECT SELL_SHARES( 'mike', 'MM', 1);*/

/* join login to closing price where we have retrived the latest closing price for all of the stocks*/
CREATE OR REPLACE VIEW MOST_RECENT_STOCK_VAL AS
    SELECT CLOSING_PRICE.symbol, price FROM CLOSING_PRICE JOIN (
            SELECT symbol, MAX(p_date) as p_date from closing_price
            GROUP BY symbol)
            as MAX_DATE on CLOSING_PRICE.p_date = MAX_DATE.p_date and CLOSING_PRICE.symbol = MAX_DATE.symbol

CREATE OR REPLACE VIEW INVESTOR_RANK AS
    SELECT login, sum(shares*price) FROM OWNS JOIN
        MOST_RECENT_STOCK_VAL ON OWNS.symbol = MOST_RECENT_STOCK_VAL.symbol
    GROUP BY login
    ORDER BY sum(shares*price) DESC;

CREATE OR REPLACE FUNCTION get_portfolio(user_login varchar)
RETURNS TABLE(mf_symb varchar(20), mf_shares_owned int, mf_val decimal(10,2), mf_cost decimal(10,2), mf_adj_cost decimal(10,2), mf_yield decimal(10,2) ) AS
    $$DECLARE
    BEGIN

    RETURN QUERY SELECT bought_sold.symbol, shares as shares_owned, shares*price as current_val_of_mutual_fund, buy as cost, buy-sell as adj_cost, (shares*price)-(buy-sell) as yield
        FROM (SELECT login, buy.symbol as symbol, buy, sell FROM (SELECT login, action, symbol, sum(amount) as buy from trxlog
                WHERE login=user_login and action='buy'
                GROUP BY login, action, symbol) as buy
                LEFT JOIN
                (SELECT action, symbol, sum(amount) as sell from trxlog
                WHERE login=user_login and action='sell'
                GROUP BY action, symbol) as sell
                    on buy.symbol = sell.symbol) as bought_sold
            join
            owns on owns.login = bought_sold.login and owns.symbol = bought_sold.symbol
            join
            most_recent_stock_val on bought_sold.symbol = most_recent_stock_val.symbol;
    END;
    $$
    LANGUAGE 'plpgsql';

select * from get_portfolio('mi');


INSERT INTO ADMINISTRATOR (login,name,email,address,password) VALUES ('admin', 'Administrator', 'admin@betterfuture.com' ,'5th Ave, Pitt', 'root');
INSERT INTO MUTUAL_DATE (p_date) VALUES (TO_DATE('2020-04-04', 'YYYY-MM-DD'));