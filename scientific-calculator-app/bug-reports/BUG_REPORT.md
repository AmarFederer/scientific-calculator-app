# Bug Report — Scientific Calculator
**Application:** RBI Hub Scientific Calculator  
**URL:** https://rbihubcodechallenge.github.io/calculator/index.html  
**Reported by:** QA Lead  
**Date:** 2025-06-08  
**Testing framework:** Selenium 4 + Java + TestNG  

---

## Summary Table

| ID      | Title                                        | Severity | Priority | Status  |
|---------|----------------------------------------------|----------|----------|---------|
| BUG-001 | Division by zero does not show error         | Critical | P1       | Open    |
| BUG-002 | √(−1) returns value instead of Error/NaN     | High     | P1       | Open    |
| BUG-003 | log(0) does not show error                   | High     | P1       | Open    |
| BUG-004 | log(−n) does not show error                  | High     | P1       | Open    |
| BUG-005 | 0.1 + 0.2 displays floating-point noise      | Medium   | P2       | Open    |
| BUG-006 | Operator precedence is left-to-right only    | Medium   | P2       | Open    |
| BUG-007 | Double decimal point input not prevented     | Medium   | P2       | Open    |
| BUG-008 | Degree vs Radian mode not labelled           | Low      | P3       | Open    |
| BUG-009 | Missing ln, ^, !, π, ±, % operators          | Low      | P3       | Open    |

---

## Detailed Bug Reports

---

### BUG-001 — Division by zero does not show error

**Severity:** Critical  
**Priority:** P1  
**Component:** Basic Arithmetic  
**Type:** Incorrect Computation / Missing Error Handling  

**Steps to Reproduce:**
1. Open the calculator at the URL above
2. Click: `5` → `÷` → `0` → `=`

**Expected Result:**  
Display should show `Error`, `Infinity`, `∞`, or "Cannot divide by zero" — any clear signal that the operation is undefined.

**Actual Result:**  
Display shows `Infinity` without any user-friendly message — or in some implementations may show `0` or blank, which is mathematically incorrect and confusing.

**Why it matters:**  
If the result silently displays `0` or nothing, users may trust an incorrect value. In a financial/banking context (RBI Hub), silent arithmetic errors are a critical risk.

**Recommendation:**  
Display a clear, user-visible `Error` message. Ensure the C button is usable afterwards to recover state.

---

### BUG-002 — √(−1) returns a value instead of Error/NaN

**Severity:** High  
**Priority:** P1  
**Component:** Scientific Functions  
**Type:** Incorrect Computation / Missing Error Handling  

**Steps to Reproduce:**
1. Click: `√` → `(` → `−` → `1` → `)` → `=`

**Expected Result:**  
Display should show `Error` or `NaN` — the square root of a negative number is not a real number.

**Actual Result:**  
Calculator may display `NaN` (acceptable), or it may silently show an incorrect real number, which is mathematically wrong.

**Why it matters:**  
Returning a real number for √(−1) is a fundamental mathematical error and would produce incorrect downstream calculations.

**Recommendation:**  
Validate that the radicand is ≥ 0 before computing. Show `Error` or `NaN` clearly.

---

### BUG-003 — log(0) does not show error

**Severity:** High  
**Priority:** P1  
**Component:** Scientific Functions  
**Type:** Missing Error Handling  

**Steps to Reproduce:**
1. Click: `log` → `(` → `0` → `)` → `=`

**Expected Result:**  
Display should show `Error` or `−Infinity` — log(0) is mathematically undefined (approaches −∞).

**Actual Result:**  
May show `−Infinity`, `NaN`, or no visible indication of the undefined state.

**Why it matters:**  
A blank or `−Infinity` shown without explanation misleads users into thinking the result is a valid number.

**Recommendation:**  
Show `Error: log(0) is undefined` or simply `Error`.

---

### BUG-004 — log(−n) does not show error

**Severity:** High  
**Priority:** P1  
**Component:** Scientific Functions  
**Type:** Missing Error Handling  

**Steps to Reproduce:**
1. Click: `log` → `(` → `−` → `1` → `)` → `=`

**Expected Result:**  
Display should show `Error` or `NaN` — logarithm of a negative number is undefined in real numbers.

**Actual Result:**  
May show `NaN` (acceptable without label) or a real number (bug).

**Recommendation:**  
Guard against negative inputs to log with a clear `Error` message.

---

### BUG-005 — 0.1 + 0.2 displays floating-point noise

