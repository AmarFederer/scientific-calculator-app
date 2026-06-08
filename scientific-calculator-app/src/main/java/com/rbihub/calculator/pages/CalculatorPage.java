package com.rbihub.calculator.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

/**
 * Page Object Model for the Scientific Calculator.
 * URL: https://rbihubcodechallenge.github.io/calculator/index.html
 */
public class CalculatorPage {

    private static final Logger log = LoggerFactory.getLogger(CalculatorPage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ── Display locators — try multiple strategies ───────────────────────────
    // The calculator may have a single display or separate expression/result elements
    private static final By DISPLAY           = By.id("display");
    private static final By DISPLAY_INPUT     = By.cssSelector("input#display");
    private static final By DISPLAY_ANY_INPUT = By.tagName("input");
    private static final By RESULT_DISPLAY    = By.className("result");
    private static final By SCREEN            = By.className("screen");
    private static final By CALC_DISPLAY      = By.className("display");

    // ── Button locators ───────────────────────────────────────────────────────
    private static final By BTN_0 = By.xpath("//button[normalize-space(text())='0']");
    private static final By BTN_1 = By.xpath("//button[normalize-space(text())='1']");
    private static final By BTN_2 = By.xpath("//button[normalize-space(text())='2']");
    private static final By BTN_3 = By.xpath("//button[normalize-space(text())='3']");
    private static final By BTN_4 = By.xpath("//button[normalize-space(text())='4']");
    private static final By BTN_5 = By.xpath("//button[normalize-space(text())='5']");
    private static final By BTN_6 = By.xpath("//button[normalize-space(text())='6']");
    private static final By BTN_7 = By.xpath("//button[normalize-space(text())='7']");
    private static final By BTN_8 = By.xpath("//button[normalize-space(text())='8']");
    private static final By BTN_9 = By.xpath("//button[normalize-space(text())='9']");

    private static final By BTN_ADD      = By.xpath("//button[normalize-space(text())='+']");
    private static final By BTN_SUBTRACT = By.xpath("//button[normalize-space(text())='-' or normalize-space(text())='−']");
    private static final By BTN_MULTIPLY = By.xpath("//button[normalize-space(text())='×' or normalize-space(text())='*' or normalize-space(text())='x']");
    private static final By BTN_DIVIDE   = By.xpath("//button[normalize-space(text())='÷' or normalize-space(text())='/']");
    private static final By BTN_EQUALS   = By.xpath("//button[normalize-space(text())='=']");
    private static final By BTN_DECIMAL  = By.xpath("//button[normalize-space(text())='.']");
    private static final By BTN_CLEAR    = By.xpath("//button[normalize-space(text())='C' or normalize-space(text())='AC' or normalize-space(text())='CE']");
    private static final By BTN_OPEN_PAREN  = By.xpath("//button[normalize-space(text())='(']");
    private static final By BTN_CLOSE_PAREN = By.xpath("//button[normalize-space(text())=')']");

    private static final By BTN_SIN  = By.xpath("//button[normalize-space(text())='sin']");
    private static final By BTN_COS  = By.xpath("//button[normalize-space(text())='cos']");
    private static final By BTN_TAN  = By.xpath("//button[normalize-space(text())='tan']");
    private static final By BTN_SQRT = By.xpath("//button[normalize-space(text())='√' or normalize-space(text())='sqrt']");
    private static final By BTN_LOG  = By.xpath("//button[normalize-space(text())='log']");

    // ── Constructor ───────────────────────────────────────────────────────────

    public CalculatorPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    public CalculatorPage open(String url) {
        log.info("Opening calculator: {}", url);
        driver.get(url);
        wait.until(ExpectedConditions.visibilityOfElementLocated(BTN_CLEAR));
        return this;
    }

    // ── Display reading ───────────────────────────────────────────────────────

    /**
     * Returns the result value shown on the display.
     * Tries multiple strategies to handle different calculator implementations:
     * 1. input#display value attribute
     * 2. id="display" text content
     * 3. Any visible input on the page
     * 4. Common CSS class names used in calculator UIs
     * After pressing =, extracts only the numeric result portion.
     */
    public String getDisplayValue() {
        String value = "";

        // Strategy 1: input with id="display"
        try {
            List<WebElement> inputs = driver.findElements(DISPLAY_INPUT);
            if (!inputs.isEmpty() && inputs.get(0).isDisplayed()) {
                value = inputs.get(0).getAttribute("value");
                if (value == null || value.isEmpty()) {
                    value = inputs.get(0).getText();
                }
            }
        } catch (Exception ignored) {}

        // Strategy 2: element with id="display" (div/span/p)
        if (value == null || value.isEmpty()) {
            try {
                List<WebElement> els = driver.findElements(DISPLAY);
                if (!els.isEmpty() && els.get(0).isDisplayed()) {
                    value = els.get(0).getText();
                    if (value == null || value.isEmpty()) {
                        value = els.get(0).getAttribute("value");
                    }
                }
            } catch (Exception ignored) {}
        }

        // Strategy 3: any visible input on page
        if (value == null || value.isEmpty()) {
            try {
                List<WebElement> inputs = driver.findElements(DISPLAY_ANY_INPUT);
                for (WebElement el : inputs) {
                    if (el.isDisplayed()) {
                        value = el.getAttribute("value");
                        if (value != null && !value.isEmpty()) break;
                    }
                }
            } catch (Exception ignored) {}
        }

        // Strategy 4: common class names
        if (value == null || value.isEmpty()) {
            By[] fallbacks = { RESULT_DISPLAY, SCREEN, CALC_DISPLAY };
            for (By locator : fallbacks) {
                try {
                    List<WebElement> els = driver.findElements(locator);
                    if (!els.isEmpty() && els.get(0).isDisplayed()) {
                        value = els.get(0).getText();
                        if (value != null && !value.isEmpty()) break;
                    }
                } catch (Exception ignored) {}
            }
        }

        if (value == null) value = "";

        // Extract result: if display shows "2+3=5", return "5"
        // If display shows "5", return "5"
        value = extractResult(value.trim());

        log.debug("Display value: '{}'", value);
        return value;
    }

    /**
     * If the display shows a full expression like "2+3=5" or "2+3\n5",
     * extract only the result part after = or the last line.
     */
    private String extractResult(String raw) {
        if (raw == null || raw.isEmpty()) return raw;

        // Case: "2+3=5" — take everything after last =
        if (raw.contains("=")) {
            String afterEquals = raw.substring(raw.lastIndexOf("=") + 1).trim();
            if (!afterEquals.isEmpty()) {
                return afterEquals;
            }
        }

        // Case: multi-line display — take last non-empty line
        String[] lines = raw.split("\\n");
        for (int i = lines.length - 1; i >= 0; i--) {
            String line = lines[i].trim();
            if (!line.isEmpty()) {
                return line;
            }
        }

        return raw;
    }

    /**
     * Returns true if display shows an error state.
     */
    public boolean isDisplayShowingError() {
        String display = getDisplayValue().toLowerCase();
        return display.contains("error")
            || display.contains("nan")
            || display.contains("infinity")
            || display.contains("undefined")
            || display.contains("∞")
            || display.contains("invalid");
    }

    // ── Click helpers ─────────────────────────────────────────────────────────

    private void click(By locator) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        el.click();
    }

