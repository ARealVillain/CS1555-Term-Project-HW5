import java.util.*;

import javax.print.attribute.standard.PresentationDirection;
import javax.swing.tree.ExpandVetoException;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.lang.*;
import java.time.LocalDateTime;

public class teamTenProj {
    public static void main(String args[]) throws Exception {

        //Set up connection with SQL
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://clas3.cs.pitt.edu:5432/kkp19";
        Properties props = new Properties();
        props.setProperty("user", "kkp19");
        props.setProperty("password", "");
        Connection conn = DriverManager.getConnection(url, props);


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
                boolean adminFlag = true;
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
                    try{
                        ResultSet res1 = loginPs.executeQuery();
                        //Assess
                        String rid = null;
                        while (res1.next()) {
                            rid = res1.getString("login");
                        }
                        
                        if (rid == null){
                            System.out.println("\nSorry thats a bad login!\n");
                            adminFlag = false;
                            loggedIn = true;
                        }
                        else
                            loggedIn = true;

                    }catch(Exception e){
                    }
                }


                
                while(adminFlag) {
                    printAdminMenu();
                    System.out.println("------------------------------------------------------------------");
                    userOp = scan.nextLine();
                    System.out.println("------------------------------------------------------------------");
                    if(userOp.equals("1")) {
                        try {
                            eraseDatabase(conn, scan);
                        } catch (Exception e) {
                            System.out.println("Erase Database Failed");
                            System.out.println(e.getMessage());
                            System.out.println("------------------------------------------------------------------");
                        }
                    } else if(userOp.equals("2")) {

                        try {
                            addCustomer(conn, scan);
                        } catch (Exception e) {
                            System.out.println("Add Customer Failed");
                            System.out.println(e.getMessage());
                            System.out.println("------------------------------------------------------------------");
                        }
                    } else if(userOp.equals("3")) {
                        try {
                            addMutualFund(conn, scan);
                        } catch (Exception e) {
                            System.out.println("Add Mutual Fund Failed");
                            System.out.println(e.getMessage());
                            System.out.println("------------------------------------------------------------------");
                        }
                    } else if(userOp.equals("4")) {
                        try {
                            updateShares(conn, scan);
                        } catch (Exception e) {
                            System.out.println("Update Shares Failed");
                            System.out.println(e.getMessage());
                            System.out.println("------------------------------------------------------------------");
                        }
                    } else if(userOp.equals("5")) {
                        try {
                            topK(conn, scan);
                        } catch (Exception e) {
                            System.out.println("Show Top-K Categories Failed");
                            System.out.println(e.getMessage());
                            System.out.println("------------------------------------------------------------------");
                        }
                    } else if(userOp.equals("6")) {
                        try {
                            rankInvestors(conn, scan);
                        } catch (Exception e) {
                            System.out.println("Rank Investors Failed");
                            System.out.println(e.getMessage());
                            System.out.println("------------------------------------------------------------------");
                        }
                    } else if(userOp.equals("7")) {
                        try {
                            updateDate(conn, scan);
                        } catch (Exception e) {
                            System.out.println("Update Date Failed");
                            System.out.println(e.getMessage());
                            System.out.println("------------------------------------------------------------------");
                        }
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
                    try{
                        //execute a query
                        ResultSet res1 = loginPs.executeQuery();

                        //Assess
                        String rid = null;
                        while (res1.next()) {
                            rid = res1.getString("login");
                        }
                        
                        if (rid == null){
                            System.out.println("\nSorry thats a bad login!\n");
                            loggedIn = true;
                            custFlag = false;
                        }
                        else
                            loggedIn = true;
                    }catch(Exception e){}
                }

                while(custFlag) {
                    //might have to ask for customer information here since it is not asked for in functions but needed for them
                    printCustomerMenu();
                    System.out.println("------------------------------------------------------------------");
                    userOp = scan.nextLine();
                    System.out.println("------------------------------------------------------------------");
                    if(userOp.equals("1")) {
                        try {
                            showBalance(userName, conn);
                        } catch (Exception e) {
                            System.out.println("Show Balance Failed");
                            System.out.println(e.getMessage());
                            System.out.println("------------------------------------------------------------------");
                        }
                    } else if(userOp.equals("2")) {
                        try {
                            showMFNames(conn);
                        } catch (Exception e) {
                            System.out.println("Show Mutual Funds by Name Failed");
                            System.out.println(e.getMessage());
                            System.out.println("------------------------------------------------------------------");
                        }
                    } else if(userOp.equals("3")) {
                        try {
                            showMFPrices(conn, scan);
                        } catch (Exception e) {
                            System.out.println("Show Mutual Funds by Price Failed");
                            System.out.println(e.getMessage());
                            System.out.println("------------------------------------------------------------------");
                        }
                    } else if(userOp.equals("4")) {
                        try {
                            searchMutualFund(conn, scan);
                        } catch (Exception e) {
                            System.out.println("Search Mutual Fund Failed");
                            System.out.println(e.getMessage());
                            System.out.println("------------------------------------------------------------------");
                        }
                    } else if(userOp.equals("5")) {
                        try {
                            depositAmount(userName, conn, scan);
                        } catch (Exception e) {
                            System.out.println("Deposit Amount Failed");
                            System.out.println(e.getMessage());
                            System.out.println("------------------------------------------------------------------");
                        }
                    } else if(userOp.equals("6")) {
                        try {
                            buyShares(userName, conn, scan);
                        } catch (Exception e) {
                            System.out.println("Buy Shares Failed");
                            System.out.println(e.getMessage());
                            System.out.println("------------------------------------------------------------------");
                        }
                    } else if(userOp.equals("7")) {
                        try {
                            sellShares(userName, conn, scan);
                        } catch (Exception e) {
                            System.out.println("Sell Shares Failed");
                            System.out.println(e.getMessage());
                            System.out.println("------------------------------------------------------------------");
                        }
                    } else if(userOp.equals("8")) {
                        try {
                            showROI(userName, conn, scan);
                        } catch (Exception e) {
                            System.out.println("Show ROI Failed");
                            System.out.println(e.getMessage());
                            System.out.println("------------------------------------------------------------------");
                        }
                    } else if(userOp.equals("9")) {
                        try {
                            predict(userName, conn, scan);
                        } catch (Exception e) {
                            System.out.println("Prediction Failed");
                            System.out.println(e.getMessage());
                            System.out.println("------------------------------------------------------------------");
                        }
                    } else if(userOp.equals("10")) {
                        try {
                            changePreference(userName, conn, scan);
                        } catch (Exception e) {
                            System.out.println("Change Preference Failed");
                            System.out.println(e.getMessage());
                            System.out.println("------------------------------------------------------------------");
                        }
                    } else if(userOp.equals("11")) {
                        try {
                            rankAllocations(userName, conn, scan);
                        } catch (Exception e) {
                            System.out.println("Rank Allocation Failed");
                            System.out.println(e.getMessage());
                            System.out.println("------------------------------------------------------------------");
                        }
                    } else if(userOp.equals("12")) {
                        try {
                            showPortfolio(conn, scan, userName);
                        } catch (Exception e) {
                            System.out.println("Show Portfolio Failed");
                            System.out.println(e.getMessage());
                            System.out.println("------------------------------------------------------------------");
                        }
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

    private static void showPortfolio(Connection conn, Scanner scn, String userName) throws SQLException {
        System.out.println("Function to show customer portfolio");
        System.out.println("------------------------------------------------------------------");
        //create a query
        String portfolioQuery = "select * from get_portfolio(?)";
        PreparedStatement portfolioPs = conn.prepareStatement(portfolioQuery);
        portfolioPs.setString(1, userName);

        //execute a query
        ResultSet portfolioRes = portfolioPs.executeQuery();
        float total_val = 0;
        System.out.println("Symbol, Shares Owned, Value of Shares, Cost of Shares, Adjusted Cost of Shares, Total Yield from Shares");
        while (portfolioRes.next()) {
            //Print names
            System.out.print(portfolioRes.getString("mf_symb"));
            System.out.print(" ");
            System.out.print(portfolioRes.getString("mf_shares_owned"));
            String mf_val = portfolioRes.getString("mf_val");
            System.out.print(" ");
            System.out.print(portfolioRes.getString("mf_val"));
            System.out.print(" ");
            System.out.print(portfolioRes.getString("mf_cost"));
            System.out.print(" ");
            System.out.print(portfolioRes.getString("mf_adj_cost"));
            System.out.print(" ");
            System.out.print(portfolioRes.getString("mf_yield"));
            System.out.print(" ");
            total_val += Float.parseFloat(mf_val);
            System.out.println(" ");
        }

        String balanceQuery = "select balance from customer where login = ?";
        PreparedStatement balancePs = conn.prepareStatement(balanceQuery);
        balancePs.setString(1, userName);

        //execute a query
        ResultSet balanceRes = balancePs.executeQuery();
        balanceRes.next();
        total_val += Float.parseFloat(balanceRes.getString("balance"));
        System.out.println("Here is the total value of your mutual fund today: " + Float.toString(total_val) + "\n");
        return;
    }

    private static void rankAllocations(String userName, Connection conn, Scanner scan) throws SQLException {
        System.out.println("Function to rank customer allocations");
        System.out.println("------------------------------------------------------------------");
        HashMap<String, Double> map = new HashMap<String, Double>();

        String createQuery = "CREATE TABLE RANK_ALLOCATION(allocationNUM int, weightedROI decimal(10, 2));";
        PreparedStatement create = conn.prepareStatement(createQuery);
        create.execute();
        
        String joinQuery = "SELECT * FROM ALLOCATION JOIN PREFERS ON ALLOCATION.allocation_no = PREFERS.allocation_no WHERE login=?";
        PreparedStatement joinPs = conn.prepareStatement(joinQuery);
        joinPs.setString(1, userName);
        ResultSet joinRES = joinPs.executeQuery();
        String query = "SELECT symbol,shares FROM OWNS WHERE login=?";
        PreparedStatement roiPS = conn.prepareStatement(query);
        roiPS.setString(1, userName);
        ResultSet res = roiPS.executeQuery();
        while(res.next()) {
            String sym = res.getString("symbol");
            int shares = res.getInt("shares");
            int sharesCalc = 0;
            Double totalCost = 0.0;
            String mfQuery = "SELECT name FROM MUTUAL_FUND WHERE symbol=?";
            PreparedStatement mfPs = conn.prepareStatement(mfQuery);
            mfPs.setString(1, sym);
            ResultSet mfRes = mfPs.executeQuery();
            mfRes.next();
            String name = mfRes.getString("name");
            String priceQuery = "SELECT price FROM CLOSING_PRICE WHERE symbol=? ORDER BY p_date DESC LIMIT 1";
            PreparedStatement pricePs = conn.prepareStatement(priceQuery);
            pricePs.setString(1, sym);
            ResultSet priceRes = pricePs.executeQuery();
            priceRes.next();
            Double curPrice = Double.parseDouble(priceRes.getString("price"));
            String trxlogQuery = "SELECT symbol,action,num_shares,price FROM TRXLOG WHERE login=? AND symbol=? AND action='buy' ORDER BY t_date DESC";
            PreparedStatement trxlogPs = conn.prepareStatement(trxlogQuery);
            trxlogPs.setString(1, userName);
            trxlogPs.setString(2, sym);
            ResultSet trxRes = trxlogPs.executeQuery();
            while(trxRes.next()) {
                int tShares = trxRes.getInt("num_shares");
                Double price = trxRes.getDouble("price");
                if(sharesCalc < shares) {
                    sharesCalc += tShares;
                    totalCost += (price * tShares);
                }
            }
            Double currValue = curPrice * shares;
            Double roi = (currValue - totalCost) / totalCost;
            map.put(sym, roi);
        }
        String insertQuery = "INSERT INTO RANK_ALLOCATION (allocationNUM, weightedROI) VALUES (?,?)";
        PreparedStatement insert = conn.prepareStatement(insertQuery);
        while(joinRES.next()) {
            int alNUM = joinRES.getInt("allocation_no");
            String sym = joinRES.getString("symbol");
            Double percent = joinRES.getDouble("percentage");
            if(map.containsKey(sym)) {
                Double roi = map.get(sym);
                Double wROI = roi * percent;
                insert.setInt(1, alNUM);
                insert.setDouble(2, wROI);
                insert.execute();
            } else {
                Double wROI = 0.0;
                insert.setInt(1, alNUM);
                insert.setDouble(2, wROI);
                insert.execute();
            }
        }
        String rankQuery = "SELECT allocationNUM, sum(weightedROI) FROM RANK_ALLOCATION GROUP BY allocationNUM ORDER BY sum(weightedROI) desc;";
        PreparedStatement rankPs = conn.prepareStatement(rankQuery);
        ResultSet rankRES = rankPs.executeQuery();
        System.out.println("Here is the ranked list of allocations:");
        while(rankRES.next()) {
            System.out.println(String.valueOf(rankRES.getInt("allocationNUM")));
        }

        String dropQuery = "DROP TABLE IF EXISTS RANK_ALLOCATION;";
        PreparedStatement drop = conn.prepareStatement(dropQuery);
        drop.execute();

        return;
    }

    private static void changePreference(String userName, Connection conn, Scanner scn) throws SQLException{
        System.out.println("Function to change customer preference");
        System.out.println("------------------------------------------------------------------");
        double percent = 0;
        boolean notDone=true;
        
        //testing if the date is valid
        String dateQuery = "SELECT p_date FROM ALLOCATION WHERE login=? ORDER BY p_date DESC LIMIT 1";
        PreparedStatement datePs = conn.prepareStatement(dateQuery);
        datePs.setString(1, userName);
        ResultSet dateRes = datePs.executeQuery();
        
        // getting the mutual_date
        String mDateQuery = "SELECT p_date FROM MUTUAL_DATE";
        PreparedStatement mDatePs = conn.prepareStatement(mDateQuery);
        ResultSet mDateRes = mDatePs.executeQuery();
        mDateRes.next();
        LocalDateTime now = mDateRes.getTimestamp("p_date").toLocalDateTime();
        if(dateRes.next()) {
           LocalDateTime time = dateRes.getTimestamp("p_date").toLocalDateTime();
   
           if(now.getYear() == time.getYear() && now.getDayOfYear() == time.getDayOfYear()) {
               System.out.println("You already made an allocation today, try again tomorrow");
               System.out.println();
               return;
           }  
        }
        
        System.out.println("if you do not want to change your allocation preferences type -1 now otherwise type anything to continue");
        if(scn.nextLine().equals("-1")) {
            return;
        }
        int allocationNum;
        String noQuery = "SELECT allocation_no FROM ALLOCATION ORDER BY allocation_no DESC LIMIT 1";
        PreparedStatement noPs = conn.prepareStatement(noQuery);
        ResultSet noRes = noPs.executeQuery();
        if(noRes.next()) {
            allocationNum=noRes.getInt("allocation_no")+1;
        }
        else {
            allocationNum=0;
        }
        
        
        String allocationIn = "INSERT INTO ALLOCATION (allocation_no,login,p_date) VALUES (?,?,?);";
        PreparedStatement allocationPs = conn.prepareStatement(allocationIn);
        allocationPs.setInt(1, allocationNum);
        allocationPs.setString(2, userName);
        allocationPs.setTimestamp(3, Timestamp.valueOf(now));
        allocationPs.executeUpdate();
        
        while(notDone==true) {
            System.out.println("currently allocated percent " + percent);
            System.out.println("Enter the symbol of a mutual fund you would like to allocate");
            String symbol = scn.nextLine();
            System.out.println("enter the percent you would like to allocate. ex 0.154 would allocate 15.4%");
            double allocationVal=scn.nextDouble();
            scn.nextLine();
            if(percent + allocationVal > 1) {
                System.out.println("The percent of allocated funds exceeded 100% please try again");
            }
            else{
                percent=percent+allocationVal;
                String preferenceIn = "INSERT INTO PREFERS (allocation_no,symbol,percentage) VALUES (?,?,?);";
                PreparedStatement preferencePs = conn.prepareStatement(preferenceIn);
                preferencePs.setInt(1, allocationNum);
                preferencePs.setString(2, symbol);
                preferencePs.setDouble(3, allocationVal);
                preferencePs.executeUpdate();
                if(percent==1) {
                    System.out.println("allocation complete");
                    notDone=false;
                }
            } 
        }
        
        return;
    }

    private static void predict(String userName, Connection conn, Scanner scn) throws SQLException{
        System.out.println("Function to predict gains or losses of the customer transactions");
        System.out.println("------------------------------------------------------------------");
        //create a query
        String trxlogQuery = "SELECT symbol,action,num_shares,price FROM TRXLOG WHERE login=?";
        PreparedStatement trxlogPs = conn.prepareStatement(trxlogQuery);
        trxlogPs.setString(1, userName);

        //execute a query
        ResultSet trxlogRes = trxlogPs.executeQuery();

        //Assess
        String rSymbol = "";
        String rAction = "";
        int rShares = 0;
        double rPrice = 0;
        double curPrice;
        
        while (trxlogRes.next()) {
            if( trxlogRes.getString("action").equals("deposit")) {continue;}
            rSymbol = trxlogRes.getString("symbol");
            rAction = trxlogRes.getString("action");
            rShares = trxlogRes.getInt("num_shares");
            rPrice = Double.parseDouble(trxlogRes.getString("price"));
            
            double value = rPrice * (double)rShares;
            
            String priceQuery = "SELECT price FROM CLOSING_PRICE WHERE symbol=? ORDER BY p_date DESC LIMIT 1";
            PreparedStatement pricePs = conn.prepareStatement(priceQuery);
            pricePs.setString(1, rSymbol);
            ResultSet priceRes = pricePs.executeQuery();
            priceRes.next();
            curPrice=Double.parseDouble(priceRes.getString("price"));
            
            if(rAction.equals("buy")) {
                System.out.println("symbol:"+rSymbol);
                System.out.println("bought " + rShares + " shares");
                System.out.println("historical price per share:" + rPrice);
                System.out.println("current price per share:" + curPrice);
                if(curPrice>rPrice) {
                    double sum = curPrice * (double)rShares - value;
                    System.out.println("profit of " + sum);
                    System.out.println();
                }
                else if (curPrice<rPrice) {
                    double sum =  value-curPrice * (double)rShares;
                    System.out.println("loss of " + sum);
                    System.out.println();
                }
                else {
                    System.out.println("hold");
                }
                System.out.println();
            }
            else if(rAction.equals("sell")) {
                System.out.println("symbol:"+rSymbol);
                System.out.println("bought " + rShares + "shares");
                System.out.println("historical price per share:" + rPrice);
                System.out.println("current price per share:" + curPrice);
                if(curPrice>rPrice) {
                    double sum = curPrice * (double)rShares - value;
                    System.out.println("loss of " + sum);
                    System.out.println();
                }
                else if (curPrice<rPrice) {
                    double sum =  value-curPrice * (double)rShares;
                    System.out.println("profit of " + sum);
                    System.out.println();
                }
                else {
                    System.out.println("hold");
                }
                System.out.println();
            }
            
        }
        return;
    }

    private static void showROI(String userName, Connection conn, Scanner scn) throws SQLException {
        System.out.println("Function to calculate and show return of investment");
        System.out.println("------------------------------------------------------------------");
        String query = "SELECT symbol,shares FROM OWNS WHERE login=?";
        PreparedStatement roiPS = conn.prepareStatement(query);
        roiPS.setString(1, userName);
        ResultSet res = roiPS.executeQuery();
        while(res.next()) {
            String sym = res.getString("symbol");
            int shares = res.getInt("shares");
            int sharesCalc = 0;
            Double totalCost = 0.0;
            String mfQuery = "SELECT name FROM MUTUAL_FUND WHERE symbol=?";
            PreparedStatement mfPs = conn.prepareStatement(mfQuery);
            mfPs.setString(1, sym);
            ResultSet mfRes = mfPs.executeQuery();
            mfRes.next();
            String name = mfRes.getString("name");
            String priceQuery = "SELECT price FROM CLOSING_PRICE WHERE symbol=? ORDER BY p_date DESC LIMIT 1";
            PreparedStatement pricePs = conn.prepareStatement(priceQuery);
            pricePs.setString(1, sym);
            ResultSet priceRes = pricePs.executeQuery();
            priceRes.next();
            Double curPrice = Double.parseDouble(priceRes.getString("price"));
            String trxlogQuery = "SELECT symbol,action,num_shares,price FROM TRXLOG WHERE login=? AND symbol=? AND action='buy' ORDER BY t_date DESC";
            PreparedStatement trxlogPs = conn.prepareStatement(trxlogQuery);
            trxlogPs.setString(1, userName);
            trxlogPs.setString(2, sym);
            ResultSet trxRes = trxlogPs.executeQuery();
            while(trxRes.next()) {
                int tShares = trxRes.getInt("num_shares");
                Double price = trxRes.getDouble("price");
                if(sharesCalc < shares) {
                    sharesCalc += tShares;
                    totalCost += (price * tShares);
                }
            }
            Double currValue = curPrice * shares;
            Double roi = (currValue - totalCost) / totalCost;
            System.out.println("For the mutual fund " + name +" with the symbol " + sym + " the ROI = " + roi);
        }
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

            System.out.println("What amount would you like to spend?");
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
            String addClosingPrice = "set transaction read write; INSERT INTO CLOSING_PRICE (symbol,price,p_date) VALUES (?, ?,  (select * from mutual_date) )";
            PreparedStatement cpStat = conn.prepareStatement(addClosingPrice);
            cpStat.setString(1, sym);
            cpStat.setFloat(2, price);
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
        

        //Insert into Mutual Fund table
        String addMF = "INSERT INTO MUTUAL_FUND (symbol,name,description,category,c_date) VALUES (?, ?, ?, ?, (select * from mutual_date))";
        PreparedStatement pStat = conn.prepareStatement(addMF);
        pStat.setString(1, sym);
        pStat.setString(2, name);
        pStat.setString(3, desc);
        pStat.setString(4, cat);
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

        System.out.println("Since you are the admin, we need to add you back into the system.");
        System.out.println("Please enter your login");
        String login = scan.nextLine();
        if(login.length() > 10){
            System.out.println("Sorry that is too long");
            return;
        }
        System.out.println("Please enter your name");
        String name = scan.nextLine();
        if(name.length() > 20){
            System.out.println("Sorry that is too long");
            return;
        }
        System.out.println("Please enter your email");
        String email = scan.nextLine();
        if(email.length() > 30){
            System.out.println("Sorry that is too long");
            return;
        }
        System.out.println("Please enter your password");
        String password = scan.nextLine();
        if(password.length() > 10){
            System.out.println("Sorry that is too long");
            return;
        }
        System.out.println("Please enter your address");
        String address = scan.nextLine();
        if(address.length() > 30){
            System.out.println("Sorry that is too long");
            return;
        }
        String addAdmin = "INSERT INTO ADMINISTRATOR (login,name,email,address,password) VALUES (?, ?, ?, ?, ?);";
        PreparedStatement adminPs = conn.prepareStatement(addAdmin);
        adminPs.setString(1, login);
        adminPs.setString(2, name);
        adminPs.setString(3, email);
        adminPs.setString(4, address);
        adminPs.setString(5, password);
        adminPs.execute();

        System.out.println("What would you like to initialize the date to? (Please use the format <YYYY-MM-DD>)");
        String newDate = scan.nextLine();
        String dateQuery = "INSERT INTO MUTUAL_DATE (p_date) VALUES (TO_DATE( ? , 'YYYY-MM-DD') );";
        PreparedStatement custPs = conn.prepareStatement(dateQuery);
        custPs.setString(1, newDate);
        custPs.execute();


        

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