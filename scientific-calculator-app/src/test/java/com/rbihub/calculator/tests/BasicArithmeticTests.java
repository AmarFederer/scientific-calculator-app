package com.rbihub.calculator.tests;

import com.rbihub.calculator.base.BaseTest;
import com.rbihub.calculator.utils.TestDataProvider;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * BasicArithmeticTests — covers +, -, x, / operations.
 */
public class BasicArithmeticTests extends BaseTest {

    @Test(groups = {"regression", "arithmetic"},
          dataProvider = "additionData",
          dataProviderClass = TestDataProvider.class,
          description = "A01-A07 - Addition with various operands")
    public void addition(String a, String b, String expected) {
        logStep(a + " + " + b + " = ?");
        calculator.typeNumber(a).clickAdd().typeNumber(b).clickEquals();
        String result = calculator.getDisplayValue();
        logStep("Expected: " + expected + " | Actual: " + result);
        assertApproxEquals(parseDisplay(result), Double.parseDouble(expected), 0.0001,
            a + " + " + b);
    }

    @Test(groups = {"regression", "arithmetic"},
          dataProvider = "subtractionData",
          dataProviderClass = TestDataProvider.class,
          description = "A08-A12 - Subtraction including negative results")
    public void subtraction(String a, String b, String expected) {
        logStep(a + " - " + b + " = ?");
        calculator.typeNumber(a).clickSubtract().typeNumber(b).clickEquals();
        String result = calculator.getDisplayValue();
        logStep("Expected: " + expected + " | Actual: " + result);
        assertApproxEquals(parseDisplay(result), Double.parseDouble(expected), 0.0001,
            a + " - " + b);
    }

    @Test(groups = {"regression", "arithmetic"},
          dataProvider = "multiplicationData",
          dataProviderClass = TestDataProvider.class,
          description = "A13-A18 - Multiplication")
    public void multiplication(String a, String b, String expected) {
        logStep(a + " x " + b + " = ?");
        calculator.typeNumber(a).clickMultiply().typeNumber(b).clickEquals();
        String result = calculator.getDisplayValue();
        logStep("Expected: " + expected + " | Actual: " + result);
        assertApproxEquals(parseDisplay(result), Double.parseDouble(expected), 0.0001,
            a + " x " + b);
    }

    @Test(groups = {"regression", "arithmetic"},
          dataProvider = "divisionData",
          dataProviderClass = TestDataProvider.class,
          description = "A19-A24 - Division including fractional results")
    public void division(String a, String b, String expected) {
        logStep(a + " / " + b + " = ?");
        calculator.typeNumber(a).clickDivide().typeNumber(b).clickEquals();
        String result = calculator.getDisplayValue();
        logStep("Expected: " + expected + " | Actual: " + result);
        assertApproxEquals(parseDisplay(result), Double.parseDouble(expected), 0.0001,
            a + " / " + b);
    }

    @Test(groups = {"regression", "arithmetic"},
          description = "A25 - Chained addition: 2 + 3 = then + 4 = should equal 9")
    public void chainedAddition() {
        logStep("2 + 3 =");
        calculator.typeNumber("2").clickAdd().typeNumber("3").clickEquals();
        logStep("Intermediate: " + calculator.getDisplayValue());
        logStep("+ 4 =");
        calculator.clickAdd().typeNumber("4").clickEquals();
        String result = calculator.getDisplayValue();
        logStep("Final: " + result);
        assertApproxEquals(parseDisplay(result), 9.0, 0.0001, "Chained addition");
    }

    @Test(groups = {"regression", "arithmetic"},
          description = "A26 - Operator precedence: 2 + 3 x 4 — document actual behaviour")
    public void operatorPrecedenceCheck() {
        logStep("2 + 3 x 4 =");
        calculator.typeNumber("2").clickAdd().typeNumber("3").clickMultiply()
                  .typeNumber("4").clickEquals();
        String result = calculator.getDisplayValue();
        double actual = parseDisplay(result);
        logStep("Result: " + result + " | PEMDAS=14, left-to-right=20");
        Assert.assertTrue(actual == 14.0 || actual == 20.0,
            "Expected 14 (PEMDAS) or 20 (left-to-right) but got: " + result);
    }

    @Test(groups = {"regression", "arithmetic"},
          description = "A27 - Decimal precision: 0.1 + 0.2")
    public void decimalPrecision() {
        logStep("0.1 + 0.2 =");
        calculator.typeNumber("0.1").clickAdd().typeNumber("0.2").clickEquals();
        String result = calculator.getDisplayValue();
        logStep("Result: " + result + " | Expected ~0.3");
        assertApproxEquals(parseDisplay(result), 0.3, 0.00001, "0.1 + 0.2");
    }

    @Test(groups = {"regression", "arithmetic"},
          description = "A28 - Large number: 99999 x 99999")
    public void largeNumberMultiplication() {
        logStep("99999 x 99999 =");
        calculator.typeNumber("99999").clickMultiply().typeNumber("99999").clickEquals();
        String result = calculator.getDisplayValue();
        logStep("Result: " + result + " | Expected: 9999800001");
        assertApproxEquals(parseDisplay(result), 9999800001.0, 1.0, "99999 x 99999");
    }
}
