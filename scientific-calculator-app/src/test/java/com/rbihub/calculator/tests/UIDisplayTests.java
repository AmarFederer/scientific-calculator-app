package com.rbihub.calculator.tests;

import com.rbihub.calculator.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * UIDisplayTests — verifies display formatting, precision, and visual integrity.
 */
public class UIDisplayTests extends BaseTest {

    @Test(groups = {"regression", "ui"},
          description = "UI01 - 0.1+0.2 should not show excessive floating-point noise")
    public void noFloatingPointNoise() {
        logStep("0.1 + 0.2 =");
        calculator.typeNumber("0.1").clickAdd().typeNumber("0.2").clickEquals();
        String result = calculator.getDisplayValue();
        logStep("Result: " + result);

        // Must be close to 0.3 — if it's 0.30000000000000004 that's a UI bug
        double actual = parseDisplay(result);
        assertApproxEquals(actual, 0.3, 0.00001, "0.1+0.2 float noise check");

        Assert.assertFalse(result.length() > 15,
            "BUG [UI01]: Floating-point noise in result: '" + result + "'");
    }

    @Test(groups = {"regression", "ui"},
          description = "UI02 - Display is not empty while entering expression")
    public void displayShowsExpressionAsBuilt() {
        logStep("Type 3 + 4 without pressing =");
        calculator.typeNumber("3").clickAdd().typeNumber("4");
        String mid = calculator.getDisplayValue();
        logStep("Display mid-entry: " + mid);
        Assert.assertFalse(mid.isEmpty(),
            "BUG [UI02]: Display should not be empty while entering expression");
    }

    @Test(groups = {"regression", "ui"},
          description = "UI03 - Negative result shows minus sign: 3 - 9 = -6")
    public void negativeResultDisplay() {
        logStep("3 - 9 =");
        calculator.typeNumber("3").clickSubtract().typeNumber("9").clickEquals();
        String result = calculator.getDisplayValue();
        double actual = parseDisplay(result);
        logStep("Result: " + result);
        assertApproxEquals(actual, -6.0, 0.0001, "3 - 9");
        Assert.assertTrue(result.contains("-"),
            "BUG [UI03]: Negative result must contain '-'. Actual: '" + result + "'");
    }

    @Test(groups = {"regression", "ui"},
          description = "UI04 - 8/2 should display 4 without excessive decimal notation")
    public void integerResultNoTrailingZeros() {
        logStep("8 / 2 =");
        calculator.typeNumber("8").clickDivide().typeNumber("2").clickEquals();
        String result = calculator.getDisplayValue();
        logStep("Result: " + result);
        double actual = parseDisplay(result);
        assertApproxEquals(actual, 4.0, 0.0001, "8/2");
        // "4" or "4.0" are both acceptable — but not "4.0000000"
        Assert.assertFalse(result.matches(".*\\.0{4,}.*"),
            "BUG [UI04]: Result has unnecessary zeros: '" + result + "'");
    }

    @Test(groups = {"regression", "ui"},
          description = "UI05 - Page title should contain 'Calculator'")
    public void pageTitleIsCorrect() {
        logStep("Check page title");
        String title = driver.getTitle();
        logStep("Page title: " + title);
        Assert.assertFalse(title.isEmpty(), "Page title should not be empty");
        Assert.assertTrue(title.toLowerCase().contains("calculator"),
            "BUG [UI05]: Title should mention 'calculator'. Actual: '" + title + "'");
    }

    @Test(groups = {"regression", "ui"},
          description = "UI06 - Long number 123456789 should be visible in display")
    public void longNumberDisplayHandling() {
        logStep("Type 123456789");
        calculator.typeNumber("123456789");
        String display = calculator.getDisplayValue();
        logStep("Display: " + display);
        // Accept full number or scientific notation
        Assert.assertTrue(
            display.contains("123456789") || display.contains("1.23") || display.contains("e"),
            "BUG [UI06]: Long number not visible. Display: '" + display + "'");
    }

    @Test(groups = {"regression", "ui"},
          description = "UI07 - 1/3 should show a reasonable decimal, not '0' or 20+ chars")
    public void decimalResultPrecision() {
        logStep("1 / 3 =");
        calculator.typeNumber("1").clickDivide().typeNumber("3").clickEquals();
        String result = calculator.getDisplayValue();
        logStep("Result: " + result);
        Assert.assertFalse(result.equals("0"),
            "BUG [UI07]: 1/3 should not display '0'. Actual: '" + result + "'");
        Assert.assertTrue(result.length() <= 20,
            "BUG [UI07]: 1/3 result too long (>20 chars). Actual: '" + result + "'");
    }
}
