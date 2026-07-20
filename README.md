# Disaster Response System (DRS) Prototype

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-8A2BE2?style=for-the-badge&logo=java&logoColor=white)
![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)

An initial prototype of the **Disaster Response System (DRS)** developed as part of the Software Engineering unit (COIT20258, HT1 2026) at CQUniversity Australia. 

The application is a JavaFX-based desktop application structured using the **Model-View-Controller (MVC)** architectural pattern. It allows users to effectively report disaster incidents, assess severity levels, assign response teams, update incident status, and monitor active incidents via a dynamic dashboard.

---

## 👥 Authorship & Course Info
* **Developer:** Natthapong Rinsakul (Student ID: 12290114)
* **Institution:** CQUniversity Australia
* **Course:** Software Engineering (HT1, 2026) | COIT20258
* **Unit Coordinator:** Meiru Che
* **Tutor:** Hrishikesh Kulkarni

---

## 🚀 Features

* **FR1: Disaster Reporting:** Report emergency incidents with detailed inputs (disaster type, location, description, date, and specific time).
* **FR5: Severity Assessment:** Officers can assess and assign severity levels to reported incidents.
* **FR7: Response Team Assignment:** Managers can deploy specific response units to incidents based on availability.
* **FR9: Incident Status Update:** Real-time tracking and state workflow updates for each case.
* **FR10: Live Dashboard View:** Comprehensive overview of all active, reported, and resolved incidents.
* **✨ Creative Feature 1 (Activity Log):** Automatic timestamping and tracking of every action performed on an incident, rendered as a clear historical timeline.
* **✨ Creative Feature 2 (Visual Severity & Warning System):** Color-coded severity indicators on the dashboard (Red for Critical, Orange for High, Yellow for Medium, Green for Low) along with an automated global critical alert banner.

---

## 📐 Architecture & System Design

The system adheres strictly to the **MVC Pattern**, maintaining a clean separation of concerns:

DRS/
├── src/
│   ├── drs/           # Application Entry (Main.java)
│   ├── view/          # View Layer (FXML files: Dashboard, ReportDisaster, etc.)
│   ├── controller/    # Controller Layer (JavaFX Controller classes)
│   ├── model/         # Model Layer (Incident, ResponseTeam, IncidentLog)
│   └── data/          # Data Layer (Shared In-Memory DataStore)
└── test/              # Unit Tests (JUnit 5 Suite)

### Core Architecture Components:
* **View Layer:** `ReportDisaster.fxml`, `AssessSeverity.fxml`, `AssignTeam.fxml`, `UpdateStatus.fxml`, `Dashboard.fxml`
* **Controller Layer:** Handles UI events, input validation, and channels data flow to the Model.
* **Model Layer:** 
  * `Incident`: Manages all properties of a disaster entry.
  * `ResponseTeam`: Contains emergency dispatch unit details.
  * `IncidentLog`: Audit trail structure for actions.
* **Data Layer (`DataStore`):** Centralized in-memory static arrays simulating data storage for prototype efficiency.

---

## 🧪 Testing Plan & Verification

Robust verification was implemented using **JUnit 5**, maintaining a **100% Pass Rate** across 27 core structural tests covering edge cases, default states, data generation logic, and constraints.

### Test Result Summary

| Test Class | Total Tests | Passed | Failed |
| :--- | :---: | :---: | :---: |
| **IncidentTest** | 10 | 10 | 0 |
| **ResponseTeamTest** | 5 | 5 | 0 |
| **IncidentLogTest** | 5 | 5 | 0 |
| **DataStoreTest** | 7 | 7 | 0 |
| **TOTAL** | **27** | **27** | **0** |

---

## 🛠️ Environment & Technical Specifications

* **Language:** Java
* **UI Framework:** JavaFX SDK 21.0.11 (or compatible version)
* **Testing Library:** JUnit 5 (JUnit Jupiter Platform Console Standalone)
* **Data Storage:** In-Memory List Management (`DataStore.java`)

### Required VM Arguments
To run the JavaFX GUI application correctly, ensure that your runtime environment configuration includes the module path configurations. For IDEs like VS Code, configure your `.vscode/launch.json` with the following arguments:

```json
"--module-path", "/path/to/your/JavaFX/javafx-sdk-21.0.11/lib",
"--add-modules", "javafx.controls,javafx.fxml"

Execution via Terminal (Compilation & Tests)
To clean, build, compile, and execute the test suites directly through CLI, you can use:

# Clear old builds and setup build environment
rm -rf build && mkdir -p build

# Compile source files with JavaFX modules
javac --module-path /path/to/JavaFX/lib --add-modules javafx.controls,javafx.fxml -cp "junit-platform-console-standalone.jar:src" -d build $(find src -name "*.java")

# Run JUnit Test Suites
java --module-path /path/to/JavaFX/lib --add-modules javafx.controls,javafx.fxml -jar junit-platform-console-standalone.jar execute --class-path build --scan-classpath

📝 Prototype Reflections & Future Scope
Storage Layer: Current state utilizes an volatile in-memory storage strategy. Future roadmap targets transitioning into persistent relational data schemas (e.g., SQLite/MySQL).

Role-based Authentication: Instead of a structural gated login system, roles are currently cleanly simulated via isolated, dedicated operational workflow screens (Officer views vs. Manager views).
