package com.rbihub.calculator.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Captures screenshots on test failure.
 * Screenshots are saved under test-output/screenshots/.
 */
public class ScreenshotUtils {

    private static final Logger log = LoggerFactory.getLogger(ScreenshotUtils.class);
    private static final String SCREENSHOT_DIR = "test-output/screenshots/";
    private static final DateTimeFormatter TIMESTAMP_FMT =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private ScreenshotUtils() { /* utility class */ }

    /**
     * Captures a screenshot and returns the absolute file path.
     *
     * @param driver   active WebDriver instance
     * @param testName name of the failing test (used in filename)
     * @return absolute path of the saved screenshot, or null on failure
     */
    public static String captureScreenshot(WebDriver driver, String testName) {
        try {
            // Ensure directory exists
            Path screenshotDir = Paths.get(SCREENSHOT_DIR);
            Files.createDirectories(screenshotDir);

            // Build filename
            String timestamp = LocalDateTime.now().format(TIMESTAMP_FMT);
            String sanitized = testName.replaceAll("[^a-zA-Z0-9_\\-]", "_");
            String filename  = sanitized + "_" + timestamp + ".png";
            Path   filepath  = screenshotDir.resolve(filename);

            // Take and save
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(src.toPath(), filepath);

            log.info("Screenshot saved: {}", filepath.toAbsolutePath());
            return filepath.toAbsolutePath().toString();

        } catch (IOException e) {
            log.error("Failed to capture screenshot for test '{}': {}", testName, e.getMessage());
            return null;
        }
    }
}
