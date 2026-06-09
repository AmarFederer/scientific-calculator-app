package com.rbihub.calculator.base;

import com.aventstack.extentreports.Status;
import com.rbihub.calculator.pages.CalculatorPage;
import com.rbihub.calculator.utils.DriverFactory;
import com.rbihub.calculator.utils.ExtentReportManager;
import com.rbihub.calculator.utils.ScreenshotUtils;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.time.Duration;

/**
 * BaseTest — parent for all test classes.
 */
public class BaseTest {

    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    protected WebDriver driver;
    protected CalculatorPage calculator;

    // ── Suite-level lifecycle ────────────────────────────────────────────────

    @BeforeSuite(alwaysRun = true)
    public void initReport() {
        ExtentReportManager.init();
        log.info("ExtentReports initialised");
    }

    @AfterSuite(alwaysRun = true)
    public void flushReport() {
        ExtentReportManager.flush();
        log.info("ExtentReports flushed to test-output/ExtentReport.html");
    }

    // ── Test-level lifecycle ─────────────────────────────────────────────────

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser", "baseUrl"})
    public void setUp(
            @Optional("chrome") String browser,
            @Optional("https://rbihubcodechallenge.github.io/calculator/index.html") String baseUrl,
            ITestResult result) {

        // Start extent test node
        String testName = result.getMethod().getMethodName();
        String desc     = result.getMethod().getDescription();
        ExtentReportManager.createTest(testName, desc != null ? desc : "");

        // Boot driver and open page
        driver = DriverFactory.createDriver(browser);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();

        calculator = new CalculatorPage(driver);
        calculator.open(baseUrl);

        log.info("=== START: {} ===", testName);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        if (result.getStatus() == ITestResult.SUCCESS) {
            ExtentReportManager.getTest().log(Status.PASS, "Test PASSED");
            log.info("=== PASS: {} ===", testName);
        } else if (result.getStatus() == ITestResult.FAILURE) {
            String screenshotPath = ScreenshotUtils.captureScreenshot(driver, testName);
            if (screenshotPath != null) {
                ExtentReportManager.getTest()
                        .addScreenCaptureFromPath(screenshotPath, "Failure Screenshot");
            }
            Throwable throwable = result.getThrowable();
            ExtentReportManager.getTest().log(Status.FAIL,
                    "Test FAILED: " + (throwable != null ? throwable.getMessage() : "unknown"));
            log.error("=== FAIL: {} - {} ===", testName,
                    throwable != null ? throwable.getMessage() : "unknown");
        } else if (result.getStatus() == ITestResult.SKIP) {
            ExtentReportManager.getTest().log(Status.SKIP, "Test SKIPPED");
            log.warn("=== SKIP: {} ===", testName);
        }

        if (driver != null) {
            driver.quit();
        }
    }

    // ── Shared helpers ────────────────────────────────────────────────────────

    protected void logStep(String message) {
        ExtentReportManager.getTest().log(Status.INFO, message);
        log.info("  STEP: {}", message);
    }

    protected double parseDisplay(String value) {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    protected void assertApproxEquals(double actual, double expected, double delta, String message) {
        if (Math.abs(actual - expected) > delta) {
            throw new AssertionError(
                String.format("%s - expected ~%.6f but got %.6f (delta=%.6f)",
                        message, expected, actual, delta)
            );
        }
    }
}
