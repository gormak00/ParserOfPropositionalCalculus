package by.bsuir;

import java.util.*;

public class Parser {
    private final StringBuilder formula;
    private String LOGIC_TRUE;
    private String LOGIC_FALSE;
    private final String IMPLICATION = "->";
    private final String CONJUNCTION = "/\\";
    private final String DISJUNCTION = "\\/";
    private final String NEGATION = "!";
    private final String EQUIVALENCE = "~";
    private final String LEFT_BRACKET = "(";
    private final String RIGHT_BRACKET = ")";
    private final List<Character> symbol = Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');
    private final List<String> binaryBundle = Arrays.asList(IMPLICATION, CONJUNCTION, DISJUNCTION, EQUIVALENCE);
    private boolean shortLang;

    public Parser(StringBuilder formula, boolean shortLang) {
        this.formula = formula;
        this.shortLang = shortLang;

        if (shortLang){
            LOGIC_TRUE = "1";
            LOGIC_FALSE = "0";
        } else {
            LOGIC_TRUE = "T";
            LOGIC_FALSE = ".L";
        }

        if (isFormula(formula)) {
            System.out.println("Данная строка является формулой сокращенного языка логики высказываний");
        } else System.out.println("Данная строка не является формулой сокращенного языка логики высказываний");
    }

    private boolean isFormula(StringBuilder formula) {
        if (formula.length() == 0 || formula.toString().equals("()")) return false;
        if(shortLang) {
            if (formula.toString().equals(LOGIC_TRUE) && formula.length() == 1 || formula.toString().equals(LOGIC_FALSE) && formula.length() == 1)
                return true;
        } else {
            if (formula.toString().equals(LOGIC_TRUE) && formula.length() == 1 || formula.toString().equals(LOGIC_FALSE) && formula.length() == 2)
            return true;
        }
        if (formula.substring(0, 1).equals(LEFT_BRACKET) && formula.substring(formula.length() - 1, formula.length()).equals(RIGHT_BRACKET) && isAtomicFormula(new StringBuilder(formula.substring(1,formula.length()-1)))) return false;
        if (isAtomicFormula(formula)) return true;
        if (isUnaryComplexFormula(formula)) return true;
        if (isBinaryFormula(formula)) return true;
        return false;
    }

    private boolean isAtomicFormula(StringBuilder formula) {
        if (symbol.contains(formula.charAt(0)) && formula.length() == 1) return true;
        if (!symbol.contains(formula.charAt(0)) && formula.length() == 1) return false;
        if(!shortLang) {
            if (symbol.contains(formula.charAt(0)) && formula.charAt(1) != '0') {
                try {
                    Integer.parseInt(formula.substring(1));
                } catch (Exception e) {
                    return false;
                }
                return true;
            } else return false;
        }
        return false;
    }

    private boolean isUnaryComplexFormula(StringBuilder formula) {
        StringBuilder substringFormula;

        if (!formula.substring(0, 1).equals(LEFT_BRACKET)) return false;
        if (!formula.substring(formula.length() - 1, formula.length()).equals(RIGHT_BRACKET)) return false;

        if (formula.substring(1, 2).equals(NEGATION)) {
            substringFormula = new StringBuilder(formula.substring(2, formula.length() - 1));
        } else substringFormula = new StringBuilder(formula.substring(1, formula.length() - 1));

        return isFormula(substringFormula);
    }

    private boolean isBinaryFormula(StringBuilder formula) {
        StringBuilder firstSubstring = null, secondSubstring = null;
        List<Integer> listOfBinaryBundlesIndexes = new ArrayList<>();
        if (!formula.substring(0, 1).equals(LEFT_BRACKET)) return false;
        if (!formula.substring(formula.length() - 1, formula.length()).equals(RIGHT_BRACKET)) return false;

        binaryBundle.forEach(bundle -> {
            int position = 0;
            while (position != -1) {
                position = formula.indexOf(bundle, position + 1);
                if (position != -1) listOfBinaryBundlesIndexes.add(position);
            }
        });
        Collections.sort(listOfBinaryBundlesIndexes);
        if (listOfBinaryBundlesIndexes.size() != 0) {
            int indexOfBundle = 0;
            int countOfBrackets = 0;
            for (int i = 1; i < formula.length() - 1; i++) {
                if (formula.substring(i, i + 1).equals(LEFT_BRACKET)) {
                    countOfBrackets++;
                } else if (formula.substring(i, i + 1).equals(RIGHT_BRACKET)) {
                    countOfBrackets--;
                }
                if (i == listOfBinaryBundlesIndexes.get(indexOfBundle)) {
                    if (indexOfBundle < listOfBinaryBundlesIndexes.size() - 1) indexOfBundle++;
                    if (countOfBrackets == 0) {
                        firstSubstring = new StringBuilder(formula.substring(1, i));
                        if (formula.substring(i, i + 2).equals(IMPLICATION) || formula.substring(i, i + 2).equals(CONJUNCTION) || formula.substring(i, i + 2).equals(DISJUNCTION))
                            secondSubstring = new StringBuilder(formula.substring(i + 2, formula.length() - 1));
                        else secondSubstring = new StringBuilder(formula.substring(i + 1, formula.length() - 1));
                        break;
                    }
                }
            }
        }
        if (firstSubstring == null && secondSubstring == null) return false;
        return isFormula(firstSubstring) && isFormula(secondSubstring);
    }
}
