package com.rbihub.calculator.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

/**
 * Manages the ExtentReports singleton and per-test ExtentTest instances.
 *
 * Usage:
 *   ExtentReportManager.init()           — call once in @BeforeSuite
 *   ExtentReportManager.createTest(name) — call in @BeforeMethod
 *   ExtentReportManager.getTest()        — call in test / listener to log steps
 *   ExtentReportManager.flush()          — call in @AfterSuite to write the file
 */
public class ExtentReportManager {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();

    private static final String REPORT_PATH = "test-output/ExtentReport.html";

    private ExtentReportManager() { /* static utility */ }

    // ── Lifecycle ────────────────────────────────────────────────────────────

    public static void init() {
        ExtentSparkReporter spark = new ExtentSparkReporter(REPORT_PATH);
        spark.config().setTheme(Theme.DARK);
        spark.config().setDocumentTitle("Scientific Calculator — QA Test Report");
        spark.config().setReportName("RBI Hub | Scientific Calculator | E2E Results");
        spark.config().setEncoding("UTF-8");

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Application",   "Scientific Calculator");
        extent.setSystemInfo("URL",           "https://rbihubcodechallenge.github.io/calculator/index.html");
        extent.setSystemInfo("Framework",     "Selenium 4 + TestNG + ExtentReports");
        extent.setSystemInfo("Author",        "QA Lead");
        extent.setSystemInfo("Java Version",  System.getProperty("java.version"));
    }

    public static void flush() {
        if (extent != null) {
            extent.flush();
        }
    }

    // ── Per-test ─────────────────────────────────────────────────────────────

    public static ExtentTest createTest(String testName) {
        ExtentTest test = extent.createTest(testName);
        testThread.set(test);
        return test;
    }

    public static ExtentTest createTest(String testName, String description) {
        ExtentTest test = extent.createTest(testName, description);
        testThread.set(test);
        return test;
    }

    public static ExtentTest getTest() {
        return testThread.get();
    }
}
