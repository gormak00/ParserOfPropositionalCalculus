package by.bsuir;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Вы хотите ввести формулу:\n" +
                "1) ЯЛВ\n" +
                "2) сокращенного ЯЛВ");
        Scanner scannerInt = new Scanner(System.in);
        boolean shortLang;
        int a = scannerInt.nextInt();
        shortLang = a != 1;

        while (true){
            System.out.println("Введите формулу: ");
            Scanner scannerString = new Scanner(System.in);
            StringBuilder formula = new StringBuilder(scannerString.next());
            new Parser(formula, shortLang);
        }
    }
}
