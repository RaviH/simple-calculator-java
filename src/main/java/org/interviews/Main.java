package org.interviews;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();

        var input = "7+5*4-2+4/4+(8+5)";
        main.simpleCalc(input);
    }

    /**
     * Since the emphasis is on keeping it simple,
     * perform some basic validations.
     *
     * @param input input string from the user
     * @return a validation error message or empty.
     */
    private Optional<String> validate(List<String> input) {
        var charToCountMap =
            input.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));

        if (charToCountMap.containsKey("(")) {
            if (!charToCountMap.get("(").equals(charToCountMap.get(")"))) {
                return Optional.of("Parenthesis don't match");
            }
        }
        if (isNotNumericOrParens(input.get(0)) || isNotNumericOrParens(input.get(input.size()-1))) {
            return Optional.of("Input string must start and end with a number");
        }
        return Optional.empty();
    }

    /**
     * Checks if input is a number or parenthesis.
     *
     * @param input input param
     * @return true if it's a number of parenthesis
     */
    public static boolean isNotNumericOrParens(String input) {
        if (input.equals("(") || input.equals(")")) {
            return false;
        }
        try {
            Float.parseFloat(input);
        } catch (NumberFormatException nfe) {
            return true;
        }
        return false;
    }

    public String simpleCalc(String input) {
        var list = new ArrayList<>(
            Arrays.asList(input.replace(" ", "").split(""))
        );
        return validate(list).orElseGet(() -> calculate(list));
    }

    private String calculate(List<String> list) {
        if (list.contains("(")) {
            list = parens(list);
        }

        if (list.contains("*") || list.contains("/")) {
            list = performMultiplicationDivision(list);
        }

        if (list.contains("+") || list.contains("-")) {
            list = performAddSub(list);
        }
        return list.get(0);
    }

    private List<String> performMultiplicationDivision(List<String> list) {
        return performDualOperation(list, "*", "/");
    }

    private List<String> performAddSub(List<String> list) {
        return performDualOperation(list, "+", "-");
    }

    private List<String> performDualOperation(List<String> list, String op1, String op2) {
        final int op1Index = list.indexOf(op1);
        final int op2Index = list.indexOf(op2);
        String operand = op1;
        if (op1Index < 0 || (op2Index > 0 && op2Index < op1Index)) {
            operand = op2;
        }

        final int opIndex = list.indexOf(operand);
        final float x = Float.parseFloat(list.get(opIndex - 1));
        final float y = Float.parseFloat(list.get(opIndex + 1));
        final float result = calc(x, y, operand);
        int j = 0;
        ArrayList<String> newList = new ArrayList<>();
        while (j < list.size()) {
            if (j == opIndex - 1) {
                newList.add(String.valueOf(result));
                j += 3;
            } else {
                newList.add(list.get(j++));

            }
        }

        if (newList.contains(op1) || newList.contains(op2)) {
            return performDualOperation(newList, op1, op2);
        }
        return newList;
    }

    private List<String> parens(List<String> list) {
        final int startIndex = list.indexOf("(");
        final int endIndex = list.indexOf(")");
        float result = Float.parseFloat(calculate(list.subList(startIndex + 1, endIndex)));

        ArrayList<String> afterOpList = new ArrayList<>();
        int j = 0;
        while (j < list.size()) {
            if (j >= startIndex && j <= endIndex) {
                j++;
                if (j == endIndex) {
                    afterOpList.add(String.valueOf(result));
                }
                continue;
            }

            afterOpList.add(list.get(j++));
        }

        if (afterOpList.contains("(")) {
            return parens(afterOpList);
        }

        return afterOpList;
    }

    public float calc(float x, float y, String operand) {
        return switch (operand) {
            case "+" -> x + y;
            case "-" -> x - y;
            case "*" -> x * y;
            case "/" -> x / y;
            default -> throw new IllegalArgumentException("Do not know how to deal with: " + operand);
        };
    }
}