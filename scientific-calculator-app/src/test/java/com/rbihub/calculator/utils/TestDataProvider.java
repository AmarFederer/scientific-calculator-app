package com.rbihub.calculator.utils;

import org.testng.annotations.DataProvider;

/**
 * Centralised test data for data-driven tests.
 * Each row is: { expression description, expected result as String }
 */
public class TestDataProvider {

    private TestDataProvider() { /* static utility */ }

    // ── Basic Arithmetic DataProvider ─────────────────────────────────────────

    /**
     * Provides rows of: { operand1, operator, operand2, expectedResult }
     * Used by BasicArithmeticTests to drive table-based scenarios.
     */
    @DataProvider(name = "additionData")
    public static Object[][] additionData() {
        return new Object[][] {
            // operand1, operand2, expected
            { "1",   "2",   "3"   },
            { "0",   "0",   "0"   },
            { "9",   "1",   "10"  },
            { "5",   "5",   "10"  },
            { "99",  "1",   "100" },
            { "0.5", "0.5", "1"   },
            { "1.5", "2.5", "4"   },
        };
    }

    @DataProvider(name = "subtractionData")
    public static Object[][] subtractionData() {
        return new Object[][] {
            { "5",   "3",   "2"    },
            { "0",   "0",   "0"    },
            { "10",  "1",   "9"    },
            { "3",   "9",   "-6"   },
            { "1.5", "0.5", "1"    },
        };
    }

    @DataProvider(name = "multiplicationData")
    public static Object[][] multiplicationData() {
        return new Object[][] {
            { "6",   "7",   "42"   },
            { "0",   "999", "0"    },
            { "1",   "1",   "1"    },
            { "3",   "3",   "9"    },
            { "12",  "12",  "144"  },
            { "0.5", "2",   "1"    },
        };
    }

    @DataProvider(name = "divisionData")
    public static Object[][] divisionData() {
        return new Object[][] {
            { "8",   "2",   "4"    },
            { "9",   "3",   "3"    },
            { "1",   "1",   "1"    },
            { "10",  "4",   "2.5"  },
            { "0",   "5",   "0"    },
            { "7",   "2",   "3.5"  },
        };
    }

    // ── Scientific function test data ─────────────────────────────────────────

    @DataProvider(name = "sqrtData")
    public static Object[][] sqrtData() {
        return new Object[][] {
            // input, expected, delta
            { "0",   0.0,       0.0001 },
            { "1",   1.0,       0.0001 },
            { "4",   2.0,       0.0001 },
            { "9",   3.0,       0.0001 },
            { "16",  4.0,       0.0001 },
            { "25",  5.0,       0.0001 },
            { "2",   1.414213,  0.0001 },
        };
    }

    @DataProvider(name = "logData")
    public static Object[][] logData() {
        return new Object[][] {
            // input, expected (log base 10), delta
            { "1",    0.0,       0.0001 },
            { "10",   1.0,       0.0001 },
            { "100",  2.0,       0.0001 },
            { "1000", 3.0,       0.0001 },
        };
    }
}
