package Projects.MiniBankApp;
import java.util.Random;

public class AccountUtility {
     // method to generate randam acc number
    public static String generateAccountNumber() {
        Random random = new Random();
        int number = 10000000 + random.nextInt(90000000);
        return String.valueOf(number);
    }


    
}
