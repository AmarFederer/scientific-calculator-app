package com.rbihub.calculator.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DriverFactory — centralized WebDriver creation.
 *
 * Supports Chrome (default) and Firefox.
 * Headless mode is enabled automatically when the 'headless' system property is true,
 * or when running in CI (detected via CI env var).
 */
public class DriverFactory {

    private static final Logger log = LoggerFactory.getLogger(DriverFactory.class);

    private DriverFactory() { /* utility class */ }

    public static WebDriver createDriver(String browser) {
        boolean headless = isHeadless();
        log.info("Creating {} driver | headless={}", browser, headless);

        if ("firefox".equalsIgnoreCase(browser)) {
            return createFirefoxDriver(headless);
        } else {
            return createChromeDriver(headless);
        }
    }

    // ── Chrome ───────────────────────────────────────────────────────────────

    private static WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        if (headless) {
            options.addArguments("--headless=new");
        }

        // Stability flags for CI / Docker environments
        options.addArguments(
            "--no-sandbox",
            "--disable-dev-shm-usage",
            "--disable-gpu",
            "--window-size=1920,1080",
            "--disable-extensions",
            "--disable-infobars",
            "--remote-allow-origins=*"
        );

        return new ChromeDriver(options);
    }

    // ── Firefox ───────────────────────────────────────────────────────────────

    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();

        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("--headless");
        }

        return new FirefoxDriver(options);
    }

    // ── Headless detection ────────────────────────────────────────────────────

    /**
     * Returns true if:
     * - System property 'headless' is set to 'true', OR
     * - CI environment variable is set (GitHub Actions, Jenkins, CircleCI, etc.)
     */
    private static boolean isHeadless() {
        String prop = System.getProperty("headless", "false");
        boolean systemPropHeadless = Boolean.parseBoolean(prop);

        // Standard CI environment variable set by most CI providers
        boolean inCi = System.getenv("CI") != null;

        return systemPropHeadless || inCi;
    }
}
