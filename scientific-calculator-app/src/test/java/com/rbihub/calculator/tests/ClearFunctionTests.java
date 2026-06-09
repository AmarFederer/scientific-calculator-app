package com.rbihub.calculator.tests;

import com.rbihub.calculator.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * ClearFunctionTests — validates the C button and state reset.
 *
 * Groups: regression, clear
 */
public class ClearFunctionTests extends BaseTest {

    @Test(groups = {"regression", "clear"},
          description = "CL01 - Clear resets display to 0 or empty")
    public void clearResetsDisplay() {
        logStep("Type 987, then press C");
        calculator.typeNumber("987").clickClear();

        String result = calculator.getDisplayValue();
        logStep("Display after Clear: " + result);

        Assert.assertTrue(
            result.equals("0") || result.isEmpty(),
            "BUG [CL01]: Display should be '0' or empty after Clear. Actual: '" + result + "'");
    }

    @Test(groups = {"regression", "clear"},
          description = "CL02 - Clear after a result resets correctly")
    public void clearAfterResult() {
        logStep("Enter: 2 + 2 =, then C");
        calculator
            .typeNumber("2")
            .clickAdd()
            .typeNumber("2")
            .clickEquals();

        logStep("Result before clear: " + calculator.getDisplayValue());
        calculator.clickClear();

        String result = calculator.getDisplayValue();
        logStep("Display after Clear: " + result);

        Assert.assertTrue(
            result.equals("0") || result.isEmpty(),
            "BUG [CL02]: Clear after result should reset to 0. Actual: '" + result + "'");
    }

    @Test(groups = {"regression", "clear"},
          description = "CL03 - New calculation after Clear works correctly")
    public void newCalculationAfterClear() {
        logStep("Enter: 9 + 1 =, then C, then 4 + 4 =");
        calculator
            .typeNumber("9")
            .clickAdd()
            .typeNumber("1")
            .clickEquals();

        logStep("First result: " + calculator.getDisplayValue());
        calculator.clickClear();

        calculator
            .typeNumber("4")
            .clickAdd()
            .typeNumber("4")
            .clickEquals();

        String result = calculator.getDisplayValue();
        logStep("Second result (4+4): " + result);

        assertApproxEquals(parseDisplay(result), 8.0, 0.0001,
            "New calculation after Clear");
    }

    @Test(groups = {"regression", "clear"},
          description = "CL04 - Multiple clears in sequence do not cause errors")
    public void multipleConsecutiveClears() {
        logStep("Press C three times in succession");
        try {
            calculator.clickClear().clickClear().clickClear();
            String result = calculator.getDisplayValue();
            logStep("Display after 3× Clear: " + result);

            Assert.assertTrue(
                result.equals("0") || result.isEmpty(),
                "BUG [CL04]: Multiple clears should remain stable. Actual: '" + result + "'");
        } catch (Exception e) {
            Assert.fail("BUG [CL04]: Calculator crashed on multiple clears. " +
                "Error: " + e.getMessage());
        }
    }

    @Test(groups = {"regression", "clear"},
          description = "CL05 - Clear after error state allows new input")
    public void clearAfterErrorState() {
        logStep("Cause error: 5 ÷ 0 =, then C");
        calculator
            .typeNumber("5")
            .clickDivide()
            .typeNumber("0")
            .clickEquals();

        logStep("Error state display: " + calculator.getDisplayValue());
        calculator.clickClear();

        logStep("Now entering: 3 + 3 =");
        calculator
            .typeNumber("3")
            .clickAdd()
            .typeNumber("3")
            .clickEquals();

        String result = calculator.getDisplayValue();
        logStep("Result after clearing error: " + result);

        assertApproxEquals(parseDisplay(result), 6.0, 0.0001,
            "Clear after error allows fresh calculation");
    }
}
