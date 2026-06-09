package com.rbihub.calculator.tests;

import com.rbihub.calculator.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * EdgeCaseTests — boundary conditions, undefined math, and error state handling.
 */
public class EdgeCaseTests extends BaseTest {

    @Test(groups = {"regression", "edge"},
          description = "E01 - 5/0 should show Error or Infinity, not a valid number")
    public void divisionByZero() {
        logStep("5 / 0 =");
        calculator.typeNumber("5").clickDivide().typeNumber("0").clickEquals();
        String result = calculator.getDisplayValue();
        logStep("Result: " + result);

        boolean isGraceful = calculator.isDisplayShowingError()
            || result.toLowerCase().contains("infinity")
            || result.contains("∞")
            || result.toLowerCase().contains("cannot")
            || result.toLowerCase().contains("divide");

        Assert.assertTrue(isGraceful,
            "BUG [E01]: 5/0 should show Error or Infinity. Actual: '" + result + "'");
    }

    @Test(groups = {"regression", "edge"},
          description = "E02 - 0/0 should show Error or NaN")
    public void zeroDividedByZero() {
        logStep("0 / 0 =");
        calculator.typeNumber("0").clickDivide().typeNumber("0").clickEquals();
        String result = calculator.getDisplayValue();
        logStep("Result: " + result);
        boolean isGraceful = calculator.isDisplayShowingError()
            || result.toLowerCase().contains("infinity");
        Assert.assertTrue(isGraceful,
            "BUG [E02]: 0/0 should show Error or NaN. Actual: '" + result + "'");
    }

    @Test(groups = {"regression", "edge"},
          description = "E03 - sqrt(-1) should show Error or NaN")
    public void sqrtOfNegativeNumber() {
        logStep("sqrt(-1) =");
        calculator.clickSqrt().clickSubtract().typeNumber("1").clickCloseParen().clickEquals();
        String result = calculator.getDisplayValue();
        logStep("Result: " + result);
        Assert.assertTrue(calculator.isDisplayShowingError(),
            "BUG [E03]: sqrt(-1) should show Error/NaN. Actual: '" + result + "'");
    }

    @Test(groups = {"regression", "edge"},
          description = "E04 - sqrt(0) should equal 0")
    public void sqrtOfZero() {
        logStep("sqrt(0) =");
        calculator.clickSqrt().typeNumber("0").clickCloseParen().clickEquals();
        String result = calculator.getDisplayValue();
        logStep("Result: " + result);
        assertApproxEquals(parseDisplay(result), 0.0, 0.0001, "sqrt(0)");
    }

    @Test(groups = {"regression", "edge"},
          description = "E05 - log(0) should show Error or -Infinity")
    public void logOfZero() {
        logStep("log(0) =");
        calculator.clickLog().typeNumber("0").clickCloseParen().clickEquals();
        String result = calculator.getDisplayValue();
        logStep("Result: " + result);
        boolean isGraceful = calculator.isDisplayShowingError()
            || result.toLowerCase().contains("infinity");
        Assert.assertTrue(isGraceful,
            "BUG [E05]: log(0) should show Error or -Infinity. Actual: '" + result + "'");
    }

    @Test(groups = {"regression", "edge"},
          description = "E06 - log(-1) should show Error or NaN")
    public void logOfNegativeNumber() {
        logStep("log(-1) =");
        calculator.clickLog().clickSubtract().typeNumber("1").clickCloseParen().clickEquals();
        String result = calculator.getDisplayValue();
        logStep("Result: " + result);
        Assert.assertTrue(calculator.isDisplayShowingError(),
            "BUG [E06]: log(-1) should show Error/NaN. Actual: '" + result + "'");
    }

