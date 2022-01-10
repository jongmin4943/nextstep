package org.min.chapter02;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StrCalculatorTest {
    private StrCalculator calculator;

    @BeforeEach
    public void init() {
        calculator = new StrCalculator();
        System.out.println("before");
    }
    @Test
    public void strCalc_null() {
        assertEquals(0,calculator.calculate(null));
        assertEquals(0,calculator.calculate(" "));
    }

    @Test
    public void strCalc_one() {
        assertEquals(1,calculator.calculate("1"));
    }

    @Test
    public void strCalcComma() {
        assertEquals(3,calculator.calculate("1,2"));
    }

    @Test
    public void strCalcCommaColon() {
        assertEquals(6,calculator.calculate("1,2:3"));
    }

    @Test
    public void strCalcCustom() {
        assertEquals(6,calculator.calculate("//;\n1:2;3"));
    }

    @Test()
    public void strCalcNegative() {
        assertThrows(RuntimeException.class,() -> calculator.calculate("//;\n-1:2;3"));
    }
}
