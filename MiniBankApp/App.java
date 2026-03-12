package Projects.MiniBankApp;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        BankManager manager = new BankManager();
        Scanner sc = new Scanner(System.in);

        System.out.println("-- WELCOME TO MINI BANK APP --");
        
        while (true) {
            System.out.println("-----------------------------------------");
            System.out.println("1. Create New Account");
            System.out.println("2. Login to Account");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    System.out.println("\n--- Create Account ---");
                    System.out.print("Enter Full Name: ");
                    String name = sc.nextLine();
                    System.out.print("Set 4-Digit PIN: ");
                    String pin = sc.nextLine();
                    
                    manager.createAccount(name, pin);
                    break;

                case 2:
                    System.out.println("\n--- Login ---");
                    System.out.print("Enter Name: ");
                    String loginName = sc.nextLine();
                    System.out.print("Enter PIN: ");
                    String loginPin = sc.nextLine();
                    
                    manager.loginAccount(loginName, loginPin);
                    break;

                case 3:
                    System.out.println("Thank you for using Mini Bank. Goodbye!");
                    System.exit(0);

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}

