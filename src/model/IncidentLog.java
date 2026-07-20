package model;

import java.time.LocalDateTime;

// IncidentLog records a single action performed on an incident (Creative Feature 1).
// Every time something important happens to an incident — such as being reported,
// assessed, assigned a team, or updated — a new IncidentLog entry is created
// and saved to DataStore.logs.
// The UpdateStatus screen displays all log entries for the selected incident
// as an activity timeline.
public class IncidentLog {

    private int incidentId;          // the ID of the incident this log entry belongs to
    private String action;           // short description of the action e.g. "Reported", "Severity Assessed"
    private String performedBy;      // name of the user who performed the action
    private LocalDateTime timestamp; // date and time when this log entry was created
    private String notes;            // additional details about the action

    // Constructor — creates a new log entry for the given incident.
    // timestamp is set automatically to the current date and time.
    public IncidentLog(int incidentId, String action, String performedBy, String notes) {
        this.incidentId = incidentId;
        this.action = action;
        this.performedBy = performedBy;
        this.notes = notes;
        this.timestamp = LocalDateTime.now(); // record the exact time this action happened
    }

    // Getters — used by UpdateStatusController to display the activity log timeline
    public int getIncidentId()           { return incidentId; }
    public String getAction()            { return action; }
    public String getPerformedBy()       { return performedBy; }
    public LocalDateTime getTimestamp()  { return timestamp; }
    public String getNotes()             { return notes; }

    // Returns a short summary of this log entry as a single line of text.
    // Used for display in the activity log ListView on the Update Status screen.
    @Override
    public String toString() {
        return timestamp.toString() + " - " + action + " by " + performedBy;
    }
}
