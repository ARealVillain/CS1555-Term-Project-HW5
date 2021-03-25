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
