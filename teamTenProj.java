import java.util.*;
import java.sql.*;

public class teamTenProj {
    public static void main(String args[]) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Are you an admin or a customer?");
        System.out.println("Please type in <admin> or <customer> or <quit> to exit the program");
        System.out.println("------------------------------------------------------------------");
        String userType;
        userType = scan.nextLine();
        while(!userType.equals("quit")) {
            System.out.println("------------------------------------------------------------------");
            if(userType.equals("admin")) {
                printAdminMenu();
                System.out.println("------------------------------------------------------------------");
            } else if(userType.equals("customer")) {
                printCustomerMenu();
                System.out.println("------------------------------------------------------------------");
            } else {
                System.out.println("Invalid Input");
                System.out.println("------------------------------------------------------------------");
            }
            System.out.println("Are you an admin or a customer?");
            System.out.println("Please type in <admin> or <customer> or <quit> to exit the program");
            System.out.println("------------------------------------------------------------------");
            userType = scan.nextLine();
            System.out.println("------------------------------------------------------------------");
        }
        System.out.println("Goodbye");
        scan.close();
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
        System.out.println("(13) Quit");
        return;
    }
}