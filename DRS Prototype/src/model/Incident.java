package model;

import java.time.LocalDateTime;

// Incident represents a single disaster report in the system.
// It stores all details about the disaster including type, location,
// severity, priority, and current status.
// Every incident gets a unique ID starting from 1000.
public class Incident {

    // nextId is shared across all Incident objects (static).
    // It starts at 1000 and increases by 1 each time a new incident is created.
    // This ensures every incident has a unique ID e.g. 1000, 1001, 1002 ...
    private static int nextId = 1000;

    private int incidentId;          // unique ID assigned automatically when created
    private String disasterType;     // type of disaster e.g. Fire, Flood, Earthquake
    private String location;         // location where the disaster occurred
    private LocalDateTime reportedAt; // date and time the report was submitted
    private String description;      // detailed description of the disaster
    private String severity;         // set by officer: Critical, High, Medium, or Low
    private String priority;         // set by coordinator: Immediate, High, Medium, or Low
    private String status;           // current stage of the incident response
    private String reportedBy;       // name of the person who submitted the report

    // Constructor — creates a new Incident with the given details.
    // incidentId is assigned automatically using nextId.
    // status starts as "Reported" because the incident has just been submitted.
    // severity and priority start as empty strings because they have not been set yet.
    public Incident(String disasterType, String location, LocalDateTime reportedAt,
                    String description, String reportedBy) {
        this.incidentId = nextId++;   // assign current ID then increase nextId by 1
        this.disasterType = disasterType;
        this.location = location;
        this.reportedAt = reportedAt;
        this.description = description;
        this.reportedBy = reportedBy;
        this.status = "Reported";     // default status when a new incident is created
        this.severity = "";           // not assessed yet
        this.priority = "";           // not prioritised yet
    }

    // Getters — used by controllers and the Dashboard to read incident data
    public int getIncidentId()           { return incidentId; }
    public String getDisasterType()      { return disasterType; }
    public String getLocation()          { return location; }
    public LocalDateTime getReportedAt() { return reportedAt; }
    public String getDescription()       { return description; }
    public String getSeverity()          { return severity; }
    public String getPriority()          { return priority; }
    public String getStatus()            { return status; }
    public String getReportedBy()        { return reportedBy; }

    // Setters — only severity, priority, and status can be changed after creation.
    // disasterType, location, and description are fixed once the report is submitted.
    public void setSeverity(String severity) { this.severity = severity; }
    public void setPriority(String priority) { this.priority = priority; }
    public void setStatus(String status)     { this.status = status; }

    // Returns a short summary of this incident as a single line of text.
    // Used for display in dropdowns and log entries.
    @Override
    public String toString() {
        return "INC-" + incidentId + " | " + disasterType + " @ " + location
             + " | Status: " + status;
    }
}
