package org.min.chapter01;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {
    private Calculator calculator;
    @BeforeEach
    public void init() {
        calculator = new Calculator();
        System.out.println("before");
    }

    @Test
    public void add() {
        assertEquals(9,calculator.add(6,3));
        System.out.println("add");
    }

    @Test
    public void subtract() {
        assertEquals(3,calculator.subtract(6,3));
        System.out.println("subtract");
    }

    @AfterEach
    public void teardown() {
        System.out.println("after");
    }
}
