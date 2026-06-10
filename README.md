# Selenium UI Automation Framework

A robust, thread-safe, and parallel-ready automated UI testing framework built on **Java 21** using **Selenium WebDriver**, **TestNG**, **Log4j2**, and **ExtentReports**. This framework demonstrates a production-grade implementation of the Page Object Model (POM) pattern, targeting Amazon's search-to-cart workflows with advanced handling of dynamic overlays, location changes, and resilient element locators. Supports both **local browser** and **LambdaTest cloud grid** execution.

---

## Key Features

*   **Thread-Safe Parallel Execution:** Utilizes dynamic browser allocation wrapped in `ThreadLocal<WebDriver>` within the custom `DriverManager`. This enables thread-safe method-level parallel execution without test cross-talk.
*   **Dual Execution Mode:** Seamlessly switch between local browser and LambdaTest cloud grid execution via a single command-line flag — no code changes required.
*   **Robust Driver Factory:** `DriverFactory` initializes Chrome, Firefox, or Edge dynamically with anti-bot evasion settings (e.g., custom user-agent, remote origin access, window sizing, and disabled automation control signatures).
*   **Explicit Waiting Engine:** Implements `ElementUtils` to wrap standard Selenium interactions with `WebDriverWait`, drastically minimizing test flakiness.
*   **Dynamic Overlay & Popup Dismissal:** Automatically handles and dismisses unexpected protection/warranty upsell screens and side sheets during checkout/cart actions.
*   **Resilient Price Extraction:** Leverages a locator priority list to extract item prices dynamically, coping with volatile Amazon structural changes.
*   **Rich Dark-Theme Reporting:** Generates interactive ExtentReports including detailed step logging, test statuses, and automatic **inline Base64 screenshots** embedded inside failed reports.
*   **Console Logging:** Log4j2 streams all execution traces directly to the console in real time.

---

## Directory Structure

```
SeleniumUiAutomation
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.automation
│   │   │       ├── driver       # DriverManager & DriverFactory
│   │   │       ├── pages        # Page Objects (BasePage, AmazonHomePage, etc.)
│   │   │       ├── reports      # ExtentReportManager (Thread-safe reporter)
│   │   │       └── utils        # ConfigReader & ElementUtils (WebDriver wrappers)
│   │   └── resources
│   │       ├── config.properties # Global browser, URL & execution mode config
│   │       └── log4j2.xml        # Log4j2 console logging configuration
│   └── test
│       ├── java
│       │   ├── com.automation
│       │   │   ├── listeners    # TestNG TestListener for ExtentReports & Screenshots
│       │   │   └── tests        # BaseTest & suite containing iPhone & Galaxy search tests
│       │   └── com.lambdatest
│       │       └── BasicAuthentication # LambdaTest RemoteWebDriver base setup
│       └── resources
│           └── testng.xml       # Suite setup defining parallel method config
├── .gitignore                   # Git ignore rules
├── pom.xml                      # Maven dependency coordinator
└── README.md                    # Project documentation
```

---

## Prerequisites

Before running the tests, ensure you have the following installed:

*   **Java Development Kit (JDK) 21** or higher
*   **Apache Maven 3.8+**
*   Google Chrome (or Firefox/Edge if configured)

---

## Configuration

Global configuration settings are managed via `src/main/resources/config.properties`:

```properties
# Browser type: chrome, firefox, edge
browser=chrome

# Application URL under test
url=https://www.amazon.com/

# Run browser in headless mode (true/false)
headless=false

# Execution mode: local | lambdatest
execution.mode=local

# LambdaTest capabilities (used only when execution.mode=lambdatest)
lt.browser=Chrome
lt.browser.version=latest
lt.platform=Windows 10
lt.build=SeleniumUiAutomation Build
lt.project=SeleniumUiAutomation
```

---

## Execution

### Run locally (default)

```bash
mvn clean test
```

### Run on LambdaTest cloud grid

Pass your LambdaTest credentials and execution mode directly on the command line:

```bash
mvn clean test "-Dexecution.mode=lambdatest" "-DLT_USERNAME=<your_username>" "-DLT_ACCESS_KEY=<your_access_key>"
```

---

## Outputs & Reports

*   **Execution Reports:** Open the generated HTML file at `reports/index.html` to see details of the run, duration, pass/fail counts, and screenshots for any failure points.
*   **Console Logs:** All execution traces are printed to the terminal in real time via Log4j2.