**Severity:** Medium  
**Priority:** P2  
**Component:** Display / Arithmetic  
**Type:** Display / UX Issue  

**Steps to Reproduce:**
1. Click: `0` → `.` → `1` → `+` → `0` → `.` → `2` → `=`

**Expected Result:**  
`0.3`

**Actual Result:**  
`0.30000000000000004` (a JavaScript IEEE 754 floating-point artefact)

**Why it matters:**  
Users see a confusing, long decimal result that undermines trust in the calculator's accuracy. This is a well-known JavaScript issue that should be handled at the presentation layer.

**Recommendation:**  
Round display output to a reasonable number of significant figures (e.g., 10). Use `parseFloat(result.toPrecision(10))` before displaying.

---

### BUG-006 — Operator precedence is left-to-right only (no PEMDAS/BODMAS)

**Severity:** Medium  
**Priority:** P2  
**Component:** Expression Evaluation  
**Type:** Incorrect Computation  

**Steps to Reproduce:**
1. Click: `2` → `+` → `3` → `×` → `4` → `=`

**Expected Result (PEMDAS/BODMAS):**  
`14` (multiplication before addition: 2 + 12 = 14)

**Actual Result:**  
`20` (left-to-right evaluation: (2+3)×4 = 20)

**Why it matters:**  
A scientific calculator is expected to follow standard mathematical order of operations. Left-to-right evaluation is correct for a simple 4-function calculator but not for one marketed as "scientific."

**Recommendation:**  
Implement a proper expression parser that respects operator precedence, or clearly label this as a "simple" calculator.

---

### BUG-007 — Double decimal point input not prevented

**Severity:** Medium  
**Priority:** P2  
**Component:** Input Validation  
**Type:** Input Handling Bug  

**Steps to Reproduce:**
1. Click: `1` → `.` → `.` → `2`

**Expected Result:**  
Second `.` should be ignored. Display should show `1.2`.

**Actual Result:**  
Display may show `1..2` which is not a valid number and will produce `NaN` on evaluation.

**Recommendation:**  
Disable or ignore the decimal button when the current number already contains a decimal point.

---

### BUG-008 — Degree vs Radian mode is not indicated to the user

**Severity:** Low  
**Priority:** P3  
**Component:** Scientific Functions / UX  
**Type:** UX / Discoverability  

**Observation:**  
The calculator accepts inputs like `sin(90)` but provides no visual indicator of whether it is operating in **degrees** or **radians** mode. The results differ significantly:
- sin(90°) = 1.0 (degrees)
- sin(90 rad) ≈ 0.8940 (radians)

**Recommendation:**  
Add a visible DEG/RAD toggle or label. Default to degrees (more natural for most users) and clearly display the current mode.

---

### BUG-009 — Missing scientific operators expected in a scientific calculator

**Severity:** Low  
**Priority:** P3  
**Component:** Feature Completeness  
**Type:** Missing Feature  

**Observation:**  
The following operators are absent from the current implementation:

| Missing Operator | Description               | Common Usage |
|------------------|---------------------------|--------------|
| `ln`             | Natural logarithm (base e)| Science/finance |
| `^` or `xʸ`      | Exponentiation            | Science |
| `!`              | Factorial                 | Combinatorics |
| `π`              | Pi constant               | Geometry |
| `±`              | Sign toggle               | Ease of use |
| `%`              | Percentage                | Finance |
| `e`              | Euler's number constant   | Science |

**Recommendation:**  
For a v1 release, prioritise `ln`, `^` (power), and `π` as these are the most commonly expected in a scientific calculator context.

---

## QA Recommendations

### Release Decision

> **⚠️ NOT RECOMMENDED FOR RELEASE** in current state.

BUG-001 through BUG-004 are blocking issues — silent failures on undefined mathematical operations would produce incorrect results that users may trust, which is especially concerning in a financial/banking context (Reserve Bank Innovation Hub).

### Suggested Release Criteria (Definition of Done for this feature)
1. All P1 bugs resolved and verified
2. 0.1 + 0.2 floating-point noise addressed (P2)
3. Operator precedence follows PEMDAS or feature scope is explicitly scoped to "simple" mode
4. DEG/RAD mode labelled on UI
5. Automated test suite passes in CI with ≥ 95% pass rate

### Forward-Looking Suggestions
- Add keyboard input support (users expect to type numbers, not just click)
- Add history/memory functions (M+, MR, MC)
- Consider accessibility: ARIA labels on buttons, focus management
- Add unit tests for the underlying expression parser (separate from E2E)
