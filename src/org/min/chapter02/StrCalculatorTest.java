package org.min.chapter02;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StrCalculatorTest {
    private StrCalculator calculator;

    @BeforeEach
    public void init() {
        calculator = new StrCalculator();
        System.out.println("before");
    }
    @Test
    public void strCalcTest() {
        assertEquals(6,calculator.calculate("//;\n1,2:3"));
        assertEquals(6,calculator.calculate("1,2:3"));
        assertEquals(6,calculator.calculate("//z\n1,2z3"));
    }
}
