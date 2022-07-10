package org.interviews;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class MainTest {

    @ParameterizedTest(name = "{0}")
    @CsvSource(value = {
            "7 + 5 * 4 - 2 + 4 / 4 + ( 8 + 5 )   | 39",
            "(7 + 5) * 4 - 2 + 4 / 4 + ( 8 + 5 ) | 60",
            "7 * 5 * 4 - 2 + 4 / 4 + 8 + 5       | 152",
            "7 * 5 + 4                           | 39",
    }, delimiter = '|')
    void testSimpleCalc(
            String input,
            float expectedResult
    ) {
        Main main = new Main();
        assertThat(main.simpleCalc(input)).isEqualTo(expectedResult);
    }
}
