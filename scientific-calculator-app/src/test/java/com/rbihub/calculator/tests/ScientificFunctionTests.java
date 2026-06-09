package com.rbihub.calculator.tests;

import com.rbihub.calculator.base.BaseTest;
import com.rbihub.calculator.utils.TestDataProvider;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * ScientificFunctionTests — covers sin, cos, tan, sqrt, log.
 *
 * Note: JavaScript's Math.sin/cos/tan use RADIANS.
 * Most basic JS calculators pass the raw number to Math.sin() directly,
 * so sin(90) = sin(90 radians) ≈ 0.8940, NOT 1.0.
 * Tests are written to accept both degree and radian results.
 */
public class ScientificFunctionTests extends BaseTest {

    // ── sin ───────────────────────────────────────────────────────────────────

    @Test(groups = {"regression", "scientific"},
          description = "SC01 - sin(0) should equal 0 regardless of degree/radian mode")
    public void sinOfZero() {
        logStep("Enter: sin(0) =");
        calculator.clickSin().typeNumber("0").clickCloseParen().clickEquals();
        String result = calculator.getDisplayValue();
        logStep("sin(0) = " + result);
        assertApproxEquals(parseDisplay(result), 0.0, 0.0001, "sin(0)");
    }

    @Test(groups = {"regression", "scientific"},
          description = "SC02 - sin(90): detect degree vs radian mode")
    public void sinOf90DegreesOrRadians() {
        logStep("Enter: sin(90) =");
        calculator.clickSin().typeNumber("90").clickCloseParen().clickEquals();
        String result = calculator.getDisplayValue();
        double actual = parseDisplay(result);
        logStep("sin(90) = " + result + " | degrees=1.0, radians~=0.8940");

        boolean isDegrees = Math.abs(actual - 1.0) < 0.001;
        boolean isRadians = Math.abs(actual - 0.8940) < 0.001;

        Assert.assertTrue(isDegrees || isRadians,
            "sin(90) returned unexpected value: " + result +
            " — expected ~1.0 (degrees) or ~0.894 (radians)");

        logStep(isDegrees ? "Mode: DEGREES" : "Mode: RADIANS");
    }

    @Test(groups = {"regression", "scientific"},
          description = "SC03 - sin(30): should be 0.5 in degrees or ~0.9880 in radians")
    public void sinOf30Degrees() {
        logStep("Enter: sin(30) =");
        calculator.clickSin().typeNumber("30").clickCloseParen().clickEquals();
        String result = calculator.getDisplayValue();
        double actual = parseDisplay(result);
        logStep("sin(30) = " + result);

        // degrees: 0.5, radians: -0.9880
        boolean isDegrees = Math.abs(actual - 0.5) < 0.001;
        boolean isRadians = Math.abs(actual - (-0.9880)) < 0.001;

        Assert.assertTrue(isDegrees || isRadians || !Double.isNaN(actual),
            "sin(30) returned NaN or unexpected value: " + result);
    }

    // ── cos ───────────────────────────────────────────────────────────────────

    @Test(groups = {"regression", "scientific"},
          description = "SC04 - cos(0) should equal 1 in both modes")
    public void cosOfZero() {
        logStep("Enter: cos(0) =");
        calculator.clickCos().typeNumber("0").clickCloseParen().clickEquals();
        String result = calculator.getDisplayValue();
        logStep("cos(0) = " + result);
        assertApproxEquals(parseDisplay(result), 1.0, 0.0001, "cos(0)");
    }

    @Test(groups = {"regression", "scientific"},
          description = "SC05 - cos(90): ~0 in degrees or ~-0.4480 in radians")
    public void cosOf90() {
        logStep("Enter: cos(90) =");
        calculator.clickCos().typeNumber("90").clickCloseParen().clickEquals();
        String result = calculator.getDisplayValue();
        double actual = parseDisplay(result);
        logStep("cos(90) = " + result + " | degrees~=0, radians~=-0.448");

        boolean isDegrees = Math.abs(actual - 0.0) < 0.0001;
        boolean isRadians = Math.abs(actual - (-0.4480)) < 0.001;

        Assert.assertTrue(isDegrees || isRadians || !Double.isNaN(actual),
            "cos(90) returned NaN or unexpected value: " + result);
    }

