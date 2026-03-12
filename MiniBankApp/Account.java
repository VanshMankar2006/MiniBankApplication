package Projects.MiniBankApp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Account {

    private String userFullname;
    private String userAccountNumber;
    private String userPin;
    private BigDecimal userBalance;
    private LocalDateTime userCreatedAt;

    public Account() {
    }

    public Account(String userFullname,String userPin, BigDecimal userBalance) {
        this.userFullname = userFullname;
        this.userAccountNumber = AccountUtility.generateAccountNumber();
        this.userPin = userPin;
        this.userBalance = BigDecimal.ZERO;
        
    }

    // Getters and Setters
    public String getUserFullname() { return userFullname; }
    public void setUserFullname(String userFullname) { this.userFullname = userFullname; }

    public String getUserAccountNumber() { return userAccountNumber; }

    public void setUserPin(String userPin) { this.userPin = userPin; }

    public BigDecimal getUserBalance() { return userBalance; }
    public void setUserBalance(BigDecimal userBalance) { this.userBalance = userBalance; }

    public LocalDateTime getUserCreatedAt() { return userCreatedAt; }
    public void setUserCreatedAt(LocalDateTime userCreatedAt) { this.userCreatedAt = userCreatedAt; }

}
   





