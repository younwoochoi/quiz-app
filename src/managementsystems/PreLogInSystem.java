package managementsystems;

import java.util.Scanner;

/**
 * Launched before log in main sequence:
 * Asks for user's choice of language
 */
public class PreLogInSystem {

    public String promptForLanguage() {
        System.out.println("Please choose a language: ");
        System.out.println("1, English");
        System.out.println("2, Español");
        Scanner scanner = new Scanner(System.in);
        String userIn = scanner.nextLine();
        if (userIn.equalsIgnoreCase("1")) {
            return "english";
        } else if (userIn.equalsIgnoreCase("2")){
            return "spanish";
        }
        else {return "english";}
    }
}
