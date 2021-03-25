


/* Mutual Fund Information */

CREATE TABLE MUTUALFUND(
    symbol varchar(20),
    name varchar(20),
    description varchar(20),
    category varchar(20),
    c_date date,
    CONSTRAINT pk_mutualfund PRIMARY KEY (symbol)
);

CREATE TABLE CLOSING_PRICE(
    symbol varchar(20),
    price decimal(10, 2),
    p_date date,
    CONSTRAINT pk_closingprice PRIMARY KEY (symbol, p_date),
    CONSTRAINT fk_closingprice FOREIGN KEY (symbol) REFERENCES MUTUALFUND(symbol)
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
    balance       numeric(10, 2)
    CONSTRAINT CUSTOMER_PK PRIMARY KEY (login),
    CONSTRAINT EMAIL_UQ UNIQUE (email),
    CONSTRAINT PROFILE_CHK CHECK (user_id >= 0)
);

CREATE TABLE ALLOCATION
(
    allocation_no         int,
    login                 varchar(10),
    p_date                date,
    CONSTRAINT ALLOCATION_PK PRIMARY KEY (allocation_no),
    CONSTRAINT ALLOCATION_FK FOREIGN KEY (login) REFERENCES CUSTOMER ()
);

CREATE TABLE PREFERS
(
    allocation_no         int,
    symbol                varchar(20),
    percentage            numeric(3, 2),
    CONSTRAINT PREFERS_PK PRIMARY KEY (allocation_no, symbol),
    CONSTRAINT  UNIQUE (email),
    CONSTRAINT PROFILE_CHK CHECK (user_id >= 0)
);

CREATE TABLE OWNS
(
    login                 varchar(10),
    symbol                varchar(20),
    shares                int,
    CONSTRAINT CUSTOMER_PK PRIMARY KEY (login),
    CONSTRAINT EMAIL_UQ UNIQUE (email),
    CONSTRAINT PROFILE_CHK CHECK (user_id >= 0)
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
    CONSTRAINT fk_trxlog_symbol FOREIGN KEY (symbol) REFERENCES MUTUALFUND(symbol)

);

/* Pseudo Date Information */

CREATE TABLE MUTUAL_DATE(
    p_date date,
    CONSTRAINT pk_mutualdate PRIMARY KEY (p_date)
);

