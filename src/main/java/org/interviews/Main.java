package org.interviews;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Entry point class that handles calculation of input expression.
 * Each part of the expression needs to be separated by space.
 */
public class Main {
    public static void main(String[] args) {
        var main = new Main();

        var console = System.console();

        console.writer().println("Please ensure each number is separated by space:  ");
        console.writer().println("example 1: 10 + 2");
        console.writer().println("example 2: ( 10 + 2 )");
        console.writer().println("example 3: ( 10 + 2 ) + 5 * 4 * 7 + ( 10 + 3 )");
        // Keep running enter user hits Ctrl+c or enters exit
        while (true) {
            var input = console.readLine("Enter your space separated expression: for ex: 10 + 2 (enter exit to quit):\n");
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            System.out.printf("Result for input: %s = %s%n", input, main.simpleCalc(input));
        }
    }

    /**
     * Main function to be called to validate the input and provide a result.
     *
     * @param input input string entered by the user.
     * @return result string.
     */
    public String simpleCalc(String input) {
        var list = new ArrayList<>(
            Arrays.asList(input.split(" "))
        );
        return validate(list).orElseGet(() -> calculate(list));
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
        return Optional.empty();
    }

    /**
     * Iterates through the list and returns the result as a string.
     *
     * @param list takes a list of numbers and operands
     * @return resulting value
     */
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

    /**
     * Perform multiple and divide
     *
     * @param list input list
     * @return result list
     */
    private List<String> performMultiplicationDivision(List<String> list) {
        return performDualOperation(list, "*", "/");
    }

    /**
     * Perform add and sub
     *
     * @param list input list
     * @return result list
     */
    private List<String> performAddSub(List<String> list) {
        return performDualOperation(list, "+", "-");
    }

    /**
     * Handles add and subtract. Also, multiply and divide.
     *
     * @param list input list
     * @param op1  operation 1 (multiply or add)
     * @param op2  operation 2 (divide or subtract)
     * @return resulting list
     */
    private List<String> performDualOperation(List<String> list, String op1, String op2) {
        final var op1Index = list.indexOf(op1);
        final var op2Index = list.indexOf(op2);
        var operand = op1;
        if (op1Index < 0 || (op2Index > 0 && op2Index < op1Index)) {
            operand = op2;
        }

        final var opIndex = list.indexOf(operand);
        final var x = Float.parseFloat(list.get(opIndex - 1));
        final var y = Float.parseFloat(list.get(opIndex + 1));
        final var result = calc(x, y, operand);
        var j = 0;
        final var resultList = new ArrayList<String>();
        while (j < list.size()) {
            if (j == opIndex - 1) {
                // Add the result instead of source expression to the new list.
                resultList.add(String.valueOf(result));
                j += 3;
            } else {
                resultList.add(list.get(j++));
            }
        }

        // Process the list recursively until both op1 and op2 are no longer in the list.
        if (resultList.contains(op1) || resultList.contains(op2)) {
            return performDualOperation(resultList, op1, op2);
        }
        return resultList;
    }

    /**
     * Handles parenthesis.
     *
     * @param list list containing the expression.
     * @return result list.
     */
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

    /**
     * Performs basic calculation: x operand y
     *
     * @param x       value 1
     * @param y       value 2
     * @param operand operation to be performed
     * @return result
     */
    private float calc(float x, float y, String operand) {
        return switch (operand) {
            case "+" -> x + y;
            case "-" -> x - y;
            case "*" -> x * y;
            case "/" -> x / y;
            default -> throw new IllegalArgumentException("Do not know how to deal with: " + operand);
        };
    }
}