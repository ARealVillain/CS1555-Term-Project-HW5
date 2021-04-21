import java.util.*;
import java.sql.*;

public class teamTenProj {
    public static void main(String args[]) {
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
                while(custFlag) {
                    //might have to ask for customer information here since it is not asked for in functions but needed for them
                    printCustomerMenu();
                    System.out.println("------------------------------------------------------------------");
                    userOp = scan.nextLine();
                    System.out.println("------------------------------------------------------------------");
                    if(userOp.equals("1")) {
                        showBalance();
                    } else if(userOp.equals("2")) {
                        showMFNames();
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

    private static void showMFNames() {
        System.out.println("Function to show mutual funds sorted by names");
        System.out.println("------------------------------------------------------------------");
        return;
    }

    private static void showBalance() {
        System.out.println("Function to show customer balance and total number of shares");
        System.out.println("------------------------------------------------------------------");
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