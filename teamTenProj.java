import java.util.*;
import java.sql.*;

public class teamTenProj {
    public static void main(String args[]) throws SQLException, ClassNotFoundException {

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
                    
                    if (rid == null){
                        System.out.println("\nSorry thats a bad login!\n");
                    }
                    else{
                        loggedIn = true;
                    }
                }


                boolean adminFlag = true;
                while(adminFlag) {
                    printAdminMenu();
                    System.out.println("------------------------------------------------------------------");
                    userOp = scan.nextLine();
                    System.out.println("------------------------------------------------------------------");
                    if(userOp.equals("1")) {
                        eraseDatabase();
                    } else if(userOp.equals("2")) {
                        addCustomer();
                    } else if(userOp.equals("3")) {
                        addMutualFund();
                    } else if(userOp.equals("4")) {
                        updateShares();
                    } else if(userOp.equals("5")) {
                        topK();
                    } else if(userOp.equals("6")) {
                        rankInvestors();
                    } else if(userOp.equals("7")) {
                        updateDate();
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
                    
                    if (rid == null){
                        System.out.println("\nSorry thats a bad login!\n");
                    }
                    else{
                        loggedIn = true;
                    }
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
                        showMFPrices();
                    } else if(userOp.equals("4")) {
                        searchMutualFund();
                    } else if(userOp.equals("5")) {
                        depositAmount();
                    } else if(userOp.equals("6")) {
                        buyShares();
                    } else if(userOp.equals("7")) {
                        sellShares();
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

    private static void sellShares() {
        System.out.println("Function to sell shares");
        System.out.println("------------------------------------------------------------------");
        return;
    }

    private static void buyShares() {
        System.out.println("Function to buy shares");
        System.out.println("------------------------------------------------------------------");
        return;
    }

    private static void depositAmount() {
        System.out.println("Function to deposit amount for investment");
        System.out.println("------------------------------------------------------------------");
        return;
    }

    private static void searchMutualFund() {
        System.out.println("Function to search for a mutual fund");
        System.out.println("------------------------------------------------------------------");

        return;
    }

    private static void showMFPrices() {
        System.out.println("Function to show mutual funds sorted by prices on a date");
        System.out.println("------------------------------------------------------------------");
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

    private static void updateDate() {
        System.out.println("Function to update the current date");
        System.out.println("------------------------------------------------------------------");
        return;
    }

    private static void rankInvestors() {
        System.out.println("Function to rank all the investors");
        System.out.println("------------------------------------------------------------------");
        return;
    }

    private static void topK() {
        System.out.println("Function to show top-k highest volume categories");
        System.out.println("------------------------------------------------------------------");
        return;
    }

    private static void updateShares() {
        System.out.println("Function to update share quotes for a day");
        System.out.println("------------------------------------------------------------------");
        return;
    }

    private static void addMutualFund() {
        System.out.println("Function to add a mutual fund");
        System.out.println("------------------------------------------------------------------");
        return;
    }

    private static void addCustomer() {
        System.out.println("Function to add a customer");
        System.out.println("------------------------------------------------------------------");
        return;
    }

    private static void eraseDatabase() {
        System.out.println("Function to erase the database");
        System.out.println("------------------------------------------------------------------");
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