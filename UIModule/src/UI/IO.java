package UI;

import java.util.Scanner;

public class IO {
    private static Scanner scanner = new Scanner(System.in);

    public static int getInt(){
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a number (INT)!");
            scanner.next();
        }

        return scanner.nextInt();
    }

    public static double getDouble(){
        while (!scanner.hasNextDouble()) {
            System.out.println("Please enter a number!");
            scanner.next();
        }

        return scanner.nextDouble();
    }

}
