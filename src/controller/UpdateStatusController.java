package controller;

import data.DataStore;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Incident;
import model.IncidentLog;
import java.time.format.DateTimeFormatter;

// This controller handles the Update Incident Status screen.
// An Emergency Responder uses this screen to update the progress of an assigned incident.
// It also displays the full Activity Log timeline for the selected incident (Creative Feature 1).
public class UpdateStatusController {

    // Input fields connected to UpdateStatus.fxml
    @FXML private ComboBox<String> incidentBox;      // dropdown to pick an incident
    @FXML private TextField typeField;               // shows the disaster type (read-only)
    @FXML private TextField locationField;           // shows the location (read-only)
    @FXML private TextField severityField;           // shows the severity level (read-only)
    @FXML private TextField currentStatusField;      // shows the current status (read-only)
    @FXML private ComboBox<String> statusBox;        // dropdown to choose the new status
    @FXML private TextArea notesArea;                // responder types update notes here
    @FXML private TextField messageField;            // shows confirmation message after saving
    @FXML private ListView<String> activityLogList;  // shows the full action history (Creative Feature 1)

    // Stores the incident that the responder selected from the dropdown
    private Incident selectedIncident;

    // This method runs automatically when the screen opens
    @FXML
    public void initialize() {
        // Add the six possible status options to the dropdown
        statusBox.getItems().addAll(
            "Reported", "Under Assessment", "Active Response",
            "Contained", "Resolved", "Escalated");

        // Load all existing incidents into the incident dropdown
        loadIncidents();
    }

    // Reads all incidents from DataStore and puts them in the dropdown list
    private void loadIncidents() {
        incidentBox.getItems().clear();
        for (Incident inc : DataStore.incidents) {
            // Show the incident ID, disaster type, and current status in the label
            String label = "INC-" + inc.getIncidentId() + " - " + inc.getDisasterType()
                         + " (" + inc.getStatus() + ")";
            incidentBox.getItems().add(label);
        }
    }

    // Runs when the responder picks an incident from the dropdown
    // Fills in the detail fields and loads the activity log for that incident
    @FXML
    private void handleIncidentSelected() {
        String selected = incidentBox.getValue();
        if (selected == null) return;

        // Extract the incident ID number from the label e.g. "INC-1000 - Fire (Reported)"
        int id = Integer.parseInt(selected.substring(4, selected.indexOf(" ")));
        selectedIncident = DataStore.findIncidentById(id);

        if (selectedIncident != null) {
            // Fill in the detail fields with the selected incident's data
            typeField.setText(selectedIncident.getDisasterType());
            locationField.setText(selectedIncident.getLocation());
            severityField.setText(selectedIncident.getSeverity());
            currentStatusField.setText(selectedIncident.getStatus());

            // Pre-select the current status in the status dropdown
            statusBox.setValue(selectedIncident.getStatus());

            // Load and display all previous actions for this incident (Creative Feature 1)
            loadActivityLog();
        }
    }

    // Reads all log entries for the selected incident from DataStore
    // and displays them in the activity log list (Creative Feature 1)
    private void loadActivityLog() {
        activityLogList.getItems().clear();
        if (selectedIncident == null) return;

        // Format each log entry as "[date time] action by user - notes"
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (IncidentLog log : DataStore.findLogsByIncidentId(selectedIncident.getIncidentId())) {
            String entry = "[" + log.getTimestamp().format(fmt) + "] "
                         + log.getAction() + " by " + log.getPerformedBy()
                         + " - " + log.getNotes();
            activityLogList.getItems().add(entry);
        }
    }

    // Submit Update button — validates the form, saves the new status, and refreshes the log
    @FXML
    private void handleSubmit() {
        // Check that all required fields have been filled in
        if (selectedIncident == null) {
            showError("Please select an incident first.");
            return;
        }
        if (statusBox.getValue() == null) {
            showError("Please select a status.");
            return;
        }
        if (notesArea.getText().trim().isEmpty()) {
            showError("Please enter update notes.");
            return;
        }

        // Save the new status to the incident
        selectedIncident.setStatus(statusBox.getValue());

        // Record this action in the activity log (Creative Feature 1)
        DataStore.logs.add(new IncidentLog(
            selectedIncident.getIncidentId(),
            "Status Updated",
            "Responder User",
            "Status changed to " + statusBox.getValue() + ". " + notesArea.getText()));

        // Update the current status field and show a confirmation message
        currentStatusField.setText(statusBox.getValue());
        messageField.setText("Status updated to " + statusBox.getValue());

        // Reload the activity log so the new entry appears immediately
        loadActivityLog();

        // Reload the incident dropdown so the updated status label is shown
        loadIncidents();

        // Show a popup confirming the update
        showInfo("Status Updated",
            "INC-" + selectedIncident.getIncidentId() + " is now " + statusBox.getValue());
    }

    // Clear button — resets all fields and clears the activity log list
    @FXML
    private void handleClear() {
        incidentBox.setValue(null);
        typeField.clear();
        locationField.clear();
        severityField.clear();
        currentStatusField.clear();
        statusBox.setValue(null);
        notesArea.clear();
        messageField.clear();
        activityLogList.getItems().clear();
        selectedIncident = null;
    }

    // Cancel button — clears the form and shows a cancelled message
    @FXML
    private void handleCancel() {
        handleClear();
        messageField.setText("Update cancelled.");
    }

    // Navigation buttons — each button loads a different screen
    @FXML private void goReport(ActionEvent e) { switchScreen(e, "ReportDisaster"); }
    @FXML private void goAssess(ActionEvent e) { switchScreen(e, "AssessSeverity"); }
    @FXML private void goAssign(ActionEvent e) { switchScreen(e, "AssignTeam"); }
    @FXML private void goUpdate(ActionEvent e) { /* already on this screen — do nothing */ }
    @FXML private void goDashboard(ActionEvent e) { switchScreen(e, "Dashboard"); }

    // Loads the given FXML screen and replaces the current window content
    private void switchScreen(ActionEvent e, String screenName) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/" + screenName + ".fxml"));
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception ex) {
            showError("Could not load screen: " + ex.getMessage());
        }
    }

    // Shows a red error popup with the given message
    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // Shows a blue information popup with the given title and message
    private void showInfo(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
