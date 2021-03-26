/* Team 10 */
/* Krutarth Patel - kkp19 */
/* Erik Nordby - ern23 */
/* Kyle Tissue - kjt32 */

INSERT INTO ADMINISTRATOR (login,name,email,address,password) VALUES ('admin', 'Administrator', 'admin@betterfuture.com' ,'5th Ave, Pitt', 'root');

INSERT INTO CUSTOMER (login,name,email,address,password,balance) VALUES ('mike', 'Mike Costa', 'mike@betterfuture.com', '1st street', 'pwd', 750.00);
INSERT INTO CUSTOMER (login,name,email,address,password,balance) VALUES ('mary', 'Mary Chrysanthis', 'mary@betterfuture.com', '2nd street', 'pwd', 0.00);

INSERT INTO ALLOCATION (allocation_no,login,p_date) VALUES (0, 'mike', TO_DATE('2020-03-28', 'YYYY-MM-DD'));
INSERT INTO ALLOCATION (allocation_no,login,p_date) VALUES (1, 'mary', TO_DATE('2020-03-29', 'YYYY-MM-DD'));
INSERT INTO ALLOCATION (allocation_no,login,p_date) VALUES (2, 'mike', TO_DATE('2020-03-03', 'YYYY-MM-DD'));

INSERT INTO MUTUAL_FUND (symbol,name,description,category,c_date) VALUES ( 'MM', 'money-market', 'money market, conservative', 'fixed', TO_DATE('2020-01-06', 'YYYY-MM-DD') );
INSERT INTO MUTUAL_FUND (symbol,name,description,category,c_date) VALUES ( 'RE', 'real-estate', 'real estate', 'fixed', TO_DATE('2020-01-09', 'YYYY-MM-DD') );
INSERT INTO MUTUAL_FUND (symbol,name,description,category,c_date) VALUES ( 'STB', 'short-term-bonds', 'short term bonds', 'bonds', TO_DATE('2020-01-10', 'YYYY-MM-DD') );
INSERT INTO MUTUAL_FUND (symbol,name,description,category,c_date) VALUES ( 'LTB', 'long-term-bonds', 'long term bonds', 'bonds', TO_DATE('2020-01-11', 'YYYY-MM-DD') );
INSERT INTO MUTUAL_FUND (symbol,name,description,category,c_date) VALUES ( 'BBS', 'balance-bonds-stocks', 'balance bonds and stocks', 'mixed', TO_DATE('2020-01-12', 'YYYY-MM-DD') );
INSERT INTO MUTUAL_FUND (symbol,name,description,category,c_date) VALUES ( 'SRBS', 'balance-response-bonds-stocks', 'social responsibility and stocks', 'mixed', TO_DATE('2020-01-12', 'YYYY-MM-DD') );
INSERT INTO MUTUAL_FUND (symbol,name,description,category,c_date) VALUES ( 'GS', 'general-stocks', 'general stocks', 'stocks', TO_DATE('2020-01-16', 'YYYY-MM-DD') );
INSERT INTO MUTUAL_FUND (symbol,name,description,category,c_date) VALUES ( 'AS', 'aggressive-stocks', 'aggressive stocks', 'stocks', TO_DATE('2020-01-23', 'YYYY-MM-DD') );
INSERT INTO MUTUAL_FUND (symbol,name,description,category,c_date) VALUES ( 'IMS', 'international-markets-stock', 'international markets stock, risky', 'stocks', TO_DATE('2020-01-30', 'YYYY-MM-DD') );

INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('MM', 10.00, TO_DATE('2020-03-28', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('MM', 11.00, TO_DATE('2020-03-29', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('MM', 12.00, TO_DATE('2020-03-30', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('MM', 15.00, TO_DATE('2020-03-31', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('MM', 14.00, TO_DATE('2020-04-01', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('MM', 15.00, TO_DATE('2020-04-02', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('MM', 16.00, TO_DATE('2020-04-03', 'YYYY-MM-DD'));

INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('RE', 10.00, TO_DATE('2020-03-28', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('RE', 12.00, TO_DATE('2020-03-29', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('RE', 15.00, TO_DATE('2020-03-30', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('RE', 14.00, TO_DATE('2020-03-31', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('RE', 16.00, TO_DATE('2020-04-01', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('RE', 17.00, TO_DATE('2020-04-02', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('RE', 15.00, TO_DATE('2020-04-03', 'YYYY-MM-DD'));

INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('STB', 10.00, TO_DATE('2020-03-28', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('STB', 9.00, TO_DATE('2020-03-29', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('STB', 10.00, TO_DATE('2020-03-30', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('STB', 12.00, TO_DATE('2020-03-31', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('STB', 14.00, TO_DATE('2020-04-01', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('STB', 10.00, TO_DATE('2020-04-02', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('STB', 12.00, TO_DATE('2020-04-03', 'YYYY-MM-DD'));

INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('LTB', 10.00, TO_DATE('2020-03-28', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('LTB', 12.00, TO_DATE('2020-03-29', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('LTB', 13.00, TO_DATE('2020-03-30', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('LTB', 15.00, TO_DATE('2020-03-31', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('LTB', 12.00, TO_DATE('2020-04-01', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('LTB', 9.00, TO_DATE('2020-04-02', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('LTB', 10.00, TO_DATE('2020-04-03', 'YYYY-MM-DD'));

INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('BBS', 10.00, TO_DATE('2020-03-28', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('BBS', 11.00, TO_DATE('2020-03-29', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('BBS', 14.00, TO_DATE('2020-03-30', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('BBS', 18.00, TO_DATE('2020-03-31', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('BBS', 13.00, TO_DATE('2020-04-01', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('BBS', 15.00, TO_DATE('2020-04-02', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('BBS', 16.00, TO_DATE('2020-04-03', 'YYYY-MM-DD'));

INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('SRBS', 10.00, TO_DATE('2020-03-28', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('SRBS', 12.00, TO_DATE('2020-03-29', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('SRBS', 12.00, TO_DATE('2020-03-30', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('SRBS', 14.00, TO_DATE('2020-03-31', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('SRBS', 17.00, TO_DATE('2020-04-01', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('SRBS', 20.00, TO_DATE('2020-04-02', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('SRBS', 20.00, TO_DATE('2020-04-03', 'YYYY-MM-DD'));

INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('GS', 10.00, TO_DATE('2020-03-28', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('GS', 12.00, TO_DATE('2020-03-29', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('GS', 13.00, TO_DATE('2020-03-30', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('GS', 15.00, TO_DATE('2020-03-31', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('GS', 14.00, TO_DATE('2020-04-01', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('GS', 15.00, TO_DATE('2020-04-02', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('GS', 12.00, TO_DATE('2020-04-03', 'YYYY-MM-DD'));

INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('AS', 10.00, TO_DATE('2020-03-28', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('AS', 15.00, TO_DATE('2020-03-29', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('AS', 14.00, TO_DATE('2020-03-30', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('AS', 16.00, TO_DATE('2020-03-31', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('AS', 14.00, TO_DATE('2020-04-01', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('AS', 17.00, TO_DATE('2020-04-02', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('AS', 18.00, TO_DATE('2020-04-03', 'YYYY-MM-DD'));

INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('IMS', 10.00, TO_DATE('2020-03-28', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('IMS', 12.00, TO_DATE('2020-03-29', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('IMS', 12.00, TO_DATE('2020-03-30', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('IMS', 14.00, TO_DATE('2020-03-31', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('IMS', 13.00, TO_DATE('2020-04-01', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('IMS', 12.00, TO_DATE('2020-04-02', 'YYYY-MM-DD'));
INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES ('IMS', 11.00, TO_DATE('2020-04-03', 'YYYY-MM-DD'));

INSERT INTO MUTUAL_DATE (p_date) VALUES (TO_DATE('2020-04-04', 'YYYY-MM-DD'));

INSERT INTO OWNS (login, symbol, shares) VALUES ('mike', 'RE', 50);

INSERT INTO PREFERS (allocation_no, symbol, percentage) VALUES (0, 'MM', 0.50);
INSERT INTO PREFERS (allocation_no, symbol, percentage) VALUES (0, 'RE', 0.50);
INSERT INTO PREFERS (allocation_no, symbol, percentage) VALUES (1, 'STB', 0.20);
INSERT INTO PREFERS (allocation_no, symbol, percentage) VALUES (1, 'LTB', 0.40);
INSERT INTO PREFERS (allocation_no, symbol, percentage) VALUES (1, 'BBS', 0.40);
INSERT INTO PREFERS (allocation_no, symbol, percentage) VALUES (2, 'GS', 0.30);
INSERT INTO PREFERS (allocation_no, symbol, percentage) VALUES (2, 'AS', 0.30);
INSERT INTO PREFERS (allocation_no, symbol, percentage) VALUES (2, 'IMS', 0.40);

INSERT INTO TRXLOG (trx_id,login,symbol,t_date,action,num_shares,price,amount) VALUES (1, 'mike', NULL , TO_DATE('2020-03-29', 'YYYY-MM-DD'), 'deposit',  NULL, NULL, 1000.00);
INSERT INTO TRXLOG (trx_id,login,symbol,t_date,action,num_shares,price,amount) VALUES (2, 'mike', 'MM', TO_DATE('2020-03-29', 'YYYY-MM-DD'), 'buy', 50, 10.00 , 500.00);
INSERT INTO TRXLOG (trx_id,login,symbol,t_date,action,num_shares,price,amount) VALUES (3, 'mike', 'RE', TO_DATE('2020-03-29', 'YYYY-MM-DD'), 'buy', 50, 10.00, 500.00);
INSERT INTO TRXLOG (trx_id,login,symbol,t_date,action,num_shares,price,amount) VALUES (4, 'mike', 'MM', TO_DATE('2020-04-01', 'YYYY-MM-DD'), 'sell', 50, 15.00, 1000.00);
