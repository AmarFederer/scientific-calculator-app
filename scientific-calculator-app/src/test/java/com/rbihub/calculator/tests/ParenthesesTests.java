package com.rbihub.calculator.tests;

import com.rbihub.calculator.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * ParenthesesTests — validates grouping and bracket handling.
 */
public class ParenthesesTests extends BaseTest {

    @Test(groups = {"regression", "parentheses"},
          description = "P01 - (2+3)x4 should equal 20")
    public void simpleGrouping() {
        logStep("(2+3)x4 =");
        calculator
            .clickOpenParen().typeNumber("2").clickAdd().typeNumber("3").clickCloseParen()
            .clickMultiply().typeNumber("4").clickEquals();
        String result = calculator.getDisplayValue();
        logStep("Result: " + result + " | Expected: 20");
        assertApproxEquals(parseDisplay(result), 20.0, 0.0001, "(2+3)x4");
    }

    @Test(groups = {"regression", "parentheses"},
          description = "P02 - 2x(3+4) should equal 14")
    public void groupingAfterOperand() {
        logStep("2x(3+4) =");
        calculator
            .typeNumber("2").clickMultiply()
            .clickOpenParen().typeNumber("3").clickAdd().typeNumber("4").clickCloseParen()
            .clickEquals();
        String result = calculator.getDisplayValue();
        logStep("Result: " + result + " | Expected: 14");
        assertApproxEquals(parseDisplay(result), 14.0, 0.0001, "2x(3+4)");
    }

    @Test(groups = {"regression", "parentheses"},
          description = "P03 - ((2+3)) should equal 5")
    public void doubleNestedParentheses() {
        logStep("((2+3)) =");
        calculator
            .clickOpenParen().clickOpenParen()
            .typeNumber("2").clickAdd().typeNumber("3")
            .clickCloseParen().clickCloseParen()
            .clickEquals();
        String result = calculator.getDisplayValue();
        logStep("Result: " + result + " | Expected: 5");
        assertApproxEquals(parseDisplay(result), 5.0, 0.0001, "((2+3))");
    }

    @Test(groups = {"regression", "parentheses"},
          description = "P04 - (1+(2x3)) should equal 7")
    public void nestedParentheses() {
        logStep("(1+(2x3)) =");
        calculator
            .clickOpenParen().typeNumber("1").clickAdd()
            .clickOpenParen().typeNumber("2").clickMultiply().typeNumber("3").clickCloseParen()
            .clickCloseParen().clickEquals();
        String result = calculator.getDisplayValue();
        logStep("Result: " + result + " | Expected: 7");
        assertApproxEquals(parseDisplay(result), 7.0, 0.0001, "(1+(2x3))");
    }

    @Test(groups = {"regression", "parentheses"},
          description = "P05 - Unmatched open paren: (2+3 = should show error or auto-evaluate to 5")
    public void unmatchedOpenParenthesis() {
        logStep("(2+3 = (no closing paren)");
        calculator.clickOpenParen().typeNumber("2").clickAdd().typeNumber("3").clickEquals();
        String result = calculator.getDisplayValue();
        logStep("Result: " + result);
        boolean acceptable = calculator.isDisplayShowingError()
            || Math.abs(parseDisplay(result) - 5.0) < 0.0001;
        Assert.assertTrue(acceptable,
            "BUG [P05]: Unmatched '(' should show Error or evaluate to 5. Actual: '" + result + "'");
    }

    @Test(groups = {"regression", "parentheses"},
          description = "P06 - Unmatched close paren: 2+3) = should not crash")
    public void unmatchedCloseParenthesis() {
        logStep("2+3) =");
        try {
            calculator.typeNumber("2").clickAdd().typeNumber("3").clickCloseParen().clickEquals();
            String result = calculator.getDisplayValue();
            logStep("Result: " + result);
            boolean acceptable = calculator.isDisplayShowingError()
                || Math.abs(parseDisplay(result) - 5.0) < 0.0001;
            Assert.assertTrue(acceptable,
                "BUG [P06]: Unmatched ')' should show Error or 5. Actual: '" + result + "'");
        } catch (Exception e) {
            Assert.fail("BUG [P06]: Calculator crashed on unmatched ')'. Error: " + e.getMessage());
        }
    }

    @Test(groups = {"regression", "parentheses"},
          description = "P07 - sin(0) + 1 should equal 1")
    public void scientificFunctionWithArithmetic() {
        logStep("sin(0) + 1 =");
        calculator.clickSin().typeNumber("0").clickCloseParen()
                  .clickAdd().typeNumber("1").clickEquals();
        String result = calculator.getDisplayValue();
        logStep("Result: " + result + " | Expected: 1");
        assertApproxEquals(parseDisplay(result), 1.0, 0.0001, "sin(0)+1");
    }
}
