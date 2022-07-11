package org.interviews;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

public class MainTest {

    private static final Main main = new Main();

    @ParameterizedTest(name = "{0}")
    @CsvSource(value = {
        "2 ^ 3                                 | 8.0f",
        "2 ^ 3 * 10                            | 80.0f",
        "2 ^ 3 * 10 / 2                        | 40.0f",
        "2 ^ 3 * 10 / 2 + 5 - 4.3              | 40.7f",
        "7 + 5 * 4 - 2 + 4 / 4 + ( 8 + 5 )     | 39.0f",
        "( 7 + 5 ) * 4 - 2 + 4 / 4 + ( 8 + 5 ) | 60.0f",
        "( 7 + 5 * 4 ) - 2 + 4 / 4 + 8 + 5     | 39.0f",
        "( 7 + 5 ) * ( 4 - 2 ) + 4 / 4 + 8 + 5 | 38.0f",
        "7 * 5 * 4 - 2 + 4 / 4 + 8 + 5         | 152.0f",
        "7 * 5 + 4                             | 39.0f",
        "10 + 2                                | 12.0f",
    }, delimiter = '|')
    void testSimpleCalc(
        String input,
        Float expectedResult
    ) {
        assertThat(Float.parseFloat(main.simpleCalc(input))).isEqualTo(expectedResult);
    }

    @ParameterizedTest(name = "{0}")
    @CsvSource(value = {
        "7 + 5 * 4 - 2 + 4 / 4 + ( 8 + 5 | Parenthesis don't match",
    }, delimiter = '|')
    void testValidation(
        String input,
        String expectedResult
    ) {
        assertThat(main.simpleCalc(input)).isEqualTo(expectedResult);
    }
}
