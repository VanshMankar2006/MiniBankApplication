package Projects.MiniBankApp;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class BankManager {
    Account acc = new Account();
    private final Connection con = DBConnection.getConnection();
    private Scanner sc = new Scanner(System.in);

    // to create account
    public void createAccount(String userName ,String pin){
        if(userName==null || pin == null || pin.length()!=4){
            System.out.println("Field can't be empty,pin length must be 4");
            return ;
        }

        try{
            final String sql = "INSERT INTO BankAccounts (user_fullname, user_accountnumber, user_pin) VALUES (?, ?, ?)";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1,userName);
            st.setString(2,AccountUtility.generateAccountNumber());
            st.setString(3, pin);

            int rs = st.executeUpdate();
            if(rs > 0){
                System.out.println("Account created Sucessfully...");
            }

        }catch(Exception e){
            System.out.println("Fail to create Account...");
            e.getMessage();
        }
    }
    // to login account
    public void loginAccount(String name, String pass) {
        String sql = "SELECT * FROM BankAccounts WHERE user_fullname = ? AND user_pin = ?";
        
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, name);
            st.setString(2, pass);
            ResultSet rs = st.executeQuery();

            if (rs.next()) { 
                String currentAcNo = rs.getString("user_accountnumber");
                String fullName = rs.getString("user_fullname");
                
                boolean stayLoggedIn = true;
                System.out.println("\nHello, " + fullName + "! [A/c: " + currentAcNo + "]");
                while (stayLoggedIn) {
                    System.out.println("1) Credit Money\n2) Debit Money\n3) View Balance\n4) Transfer Money\n5) Logout");
                    System.out.print("Enter Choice: ");
                    int ch = sc.nextInt();
                    sc.nextLine();

                    switch (ch) {
                        case 1 -> {
                            System.out.print("Enter Amount: ");
                            BigDecimal amt = sc.nextBigDecimal();
                            creditAmt(currentAcNo, amt);
                        }
                        case 2 -> {
                            System.out.print("Enter Amount: ");
                            BigDecimal amt = sc.nextBigDecimal();
                            debitAmt(currentAcNo, amt);
                        }
                        case 3 -> getBalance(currentAcNo);
                        case 4 -> {
                            System.out.print("Enter Receiver A/c No: ");
                            String receiver = sc.nextLine();
                            System.out.print("Enter Amount: ");
                            BigDecimal amt = sc.nextBigDecimal();
                            transferMoney(currentAcNo, receiver, amt);
                        }
                        case 5 -> stayLoggedIn = false;
                        
                        default -> System.out.println("Invalid choice!");
                    }
                }
            } else {
                System.out.println("Invalid Login Credentials!,Create Account First...");
            }
        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
        }
    }
    // to get balance details
    public void getBalance(String accNo){
        String sql = "SELECT * FROM BankAccounts WHERE user_accountnumber = ?";

        try(PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, accNo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("\n-------------------------------------------------");
                System.out.printf("%-15s | %-10s\n", "Account No", "Balance");
                System.out.printf("%-15s |$%,.2f\n",
                        rs.getString("user_accountnumber"),
                        rs.getBigDecimal("user_balance"));
                System.out.println("-------------------------------------------------");
            }
            
        } catch (Exception e) {
            System.out.println("Error : failed to fetch balance "+e.getMessage());
        }

    }
    // adding amount in account
    public void creditAmt(String accNo,BigDecimal amt) throws SQLException{
            String credit = "UPDATE BankAccounts SET user_balance = user_balance + ? WHERE user_accountnumber = ?";
            PreparedStatement ps = con.prepareStatement(credit);
            ps.setBigDecimal(1, amt);
            ps.setString(2, accNo);
            int cnt = ps.executeUpdate();
            if(cnt > 0){
                System.out.println("Amount added Sucessfully..");
            }else{
                System.out.println("Error : amount not credit");
            }

    }
    // debiting money from acccount
    public void debitAmt(String accNo,BigDecimal amt) throws SQLException{
        BigDecimal currBalance = acc.getUserBalance();

        if( currBalance.subtract(amt).compareTo(BigDecimal.ZERO) > 0){
            String credit = "UPDATE BankAccounts SET user_balance = user_balance - ? WHERE user_accountnumber = ?";
            PreparedStatement ps = con.prepareStatement(credit);
            ps.setBigDecimal(1, amt);
            ps.setString(2, accNo);
            int cnt = ps.executeUpdate();
            if(cnt > 0){
                System.out.println("Amount added Sucessfully..");
            }else{
                System.out.println("Error : amount not credit");
            }

        }else{
            System.out.println("Not sufficient amt to debit..");
        }
            

    }
    // transfer money
    public boolean transferMoney(String sender_ac, String receiver_ac, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Amount must be greater than zero!");
            return false;
        }

        try {
            con.setAutoCommit(false); // Start Transaction

            // 1. Check Balance
            String checkSql = "SELECT user_balance FROM BankAccounts WHERE user_accountnumber = ?";
            PreparedStatement psCheck = con.prepareStatement(checkSql);
            psCheck.setString(1, sender_ac);
            ResultSet rs = psCheck.executeQuery();

            if (rs.next() && rs.getBigDecimal("user_balance").compareTo(amount) <= 0) {
                System.out.println("Insufficient Balance!");
                con.rollback();
                return false;
            }

            // 2. Debit
            String debit = "UPDATE BankAccounts SET user_balance = user_balance - ? WHERE user_accountnumber = ?";
            PreparedStatement psDebit = con.prepareStatement(debit);
            psDebit.setBigDecimal(1, amount);
            psDebit.setString(2, sender_ac);
            int dCount = psDebit.executeUpdate();

            // 3. Credit
            String credit = "UPDATE BankAccounts SET user_balance = user_balance + ? WHERE user_accountnumber = ?";
            PreparedStatement psCredit = con.prepareStatement(credit);
            psCredit.setBigDecimal(1, amount);
            psCredit.setString(2, receiver_ac);
            int cCount = psCredit.executeUpdate();

            if (dCount > 0 && cCount > 0) {
                con.commit();
                System.out.println("Transaction Successful!");
                return true;
            } else {
                System.out.println("Receiver account not found!");
                con.rollback();
            }
        } catch (Exception e) {
            System.out.println("Transaction failed: " + e.getMessage());
        }
        return false;
    }
    // view details
    
}



   

