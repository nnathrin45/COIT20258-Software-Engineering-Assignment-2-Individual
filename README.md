# 🚨 Disaster Response System (DRS) Prototype

<div align="center">

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-8A2BE2?style=for-the-badge&logo=java&logoColor=white)
![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)

</div>

An initial desktop prototype of the **Disaster Response System (DRS)** developed as part of the **Software Engineering** unit (`COIT20258`, HT1 2026) at **CQUniversity Australia**.

The application is built using **JavaFX** and structured around the **Model-View-Controller (MVC)** architectural pattern. It empowers users to report incidents, assess severity levels, allocate response teams, update incident status, and monitor operations via a dynamic dashboard.

---

## 👥 Authorship & Course Info

* **Developer:** Natthapong Rinsakul *(Student ID: 12290114)*
* **Institution:** CQUniversity Australia
* **Course:** Software Engineering (HT1, 2026) | COIT20258
* **Unit Coordinator:** Meiru Che
* **Tutor:** Hrishikesh Kulkarni

---

## 🚀 Key Features

* **📌 FR1: Disaster Reporting:** Report emergency incidents with structured inputs including disaster type, location, description, date, and time.
* **⚖️ FR5: Severity Assessment:** Authorized officers can evaluate incidents and assign explicit severity levels.
* **🚒 FR7: Response Team Assignment:** Managers can deploy dedicated response units based on real-time availability.
* **🔄 FR9: Incident Status Update:** Real-time state tracking and workflow management for all ongoing incidents.
* **📊 FR10: Live Dashboard View:** Comprehensive overview monitoring active, critical, and resolved emergency cases.
* **✨ Creative Feature 1 (Activity Log):** Automatic logging of every system action with user names and timestamps, presented as an interactive timeline.
* **✨ Creative Feature 2 (Visual Severity & Warning Banner):** Color-coded indicators for incidents (Red/Orange/Yellow/Green) coupled with an automated global critical alert banner.

---

## 📐 Architecture & System Design

The project is structured strictly under the **MVC Pattern** to enforce clean separation of concerns:

```text
DRS/
├── src/
│   ├── drs/           # Application Entry (Main.java)
│   ├── view/          # View Layer (FXML UI layouts)
│   ├── controller/    # Controller Layer (JavaFX Action Event Handlers)
│   ├── model/         # Model Layer (Incident, ResponseTeam, IncidentLog)
│   └── data/          # Data Layer (Shared In-Memory DataStore)
└── test/              # Testing Framework (JUnit 5 Suite)
```

### Core Architecture Components
* **View Layer:** `ReportDisaster.fxml`, `AssessSeverity.fxml`, `AssignTeam.fxml`, `UpdateStatus.fxml`, `Dashboard.fxml`
* **Controller Layer:** Intercepts UI events, performs input validation, and channels communication down to the model.
* **Model Layer:** 
  * `Incident`: Manages data fields of a specific disaster entry.
  * `ResponseTeam`: Contains emergency dispatch unit information and status.
  * `IncidentLog`: Provides a data structure for structural audit trails.
* **Data Layer (`DataStore`):** Centralized in-memory static arrays simulating a data store for rapid prototyping.

---

## 🧪 Testing Plan & Verification

Robust structural verification was performed utilizing **JUnit 5**, achieving a **100% Pass Rate** across 27 core unit tests covering automated constraints, boundary rules, and default domain states.

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
* **UI Framework:** JavaFX SDK 21.0.11 (or compatible)
* **Testing Library:** JUnit 5 Platform Console Standalone
* **Data Storage:** In-Memory List Management (`DataStore.java`)

### 🔧 Required VM Arguments
To run the JavaFX desktop GUI application correctly, make sure to add the explicit module paths to your runtime settings. For VS Code, append the following to `.vscode/launch.json`:

```json
{
  "vmArgs": [
    "--module-path", "/path/to/your/JavaFX/javafx-sdk-21.0.11/lib",
    "--add-modules", "javafx.controls,javafx.fxml"
  ]
}
```

### 💻 Execution via Terminal (Compilation & Testing)
To clean, build, compile, and run the test suite via the Command Line Interface (CLI):

```bash
# 1. Clear old builds and setup build environment
rm -rf build && mkdir -p build

# 2. Compile source files with JavaFX modules
javac --module-path /path/to/JavaFX/lib --add-modules javafx.controls,javafx.fxml -cp "junit-platform-console-standalone.jar:src" -d build $(find src -name "*.java")

# 3. Run JUnit Test Suites
java --module-path /path/to/JavaFX/lib --add-modules javafx.controls,javafx.fxml -jar junit-platform-console-standalone.jar execute --class-path build --scan-classpath
```

---

## 📝 Prototype Reflections & Future Scope

* **💾 Storage Layer:** The prototype currently utilizes volatile in-memory storage. The future production roadmap targets transitioning into an external persistent relational database schema (e.g., SQLite or MySQL).
* **🔒 Authentication:** Role-based access control is simulated cleanly by isolating operational workflows into specific context screens (e.g., Officer views vs. Manager dashboards) rather than standard login gates.
