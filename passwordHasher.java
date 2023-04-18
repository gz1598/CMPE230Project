import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class passwordHasher {
    public static void main(String[] args) throws NoSuchAlgorithmException, FileNotFoundException {
    	File input = new File("data.txt");
    	FileReader fr=new FileReader(input);   //reads the file  
    	BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream  
    	StringBuffer sb=new StringBuffer();    //constructs a string buffer with no characters  
    	String line = null;  
    	Boolean userExist = false;
    	Boolean passwordCorrect = false;
    	
    	Scanner in = new Scanner(System.in);
    	
    	//Get user name input from use
    	System.out.print("Enter your username: ");
        String user = in.nextLine();
        
     // Try block to check if exception occurs
        try {
        	while(((line=br.readLine())!=null) && (userExist == false))  
        	{  
        		userExist = line.contains(user);
        		sb.append(line);      //appends line to string buffer  
        		sb.append("\n");     //line feed   
        	} 
        	fr.close();
        	br.close();
        }
        // Catch block to handle the exception
        catch (IOException e) {
            System.out.print(e.getMessage());
        }
        
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

        String output = ("Username: " + user + '\t' + "Hashed password: " + hexString.toString() + '\n');
        // Print the username and hashed password in hexadecimal format
        System.out.println(output);
       
        if (userExist == false) {
	        FileOutputStream outputStream = null;
	        
	        // Try block to check if exception occurs
	        try {
	            outputStream = new FileOutputStream("data.txt", true);
	            // Store byte content from string
	            byte[] strToBytes = output.getBytes();
	
	            // Write into the file
	            outputStream.write(strToBytes);
	            
	            outputStream.close();
	        }
	        // Catch block to handle the exception
	        catch (IOException e) {
	            System.out.print(e.getMessage());
	        }
        } else {
        	passwordCorrect = line.contains(hexString.toString());
        	
        	if (passwordCorrect == true) {
        		System.out.println("Welcome back " + user);
        	} else {
        		System.out.println("The password is incorrect, please try again");
        	}
        }
    }
}

//entering "password" hashes to: 5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8 (it is the same hash each time)