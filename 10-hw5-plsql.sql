/* Question 2*/
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