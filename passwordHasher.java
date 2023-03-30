import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.io.FileOutputStream;
import java.io.IOException;

public class passwordHasher {
    public static void main(String[] args) throws NoSuchAlgorithmException {
    	Scanner in = new Scanner(System.in);
    	
    	//Get user name input from use
    	System.out.print("Enter your username: ");
        String user = in.nextLine();
        
        // Get password input from user
        System.out.print("Enter your password: ");
        String password = in.nextLine();
        
        in.close();

        // Hash the password using SHA-256 algorithm
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hash = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));

        // Convert the byte array hash into a hexadecimal string
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        String output = ("Username: " + user + '\n' + "Hashed password: " + hexString.toString());
        // Print the username and hashed password in hexadecimal format
        System.out.println(output);
       
        FileOutputStream outputStream = null;
        
        // Try block to check if exception occurs
        try {
            outputStream = new FileOutputStream("data.txt");
            // Store byte content from string
            byte[] strToBytes = output.getBytes();

            // Write into the file
            outputStream.write(strToBytes);
        }
        // Catch block to handle the exception
        catch (IOException e) {
            System.out.print(e.getMessage());
        }
    }
}

//entering "password" hashes to: 5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8 (it is the same hash each time)