    // ── Digit methods ─────────────────────────────────────────────────────────

    public CalculatorPage clickDigit(int digit) {
        if (digit == 0)      click(BTN_0);
        else if (digit == 1) click(BTN_1);
        else if (digit == 2) click(BTN_2);
        else if (digit == 3) click(BTN_3);
        else if (digit == 4) click(BTN_4);
        else if (digit == 5) click(BTN_5);
        else if (digit == 6) click(BTN_6);
        else if (digit == 7) click(BTN_7);
        else if (digit == 8) click(BTN_8);
        else if (digit == 9) click(BTN_9);
        else throw new IllegalArgumentException("Invalid digit: " + digit);
        return this;
    }

    public CalculatorPage typeNumber(String number) {
        log.debug("Typing: {}", number);
        for (char c : number.toCharArray()) {
            if (c >= '0' && c <= '9')  clickDigit(Character.getNumericValue(c));
            else if (c == '.')         clickDecimal();
            else if (c == '-')         clickSubtract();
            else throw new IllegalArgumentException("Unsupported char: " + c);
        }
        return this;
    }

    // ── Operators ─────────────────────────────────────────────────────────────

    public CalculatorPage clickAdd()         { click(BTN_ADD);         return this; }
    public CalculatorPage clickSubtract()    { click(BTN_SUBTRACT);    return this; }
    public CalculatorPage clickMultiply()    { click(BTN_MULTIPLY);    return this; }
    public CalculatorPage clickDivide()      { click(BTN_DIVIDE);      return this; }
    public CalculatorPage clickEquals()      { click(BTN_EQUALS);      return this; }
    public CalculatorPage clickDecimal()     { click(BTN_DECIMAL);     return this; }
    public CalculatorPage clickClear()       { click(BTN_CLEAR);       return this; }
    public CalculatorPage clickOpenParen()   { click(BTN_OPEN_PAREN);  return this; }
    public CalculatorPage clickCloseParen()  { click(BTN_CLOSE_PAREN); return this; }

    // ── Scientific ────────────────────────────────────────────────────────────

    public CalculatorPage clickSin()  { click(BTN_SIN);  return this; }
    public CalculatorPage clickCos()  { click(BTN_COS);  return this; }
    public CalculatorPage clickTan()  { click(BTN_TAN);  return this; }
    public CalculatorPage clickSqrt() { click(BTN_SQRT); return this; }
    public CalculatorPage clickLog()  { click(BTN_LOG);  return this; }

    // ── Visibility checks ─────────────────────────────────────────────────────

    public boolean isButtonVisible(By locator) {
        try { return driver.findElement(locator).isDisplayed(); }
        catch (Exception e) { return false; }
    }

    // ── Expose locators ───────────────────────────────────────────────────────

    public By getDisplayLocator()      { return DISPLAY; }
    public By getClearButtonLocator()  { return BTN_CLEAR; }
    public By getSinButtonLocator()    { return BTN_SIN; }
    public By getCosButtonLocator()    { return BTN_COS; }
    public By getTanButtonLocator()    { return BTN_TAN; }
    public By getSqrtButtonLocator()   { return BTN_SQRT; }
    public By getLogButtonLocator()    { return BTN_LOG; }
    public By getEqualsButtonLocator() { return BTN_EQUALS; }
    public By getAddButtonLocator()    { return BTN_ADD; }
    public By getDivideButtonLocator() { return BTN_DIVIDE; }
}
