package passwordDatabase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;


public class passwordHasher {
    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("data.txt");
        FileReader fr = new FileReader(input);
        BufferedReader br = new BufferedReader(fr);
        String line = null;
        Boolean userExist = false;
        Boolean passwordCorrect = false;
        int wrongPasswordAttempts = 0;
        Scanner in = new Scanner(System.in);
        // Get user name input from user
        System.out.print("Enter your username: ");
        String user = in.nextLine();

        String storedHashedPassword = ""; // Declare storedHashedPassword here

        try {
            while (((line = br.readLine()) != null) && (!userExist)) {
                String[] parts = line.split("\t");
                if (parts.length >= 4) {
                    userExist = parts[0].equals(user);
                    if (userExist) {
                        storedHashedPassword = parts[1];
                    }
                }

            }
            fr.close();
            br.close();
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }

        if (!userExist) {
            System.out.println("User not found. Creating a new user.");
            System.out.print("Enter your password: ");
            String password = in.nextLine();
            String salt = BCrypt.gensalt();
            String hashedPassword = BCrypt.hashpw(password, salt);

            String[] securityQuestions = {"What is the name of your first pet?",
                "What is your mother's maiden name?",
                "What is the name of the elementary school you attended?"};

            System.out.println("Choose one security question from the options below:");
            for (int i = 0; i < securityQuestions.length; i++) {
                System.out.println((i+1) + ": " + securityQuestions[i]);
            }
            int questionNumber = in.nextInt();
            in.nextLine(); // Consume the newline character
            String securityQuestion = securityQuestions[questionNumber-1];

            System.out.print(securityQuestion + "\n");

            System.out.print("Enter your answer to the security question: ");
            String securityAnswer = in.nextLine();
            System.out.print("Your answer was: " + securityAnswer + "\n");

            String newUserLine = user + "\t" + hashedPassword + "\t" + securityQuestion + "\t" + securityAnswer;

            try (FileWriter fw = new FileWriter(input, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter pw = new PrintWriter(bw)) {
                pw.println(newUserLine);
            } catch (IOException e) {
                System.out.print(e.getMessage());
            }

            System.out.println("New user created. Please log in again.");
            in.close();
            return;
        }


        while (!passwordCorrect && wrongPasswordAttempts < 3) {
            // Get password input
            System.out.print("Enter your password: ");
            String password = in.nextLine();
            passwordCorrect = BCrypt.checkpw(password, storedHashedPassword);
            if (!passwordCorrect) {
                wrongPasswordAttempts++;
                if (wrongPasswordAttempts < 3) {
                    System.out.println("The password is incorrect, please try again.");
                }
            }
        }

        if (!passwordCorrect) {
            int wrongSecurityQuestionAttempts = 0;
            boolean securityQuestionCorrect = false;
            System.out.println("You have entered the wrong password 3 times. Please answer your security question to reset your password.");

            String fileData = null;
            try {
                fileData = new String(new FileInputStream(input).readAllBytes());
            } catch (IOException e) {
                System.out.print(e.getMessage());
                return; // Exit the program since fileData is essential for further processing
            }

            String[] lines = fileData.split("\n");

            String storedAnswer = "";
            String storedHashedPassword1 = "";
            String storedQuestion = "";
            for (String currentLine : lines) {
                String[] parts = currentLine.split("\t");
                if (parts.length >= 4 && parts[0].equals(user)) {
                    String hashedPassword = parts[1];
                    String securityQuestion = parts[2];
                    storedAnswer = parts[3];
                    storedHashedPassword1 = hashedPassword;
                    storedQuestion = securityQuestion;
                    break;
                }
            }

            while (!securityQuestionCorrect && wrongSecurityQuestionAttempts < 3) {
                System.out.println("Security question: " + storedQuestion);
                System.out.print("Answer to your security question: ");
                String answer = in.nextLine();
                if (answer.trim().equals(storedAnswer.trim())) {
                    securityQuestionCorrect = true;
                } else {
                    wrongSecurityQuestionAttempts++;
                    if (wrongSecurityQuestionAttempts < 3) {
                        System.out.println("Incorrect answer, please try again");
                    }
                }
            }

            if (securityQuestionCorrect) {
                System.out.print("Enter your new password: ");
                String newPassword = in.nextLine();
                // Hash the new password using Bcrypt
                String salt = BCrypt.gensalt();
                String newHashedPassword = BCrypt.hashpw(newPassword, salt);
                String updatedLine = "";
                for (int i = 0; i < lines.length; i++) {
                    String currentLine = lines[i];
                    String[] parts = currentLine.split("\t");
                    if (parts.length >= 2 && parts[0].equals(user)) {
                        updatedLine = parts[0] + "\t" + newHashedPassword + "\t" + parts[2] + "\t" + parts[3];
                        lines[i] = updatedLine;
                        break;
                    }
                }

                try (FileOutputStream outputStream = new FileOutputStream("data.txt")) {
                    for (String currentLine : lines) {
                        outputStream.write(currentLine.getBytes());
                        outputStream.write("\n".getBytes());
                    }
                } catch (IOException e) {
                    System.out.print(e.getMessage());
                }

                System.out.println("Password has been reset. Please log in with the updated password.");
            } else {
                System.out.println("Too many incorrect guesses! Please contact your System's Administrator.");
            }
        } else {
            System.out.println("Welcome back " + user + "!");
        }

        in.close();
    }
}
