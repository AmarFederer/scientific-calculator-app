package com.rbihub.calculator.tests;

import com.rbihub.calculator.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * SanityTests — fast smoke checks that confirm the calculator is alive.
 *
 * These run first. If any sanity test fails, regression should be investigated.
 * Group: sanity
 */
public class SanityTests extends BaseTest {

    // ── S01: Page loads ───────────────────────────────────────────────────────

    @Test(groups = {"sanity"},
          description = "S01 - Calculator page loads and the display is visible")
    public void pageLoadsAndDisplayVisible() {
        logStep("Verify page title contains 'Calculator'");
        String title = driver.getTitle();
        Assert.assertTrue(
            title.toLowerCase().contains("calculator"),
            "Page title should contain 'Calculator' but was: " + title
        );

        logStep("Verify display element is present");
        Assert.assertFalse(
            calculator.getDisplayValue() == null,
            "Display should be present and not null"
        );
    }

    // ── S02: All critical buttons are present ─────────────────────────────────

    @Test(groups = {"sanity"},
          description = "S02 - All expected buttons are visible on the calculator")
    public void allButtonsAreVisible() {
        logStep("Verify Clear button is visible");
        Assert.assertTrue(
            calculator.isButtonVisible(calculator.getClearButtonLocator()),
            "Clear (C) button should be visible"
        );

        logStep("Verify Equals button is visible");
        Assert.assertTrue(
            calculator.isButtonVisible(calculator.getEqualsButtonLocator()),
            "Equals (=) button should be visible"
        );

        logStep("Verify Add button is visible");
        Assert.assertTrue(
            calculator.isButtonVisible(calculator.getAddButtonLocator()),
            "Add (+) button should be visible"
        );

        logStep("Verify Divide button is visible");
        Assert.assertTrue(
            calculator.isButtonVisible(calculator.getDivideButtonLocator()),
            "Divide (÷) button should be visible"
        );

        logStep("Verify sin button is visible");
        Assert.assertTrue(
            calculator.isButtonVisible(calculator.getSinButtonLocator()),
            "sin button should be visible"
        );

        logStep("Verify cos button is visible");
        Assert.assertTrue(
            calculator.isButtonVisible(calculator.getCosButtonLocator()),
            "cos button should be visible"
        );

        logStep("Verify tan button is visible");
        Assert.assertTrue(
            calculator.isButtonVisible(calculator.getTanButtonLocator()),
            "tan button should be visible"
        );

        logStep("Verify √ button is visible");
        Assert.assertTrue(
            calculator.isButtonVisible(calculator.getSqrtButtonLocator()),
            "√ button should be visible"
        );

        logStep("Verify log button is visible");
        Assert.assertTrue(
            calculator.isButtonVisible(calculator.getLogButtonLocator()),
            "log button should be visible"
        );
    }

    // ── S03: Basic addition ───────────────────────────────────────────────────

    @Test(groups = {"sanity"},
          description = "S03 - Basic addition: 2 + 3 = 5")
    public void basicAdditionSmoke() {
        logStep("Enter: 2 + 3 =");
        calculator
            .typeNumber("2")
            .clickAdd()
            .typeNumber("3")
            .clickEquals();

        String result = calculator.getDisplayValue();
        logStep("Verify result is 5, actual: " + result);
        Assert.assertEquals(result, "5",
            "2 + 3 should equal 5 but display shows: " + result);
    }

    // ── S04: Display updates on digit click ───────────────────────────────────

    @Test(groups = {"sanity"},
          description = "S04 - Display updates immediately when a digit is clicked")
    public void displayUpdatesOnDigitClick() {
        logStep("Click digit 7");
        calculator.clickDigit(7);

        String display = calculator.getDisplayValue();
        logStep("Display shows: " + display);
        Assert.assertTrue(
            display.contains("7"),
            "Display should show '7' after clicking digit 7, but shows: " + display
        );
    }

    // ── S05: Clear resets display ─────────────────────────────────────────────

    @Test(groups = {"sanity"},
          description = "S05 - Clear button resets the display")
    public void clearResetsDisplay() {
        logStep("Enter digits 9, 8, 7");
        calculator.typeNumber("987");

        logStep("Press Clear");
        calculator.clickClear();

        String display = calculator.getDisplayValue();
        logStep("Display after clear: " + display);
        Assert.assertTrue(
            display.equals("0") || display.isEmpty(),
            "Display should be '0' or empty after Clear, but shows: " + display
        );
    }
}