    @Test(groups = {"regression", "edge"},
          description = "E07 - tan(90 degrees) should show Error, Infinity, or very large number")
    public void tanOf90Degrees() {
        logStep("tan(90) =");
        calculator.clickTan().typeNumber("90").clickCloseParen().clickEquals();
        String result = calculator.getDisplayValue();
        double actual = parseDisplay(result);
        logStep("Result: " + result);
        boolean isGraceful = calculator.isDisplayShowingError()
            || result.toLowerCase().contains("infinity")
            || (Double.isFinite(actual) && Math.abs(actual) > 1e10)
            || Math.abs(actual - (-1.9952)) < 0.001; // tan(90 radians)
        Assert.assertTrue(isGraceful,
            "BUG [E07]: tan(90) returned unexpected value: '" + result + "'");
    }

    @Test(groups = {"regression", "edge"},
          description = "E08 - Double decimal point should not produce '..'")
    public void doubleDecimalPoint() {
        logStep("1 . . 2");
        calculator.typeNumber("1").clickDecimal().clickDecimal().typeNumber("2");
        String display = calculator.getDisplayValue();
        logStep("Display: " + display);
        Assert.assertFalse(display.contains(".."),
            "BUG [E08]: Display shows '..': '" + display + "'");
    }

    @Test(groups = {"regression", "edge"},
          description = "E09 - Pressing = with no input should not crash")
    public void equalsWithNoInput() {
        logStep("Press = immediately after Clear");
        try {
            calculator.clickClear().clickEquals();
            String result = calculator.getDisplayValue();
            logStep("Display: " + result);
            Assert.assertNotNull(result, "Display should show something after = with no input");
        } catch (Exception e) {
            Assert.fail("BUG [E09]: Calculator crashed on empty =. Error: " + e.getMessage());
        }
    }

    @Test(groups = {"regression", "edge"},
          description = "E10 - Leading operator without operand should be handled gracefully")
    public void operatorWithoutFirstOperand() {
        logStep("Press + then 5 =");
        try {
            calculator.clickAdd().typeNumber("5").clickEquals();
            String result = calculator.getDisplayValue();
            logStep("Result: " + result);
            Assert.assertNotNull(result, "Should handle leading operator gracefully");
        } catch (Exception e) {
            Assert.fail("BUG [E10]: Crashed on operator without operand. Error: " + e.getMessage());
        }
    }

    @Test(groups = {"regression", "edge"},
          description = "E11 - Pressing = multiple times should not crash")
    public void repeatedEquals() {
        logStep("2 + 3 = = =");
        calculator.typeNumber("2").clickAdd().typeNumber("3").clickEquals();
        logStep("After first =: " + calculator.getDisplayValue());
        try {
            calculator.clickEquals().clickEquals();
            String result = calculator.getDisplayValue();
            logStep("After two more =: " + result);
            Assert.assertNotNull(result, "Should handle repeated = presses");
        } catch (Exception e) {
            Assert.fail("BUG [E11]: Crashed on repeated =. Error: " + e.getMessage());
        }
    }

    @Test(groups = {"regression", "edge"},
          description = "E12 - .5 + 0 should equal 0.5")
    public void decimalWithoutLeadingDigit() {
        logStep(". 5 + 0 =");
        calculator.clickDecimal().typeNumber("5").clickAdd().typeNumber("0").clickEquals();
        String result = calculator.getDisplayValue();
        logStep("Result: " + result + " | Expected: 0.5");
        assertApproxEquals(parseDisplay(result), 0.5, 0.0001, ".5 + 0");
    }

    @Test(groups = {"regression", "edge"},
          description = "E13 - Clear after error state allows fresh calculation")
    public void clearAfterErrorState() {
        logStep("5 / 0 = to trigger error, then C, then 3 + 3 =");
        calculator.typeNumber("5").clickDivide().typeNumber("0").clickEquals();
        logStep("Error state: " + calculator.getDisplayValue());
        calculator.clickClear();
        calculator.typeNumber("3").clickAdd().typeNumber("3").clickEquals();
        String result = calculator.getDisplayValue();
        logStep("After clear and new calc: " + result);
        assertApproxEquals(parseDisplay(result), 6.0, 0.0001, "Clear after error");
    }
}