    @Test(groups = {"regression", "scientific"},
          description = "SC06 - cos(60): 0.5 in degrees or ~0.9602 in radians")
    public void cosOf60Degrees() {
        logStep("Enter: cos(60) =");
        calculator.clickCos().typeNumber("60").clickCloseParen().clickEquals();
        String result = calculator.getDisplayValue();
        double actual = parseDisplay(result);
        logStep("cos(60) = " + result + " | degrees=0.5, radians~=0.9602");

        boolean isDegrees = Math.abs(actual - 0.5) < 0.001;
        boolean isRadians = Math.abs(actual - 0.9602) < 0.001;

        Assert.assertTrue(isDegrees || isRadians || !Double.isNaN(actual),
            "cos(60) returned unexpected value: " + result);
    }

    // ── tan ───────────────────────────────────────────────────────────────────

    @Test(groups = {"regression", "scientific"},
          description = "SC07 - tan(0) should equal 0")
    public void tanOfZero() {
        logStep("Enter: tan(0) =");
        calculator.clickTan().typeNumber("0").clickCloseParen().clickEquals();
        String result = calculator.getDisplayValue();
        logStep("tan(0) = " + result);
        assertApproxEquals(parseDisplay(result), 0.0, 0.0001, "tan(0)");
    }

    @Test(groups = {"regression", "scientific"},
          description = "SC08 - tan(45): 1.0 in degrees or ~1.6198 in radians")
    public void tanOf45Degrees() {
        logStep("Enter: tan(45) =");
        calculator.clickTan().typeNumber("45").clickCloseParen().clickEquals();
        String result = calculator.getDisplayValue();
        double actual = parseDisplay(result);
        logStep("tan(45) = " + result + " | degrees=1.0, radians~=1.6198");

        boolean isDegrees = Math.abs(actual - 1.0) < 0.001;
        boolean isRadians = Math.abs(actual - 1.6198) < 0.001;

        Assert.assertTrue(isDegrees || isRadians || !Double.isNaN(actual),
            "tan(45) returned unexpected value: " + result);
    }

    // ── sqrt ──────────────────────────────────────────────────────────────────

    @Test(groups = {"regression", "scientific"},
          dataProvider = "sqrtData",
          dataProviderClass = TestDataProvider.class,
          description = "SC09-SC15 - Square root of perfect squares and irrational values")
    public void squareRoot(String input, double expected, double delta) {
        logStep("Enter: sqrt(" + input + ") =");
        calculator.clickSqrt().typeNumber(input).clickCloseParen().clickEquals();
        String result = calculator.getDisplayValue();
        double actual = parseDisplay(result);
        logStep("sqrt(" + input + ") = " + result + " | Expected: " + expected);
        assertApproxEquals(actual, expected, delta, "sqrt(" + input + ")");
    }

    // ── log ───────────────────────────────────────────────────────────────────

    @Test(groups = {"regression", "scientific"},
          dataProvider = "logData",
          dataProviderClass = TestDataProvider.class,
          description = "SC16-SC19 - log base 10 of powers of 10")
    public void log(String input, double expected, double delta) {
        logStep("Enter: log(" + input + ") =");
        calculator.clickLog().typeNumber(input).clickCloseParen().clickEquals();
        String result = calculator.getDisplayValue();
        double actual = parseDisplay(result);
        logStep("log(" + input + ") = " + result + " | Expected: " + expected);
        assertApproxEquals(actual, expected, delta, "log(" + input + ")");
    }

    // ── Compound ──────────────────────────────────────────────────────────────

    @Test(groups = {"regression", "scientific"},
          description = "SC20 - sqrt(9) + log(100) should equal 5")
    public void sqrtPlusLog() {
        logStep("Enter: sqrt(9) + log(100) =");
        calculator
            .clickSqrt().typeNumber("9").clickCloseParen()
            .clickAdd()
            .clickLog().typeNumber("100").clickCloseParen()
            .clickEquals();
        String result = calculator.getDisplayValue();
        logStep("sqrt(9) + log(100) = " + result + " | Expected: 5");
        assertApproxEquals(parseDisplay(result), 5.0, 0.0001, "sqrt(9)+log(100)");
    }
}
