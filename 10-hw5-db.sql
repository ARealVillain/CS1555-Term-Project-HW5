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


/* Administrators Information */


/* Transactions History Information */


/* Pseudo Date Information */

CREATE TABLE MUTUAL_DATE(
    p_date date,
    CONSTRAINT pk_mutualdate PRIMARY KEY (p_date)
);