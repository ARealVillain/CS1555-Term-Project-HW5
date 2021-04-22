import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.text.SimpleDateFormat;

public class teamTenProj {
    public static void main(String args[]) throws Exception {

        //Set up connection with SQL
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost/postgres";
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "102Camelot");
        Connection conn = DriverManager.getConnection(url, props);

        Statement st = conn.createStatement();
        

        Scanner scan = new Scanner(System.in);
        System.out.println("Are you an admin or a customer?");
        System.out.println("Please type in <admin> or <customer> or <quit> to exit the program");
        System.out.println("------------------------------------------------------------------");
        String userType;
        String userOp;
        userType = scan.nextLine();
        while(!userType.equals("quit")) {
            System.out.println("------------------------------------------------------------------");
            if(userType.equals("admin")) {
                //Login... should probably be moved to a function
                Boolean loggedIn = false;
                while(loggedIn == false){
                    System.out.println("Please login to continue: ");
                    System.out.println("What is your user name?");
                    String userName = scan.nextLine();
                    System.out.println("What is your password?");
                    String password = scan.nextLine();

                    //create a query
                    String loginQuery = "SELECT * FROM ADMINISTRATOR WHERE login=? and password=?";
                    PreparedStatement loginPs = conn.prepareStatement(loginQuery);
                    loginPs.setString(1, userName);
                    loginPs.setString(2, password);

                    //execute a query
                    ResultSet res1 = loginPs.executeQuery();

                    //Assess
                    String rid = null;
                    while (res1.next()) {
                        rid = res1.getString("login");
                    }
                    
                    if (rid == null)
                        System.out.println("\nSorry thats a bad login!\n");
                    else
                        loggedIn = true;
                }


                boolean adminFlag = true;
                while(adminFlag) {
                    printAdminMenu();
                    System.out.println("------------------------------------------------------------------");
                    userOp = scan.nextLine();
                    System.out.println("------------------------------------------------------------------");
                    if(userOp.equals("1")) {
                        eraseDatabase(conn, scan);
                    } else if(userOp.equals("2")) {
                        addCustomer(conn, scan);
                    } else if(userOp.equals("3")) {
                        addMutualFund(conn, scan);
                    } else if(userOp.equals("4")) {
                        updateShares(conn, scan);
                    } else if(userOp.equals("5")) {
                        topK(conn, scan);
                    } else if(userOp.equals("6")) {
                        rankInvestors(conn, scan);
                    } else if(userOp.equals("7")) {
                        updateDate(conn, scan);
                    } else if(userOp.equals("8")) {
                        adminFlag = false;
                    } else if(userOp.equals("9")) {
                        adminFlag = false;
                        userType = "quit";
                    } else {
                        System.out.println("That is not a valid option");
                        System.out.println("------------------------------------------------------------------");
                    }
                }
            } else if(userType.equals("customer")) {
                boolean custFlag = true;

                //Login... should probably be moved to a function
                Boolean loggedIn = false;
                String userName = "";
                String password = "";
                while(loggedIn == false){

                    System.out.println("Please login to continue: ");
                    System.out.println("What is your user name?");
                    userName = scan.nextLine();
                    System.out.println("What is your password?");
                    password = scan.nextLine();

                    //create a query
                    String loginQuery = "SELECT * FROM CUSTOMER WHERE login=? and password=?";
                    PreparedStatement loginPs = conn.prepareStatement(loginQuery);
                    loginPs.setString(1, userName);
                    loginPs.setString(2, password);

                    //execute a query
                    ResultSet res1 = loginPs.executeQuery();

                    //Assess
                    String rid = null;
                    while (res1.next()) {
                        rid = res1.getString("login");
                    }
                    
                    if (rid == null)
                        System.out.println("\nSorry thats a bad login!\n");
                    else
                        loggedIn = true;
                }

                while(custFlag) {
                    //might have to ask for customer information here since it is not asked for in functions but needed for them
                    printCustomerMenu();
                    System.out.println("------------------------------------------------------------------");
                    userOp = scan.nextLine();
                    System.out.println("------------------------------------------------------------------");
                    if(userOp.equals("1")) {
                        showBalance(userName, conn);
                    } else if(userOp.equals("2")) {
                        showMFNames(conn);
                    } else if(userOp.equals("3")) {
                        showMFPrices(conn, scan);
                    } else if(userOp.equals("4")) {
                        searchMutualFund(conn, scan);
                    } else if(userOp.equals("5")) {
                        depositAmount(userName, conn, scan);
                    } else if(userOp.equals("6")) {
                        buyShares(userName, conn, scan);
                    } else if(userOp.equals("7")) {
                        sellShares(userName, conn, scan);
                    } else if(userOp.equals("8")) {
                        showROI();
                    } else if(userOp.equals("9")) {
                        predict();
                    } else if(userOp.equals("10")) {
                        changePreference();
                    } else if(userOp.equals("11")) {
                        rankAllocations();
                    } else if(userOp.equals("12")) {
                        showPortfolio();
                    } else if(userOp.equals("13")) {
                        custFlag = false;
                    } else if(userOp.equals("14")) {
                        custFlag = false;
                        userType = "quit";
                    } else {
                        System.out.println("That is not a valid option");
                        System.out.println("------------------------------------------------------------------");
                    }
                }
            } else {
                System.out.println("Invalid Input");
                System.out.println("------------------------------------------------------------------");
            }
            if(!userType.equals("quit")) {
                System.out.println("Are you an admin or a customer?");
                System.out.println("Please type in <admin> or <customer> or <quit> to exit the program");
                System.out.println("------------------------------------------------------------------");
                userType = scan.nextLine();
                System.out.println("------------------------------------------------------------------");
            }
        }
        System.out.println("Thank you for using Better Future!");
        System.out.println("Goodbye!");
        System.out.println("------------------------------------------------------------------");
        scan.close();
    }

    private static void showPortfolio() {
        System.out.println("Function to show customer portfolio");
        System.out.println("------------------------------------------------------------------");
        return;
    }

    private static void rankAllocations() {
        System.out.println("Function to rank customer allocations");
        System.out.println("------------------------------------------------------------------");
        return;
    }

    private static void changePreference() {
        System.out.println("Function to change customer preference");
        System.out.println("------------------------------------------------------------------");
        return;
    }

    private static void predict() {
        System.out.println("Function to predict gains or losses of the customer transactions");
        System.out.println("------------------------------------------------------------------");
        return;
    }

    private static void showROI() {
        System.out.println("Function to calculate and show return of investment");
        System.out.println("------------------------------------------------------------------");
        return;
    }

    //Assumption: We should also update owns even though there is not 
    private static void sellShares(String userName, Connection conn, Scanner scn) throws SQLException {
        System.out.println("Function to sell shares");
        System.out.println("------------------------------------------------------------------");
        System.out.println("What symbol would you like to sell?");
        String symbol = scn.nextLine();

        System.out.println("What number of shares would you like to sell?");
        int numberShares = Integer.parseInt(scn.nextLine());

        CallableStatement sellShares = conn.prepareCall("{?=call sell_shares( ? , ? , ? )}");
        sellShares.registerOutParameter(1, Types.BOOLEAN);
        sellShares.setString(2, userName);
        sellShares.setString(3, symbol);
        sellShares.setInt(4, numberShares);
        sellShares.execute();

        Boolean succeeded = sellShares.getBoolean(1);
        if(!succeeded)
            System.out.println("Sorry something went wrong");
        sellShares.close();
        return;
    }

    private static void buyShares(String userName, Connection conn, Scanner scn) throws SQLException {
        System.out.println("Function to buy shares");
        System.out.println("------------------------------------------------------------------");

        System.out.println("Would you like to specify the <number> or the <amount> to be spent");
        String choice = scn.nextLine();
        System.out.println(choice);
        if(choice.equals("number")){

            System.out.println("What symbol would you like to buy?");
            String symbol = scn.nextLine();

            System.out.println("What number of shares would you like to buy?");
            int numberShares = Integer.parseInt(scn.nextLine());

            CallableStatement buyShares = conn.prepareCall("{?=call buy_shares( ? , ? , ? )}");
            buyShares.registerOutParameter(1, Types.BOOLEAN);
            buyShares.setString(2, userName);
            buyShares.setString(3, symbol);
            buyShares.setInt(4, numberShares);
            buyShares.execute();

            Boolean succeeded = buyShares.getBoolean(1);
            if(!succeeded)
                System.out.println("Sorry not enough funds");
            buyShares.close();
        }
        else if(choice.equals("amount")){
            //Create new version of buy_shares that is based on amount
            System.out.println("What symbol would you like to buy?");
            String symbol = scn.nextLine();

            System.out.println("What number of shares would you like to buy?");
            int amount = Integer.parseInt(scn.nextLine());

            CallableStatement buyShares = conn.prepareCall("{?=call buy_shares_by_amount( ? , ? , ? )}");
            buyShares.registerOutParameter(1, Types.BOOLEAN);
            buyShares.setString(2, userName);
            buyShares.setString(3, symbol);
            buyShares.setInt(4, amount);
            buyShares.execute();

            Boolean succeeded = buyShares.getBoolean(1);
            if(!succeeded)
                System.out.println("Sorry not enough funds");
            buyShares.close();
        }

        return;
    }

    //I think deposit for investment needs to be tweaked---- NEEDS FIXED
    private static void depositAmount(String userName, Connection conn, Scanner scn) throws SQLException {
        System.out.println("Function to deposit amount for investment");
        System.out.println("------------------------------------------------------------------");

        System.out.println("How much of it would you like to deposit");
        String amount = scn.nextLine();

        CallableStatement searchFunds = conn.prepareCall("call deposit_for_investment( ? , ? )");
        searchFunds.setString(1, userName);
        searchFunds.setInt(2, Integer.parseInt(amount));
        searchFunds.execute();
        searchFunds.close();
        
        return;
    }

    private static void searchMutualFund( Connection conn, Scanner scn) throws SQLException {
        System.out.println("Function to search for a mutual fund");
        System.out.println("------------------------------------------------------------------");
        Boolean badKeywords = true;
        String kw1 = "";
        String kw2 = "";
        while(badKeywords){
            System.out.println("What is the first keyword you would like to use? Please make sure it is less than 30 chars");
            kw1 = scn.nextLine();
            System.out.println("What is the second keyword you would like to use? Please make sure it is less than 30 chars");
            kw2 = scn.nextLine();

            if( kw1.length() <= 30 || kw2.length() <= 30)
                badKeywords = false;
            else   
                System.out.println("Sorry, that's too long");
        }


        CallableStatement searchFunds = conn.prepareCall("{?= call search_mutual_funds( ? , ? )}");
        searchFunds.registerOutParameter(1, Types.VARCHAR);
        searchFunds.setString(2, kw1);
        searchFunds.setString(3, kw2);
        searchFunds.execute();

        String rReturn = searchFunds.getString(1);
        searchFunds.close();
        System.out.println(rReturn);

        return;
    }

    private static void showMFPrices(Connection conn, Scanner scn) throws SQLException {
        System.out.println("Function to show mutual funds sorted by prices on a date");
        System.out.println("------------------------------------------------------------------");

        System.out.println("What date would you like to check? Please make it in the format YYYY-MM-DD");
        String date = scn.nextLine();

        String fundPricesQuery = "SELECT symbol, price FROM CLOSING_PRICE WHERE p_date = TO_DATE( ? , 'YYYY-MM-DD') ORDER BY price DESC";
        PreparedStatement fundPricesPs = conn.prepareStatement(fundPricesQuery);
        fundPricesPs.setString(1, date);

        //execute a query
        ResultSet balanceRes = fundPricesPs.executeQuery();

        //Assess
        String rPrice = null;
        String rName = "";
        while (balanceRes.next()) {
            rName = balanceRes.getString("symbol");
            rPrice = balanceRes.getString("price");
            System.out.println("Symbol: " + rName + "  Price: " + rPrice);
        }
        
        return;
    }

    //Assumptions: Show all MFNames, not just the ones for the user
    private static void showMFNames(Connection conn) throws SQLException {
        System.out.println("Function to show mutual funds sorted by names");
        System.out.println("------------------------------------------------------------------");

        //create a query
        String fundNamesQuery = "SELECT name FROM MUTUAL_FUND ORDER BY name ASC";
        PreparedStatement fundNamesPs = conn.prepareStatement(fundNamesQuery);

        //execute a query
        ResultSet fundNamesRes = fundNamesPs.executeQuery();

        System.out.println("Here are all of the fund names: \n");
        while (fundNamesRes.next()) {
            //Print names
            System.out.println(fundNamesRes.getString("name"));
        }

        return;
    }

    //Assumptions: Total shares means the total of all shares
    private static void showBalance(String userName, Connection conn) throws SQLException {
        System.out.println("Function to show customer balance and total number of shares");
        System.out.println("------------------------------------------------------------------");

        //create a query
        String balanceQuery = "SELECT name, balance FROM CUSTOMER WHERE login=?";
        PreparedStatement balancePs = conn.prepareStatement(balanceQuery);
        balancePs.setString(1, userName);

        //execute a query
        ResultSet balanceRes = balancePs.executeQuery();

        //Assess
        String rBalance = null;
        String rName = "";
        while (balanceRes.next()) {
            rName = balanceRes.getString("name");
            rBalance = balanceRes.getString("balance");
        }

        //create a query
        String sharesQuery = "SELECT shares FROM owns WHERE login=?";
        PreparedStatement sharesPs = conn.prepareStatement(sharesQuery);
        sharesPs.setString(1, userName);

        //execute a query
        ResultSet sharesRes = sharesPs.executeQuery();

        //Assumption: Total number of shares means the total number of all shares
        int rShares = 0;
        while (sharesRes.next()) {
            rShares += Integer.parseInt(sharesRes.getString("shares"));
        }

        System.out.println(rName + " has a total balance of: " + rBalance + " and has this many shares total: " + rShares);

        return;
    }

    private static void updateDate(Connection conn, Scanner scn) throws SQLException {
        System.out.println("Function to update the current date");
        System.out.println("------------------------------------------------------------------");
        System.out.println("What date would you like to set p_date to? Please use the format (YYYY-MM-DD)");
        String date = scn.nextLine();

        PreparedStatement upDate = conn.prepareStatement("UPDATE MUTUAL_DATE SET p_date = TO_DATE( ? , 'YYYY-MM-DD') ");
        upDate.setString(1, date);
        upDate.execute();

        return;
    }

    private static void rankInvestors(Connection conn, Scanner scan) throws SQLException {
        System.out.println("Function to rank all the investors");
        System.out.println("------------------------------------------------------------------");

        PreparedStatement rankInvestors = conn.prepareStatement("select * from investor_rank");
        ResultSet rankRes = rankInvestors.executeQuery();

        //Assumption: Total number of shares means the total number of all shares
        String investor = "";
        String total_cash = "";

        System.out.println("Here's the value of all of the investors");
        while (rankRes.next()) {
            investor = rankRes.getString("login");
            total_cash = rankRes.getString("sum");
            System.out.println(investor + " "+ total_cash);
        }

        System.out.println();
        return;
    }

    private static void topK(Connection conn, Scanner scan) throws SQLException {
        System.out.println("Function to show top-k highest volume categories");
        System.out.println("------------------------------------------------------------------");
        System.out.println("How many categories would you like to see?");
        int k = Integer.parseInt(scan.nextLine());
        String kQuery = "SELECT category, sum(shares) as shares FROM mutual_fund JOIN owns o on mutual_fund.symbol = o.symbol GROUP BY category ORDER BY sum(shares) DESC FETCH FIRST ? ROWS ONLY";
        
        PreparedStatement kPs = conn.prepareStatement(kQuery);
        kPs.setInt(1, k);

        //execute a query
        ResultSet kRes = kPs.executeQuery();

        //Assumption: Total number of shares means the total number of all shares
        String cat = "";
        String number = "";
        while (kRes.next()) {
            cat = kRes.getString("category");
            number = kRes.getString("shares");
            System.out.println(cat + " "+ number);
        }

        return;
    }

    private static void updateShares(Connection conn, Scanner scan) throws Exception {
        System.out.println("Function to update share quotes for a day");
        System.out.println("------------------------------------------------------------------");
        System.out.println("Please enter the date when the mutual fund was created in the format <YYYY-MM-DD>");
        //Think we need to use pseudo date for this instead of having them input it2
        /*I think we have to format this date input to work with the query but not sure*/
        String cDate = scan.nextLine();
        if(cDate.length() > 30) {
            System.out.println("Sorry that is too long");
            return;
        }
        System.out.println("Please enter the file name where the mutual fund symbol and prices are located");
        String fileName = scan.nextLine();
        Scanner fread = new Scanner(new File(fileName));
        while(fread.hasNextLine()) {
            String line = fread.nextLine();
            String[] lineSplit = line.split(",");
            String sym = lineSplit[0];
            if(sym.length() > 20) {
                System.out.println("Sorry that is too long");
                return;
            }
            float price = Float.parseFloat(lineSplit[1]);
            String addClosingPrice = "INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES (?, ?,  TO_DATE( ? , 'YYYY-MM-DD') )";
            PreparedStatement cpStat = conn.prepareStatement(addClosingPrice);
            cpStat.setString(1, sym);
            cpStat.setFloat(2, price);
            cpStat.setString(3, cDate);
            cpStat.execute();
        }
        return;
    }

    private static void addMutualFund(Connection conn, Scanner scan) throws SQLException {
        System.out.println("Function to add a mutual fund");
        System.out.println("------------------------------------------------------------------");
        //Get info
        System.out.println("Please enter the symbol for the mutual fund");
        String sym = scan.nextLine();
        if(sym.length() > 20) {
            System.out.println("Sorry that is too long");
            return;
        }
        System.out.println("Please enter the column name for the mutual fund");
        String cName = scan.nextLine();
        System.out.println("Please enter the name of the mutual fund");
        String name = scan.nextLine();
        if(name.length() > 30) {
            System.out.println("Sorry that is too long");
            return;
        }
        System.out.println("Please enter the description of the mutual fund");
        String desc = scan.nextLine();
        if(desc.length() > 30) {
            System.out.println("Sorry that is too long");
            return;
        }
        System.out.println("Please enter the category of the mutual fund");
        String cat = scan.nextLine();
        if(cat.length() > 10) {
            System.out.println("Sorry that is too long");
            return;
        }
        System.out.println("Please enter the date when the mutual fund was created in the format <YYYY-MM-DD>");
        /*I think we have to format this date input to work with the query but not sure*/
        String cDate = scan.nextLine();
        if(cDate.length() > 30) {
            System.out.println("Sorry that is too long");
            return;
        }

        //Insert into Mutual Fund table
        String addMF = "INSERT INTO MUTUAL_FUND (symbol,name,description,category,c_date) VALUES (?, ?, ?, ?, TO_DATE( ? , 'YYYY-MM-DD'))";
        PreparedStatement pStat = conn.prepareStatement(addMF);
        pStat.setString(1, sym);
        pStat.setString(2, name);
        pStat.setString(3, desc);
        pStat.setString(4, cat);
        pStat.setString(5, cDate);
        pStat.execute();

        //Since new mutual fund add to the closing price table?? - This is handled by a trigger
        /*
        float price = 0;
        String addClosingPrice = "INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES (?, ?, ?)";
        PreparedStatement cpStat = conn.prepareStatement(addClosingPrice);
        cpStat.setString(1, sym);
        cpStat.setFloat(2, price);
        cpStat.setString(3, cDate);
        cpStat.execute();'''*/

        return;
    }

    private static void addCustomer(Connection conn, Scanner scan) throws SQLException {
        System.out.println("Function to add a customer");
        System.out.println("------------------------------------------------------------------");
        //Get info and check if too long
        System.out.println("Please enter the login for the new user");
        String login = scan.nextLine();
        if(login.length() > 10){
            System.out.println("Sorry that is too long");
            return;
        }
        System.out.println("Please enter the name for the new user");
        String name = scan.nextLine();
        if(name.length() > 20){
            System.out.println("Sorry that is too long");
            return;
        }
        System.out.println("Please enter the email for the new user");
        String email = scan.nextLine();
        if(email.length() > 30){
            System.out.println("Sorry that is too long");
            return;
        }
        System.out.println("Please enter the password for the new user");
        String password = scan.nextLine();
        if(password.length() > 10){
            System.out.println("Sorry that is too long");
            return;
        }
        System.out.println("Please enter the address for the new user");
        String address = scan.nextLine();
        if(address.length() > 30){
            System.out.println("Sorry that is too long");
            return;
        }

        System.out.println("Would you like to set an initial balance? (y/n)");
        Boolean setBalance = (scan.nextLine().equals("y"));
        if(setBalance){
            System.out.println("How much would you like to initialize the balance to?");
            float balance = Float.parseFloat(scan.nextLine());
            String addCust = "INSERT INTO CUSTOMER (login,name,email,address,password,balance) VALUES (?, ?, ?, ?, ?, ?);";
            PreparedStatement custPs = conn.prepareStatement(addCust);
            custPs.setString(1, login);
            custPs.setString(2, name);
            custPs.setString(3, email);
            custPs.setString(4, address);
            custPs.setString(5, password);
            custPs.setFloat(6, balance);
            custPs.execute();
        }
        else{
            String addCust = "INSERT INTO CUSTOMER (login,name,email,address,password) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement custPs = conn.prepareStatement(addCust);
            custPs.setString(1, login);
            custPs.setString(2, name);
            custPs.setString(3, email);
            custPs.setString(4, address);
            custPs.setString(5, password);
            custPs.execute();
        }
        
        
        return;
    }

    private static void eraseDatabase(Connection conn, Scanner scan) throws SQLException {
        System.out.println("Function to erase the database");
        System.out.println("------------------------------------------------------------------");
        System.out.println("Are you sure? This cannot be undone (y/n)");
        Boolean sure = (scan.nextLine().equals("y"));
        if(!sure)
            return;

        String truncate = "TRUNCATE ADMINISTRATOR CASCADE; TRUNCATE ALLOCATION CASCADE; TRUNCATE CLOSING_PRICE CASCADE; TRUNCATE CUSTOMER CASCADE; TRUNCATE MUTUAL_DATE CASCADE; TRUNCATE MUTUAL_FUND CASCADE; TRUNCATE OWNS CASCADE; TRUNCATE PREFERS CASCADE; TRUNCATE TRXLOG CASCADE;";
        PreparedStatement truncPs = conn.prepareStatement(truncate);
        truncPs.execute();

        return;
    }

    public static void printAdminMenu() {
        System.out.println("What would you like to do? (Please Enter the Number)");
        System.out.println("(1) Erase the database");
        System.out.println("(2) Add a Customer");
        System.out.println("(3) Add a Mutual Fund");
        System.out.println("(4) Update Share Quotes for a Day");
        System.out.println("(5) Show the Top-K Highest Volume Categories");
        System.out.println("(6) Rank all the investors");
        System.out.println("(7) Update the Current Date");
        System.out.println("(8) Go Back");
        System.out.println("(9) Quit");
        return;
    }

    public static void printCustomerMenu() {
        System.out.println("What would you like to do? (Please Enter the Number)");
        System.out.println("(1) Show Balance and Total Number of Shares");
        System.out.println("(2) Show Mutual Funds Sorted by Name");
        System.out.println("(3) Show Mutual Funds Sorted by Prices on a Date");
        System.out.println("(4) Search for a Mutual Fund");
        System.out.println("(5) Deposit an Amount for Investment");
        System.out.println("(6) Buy Shares");
        System.out.println("(7) Sell Shares");
        System.out.println("(8) Show ROI");
        System.out.println("(9) Predict Gain or Loss of Transaction");
        System.out.println("(10) Change Allocation Preference");
        System.out.println("(11) Rank Customer's Allocation");
        System.out.println("(12) Show Portfolio");
        System.out.println("(13) Go Back");
        System.out.println("(14) Quit");
        return;
    }
}