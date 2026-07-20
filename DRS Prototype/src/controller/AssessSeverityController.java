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

// This controller handles the Assess Severity screen.
// A Disaster Assessment Officer uses this screen to review an incident
// and assign a severity level (Critical, High, Medium, or Low).
public class AssessSeverityController {

    // Input fields connected to AssessSeverity.fxml
    @FXML private ComboBox<String> incidentBox;         // dropdown to pick an incident
    @FXML private TextField typeField;                  // shows the disaster type (read-only)
    @FXML private TextField locationField;              // shows the location (read-only)
    @FXML private TextField reportedAtField;            // shows the reported date and time (read-only)
    @FXML private TextField statusField;                // shows the current status (read-only)
    @FXML private TextArea descriptionArea;             // shows the incident description (read-only)
    @FXML private ComboBox<String> severityBox;         // dropdown to choose severity level
    @FXML private TextArea notesArea;                   // officer types assessment notes here
    @FXML private TextField messageField;               // shows confirmation message after saving

    // Stores the incident that the officer selected from the dropdown
    private Incident selectedIncident;

    // This method runs automatically when the screen opens
    @FXML
    public void initialize() {
        // Add the four severity options to the dropdown
        severityBox.getItems().addAll("Critical", "High", "Medium", "Low");

        // Load all existing incidents into the incident dropdown
        loadIncidents();
    }

    // Reads all incidents from DataStore and puts them in the dropdown list
    private void loadIncidents() {
        incidentBox.getItems().clear();
        for (Incident inc : DataStore.incidents) {
            String label = "INC-" + inc.getIncidentId() + " - " + inc.getDisasterType()
                         + " at " + inc.getLocation();
            incidentBox.getItems().add(label);
        }
    }

    // Refresh button — reloads the incident list in case new incidents were added
    @FXML
    private void handleRefresh() {
        loadIncidents();
        showInfo("Refreshed", "Total incidents: " + DataStore.incidents.size());
    }

    // Runs when the officer picks an incident from the dropdown
    // Fills in all the detail fields so the officer can review the incident
    @FXML
    private void handleIncidentSelected() {
        String selected = incidentBox.getValue();
        if (selected == null) return;

        // Get the ID number from "INC-1000 - ..."
        int id = Integer.parseInt(selected.substring(4, selected.indexOf(" ")));
        selectedIncident = DataStore.findIncidentById(id);

        if (selectedIncident != null) {
            // Fill in the detail fields with the selected incident's data
            typeField.setText(selectedIncident.getDisasterType());
            locationField.setText(selectedIncident.getLocation());
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            reportedAtField.setText(selectedIncident.getReportedAt().format(fmt));
            statusField.setText(selectedIncident.getStatus());
            descriptionArea.setText(selectedIncident.getDescription());

            // If this incident was already assessed before, show the existing severity
            if (!selectedIncident.getSeverity().isEmpty()) {
                severityBox.setValue(selectedIncident.getSeverity());
            }
        }
    }

    // Save Assessment button - validates the form and saves the severity to the incident
    @FXML
    private void handleSave() {
        // Check that the officer has selected an incident
        if (selectedIncident == null) {
            showError("Please select an incident first.");
            return;
        }

        // Check that a severity level has been chosen
        if (severityBox.getValue() == null) {
            showError("Please select a severity level.");
            return;
        }
        // Check that assessment notes have been entered
        if (notesArea.getText().trim().isEmpty()) {
            showError("Please enter assessment notes.");
            return;
        }

        // Save the severity level and update the incident status
        selectedIncident.setSeverity(severityBox.getValue());
        selectedIncident.setStatus("Under Assessment");

        // Record this action in the activity log (Creative Feature 1)
        DataStore.logs.add(new IncidentLog(
            selectedIncident.getIncidentId(),
            "Severity Assessed",
            "Officer User",
            "Severity set to " + severityBox.getValue() + ". " + notesArea.getText()));
        
        // Show a confirmation message on screen and in a popup
        messageField.setText("Severity set to " + severityBox.getValue());
        showInfo("Assessment Saved",
            "INC-" + selectedIncident.getIncidentId() + " severity: " + severityBox.getValue());
    }

    // Clear button - resets all fields back to empty
    @FXML
    private void handleClear() {
        incidentBox.setValue(null);
        typeField.clear();
        locationField.clear();
        reportedAtField.clear();
        statusField.clear();
        descriptionArea.clear();
        severityBox.setValue(null);
        notesArea.clear();
        messageField.clear();
        selectedIncident = null;
    }

    // Cancel button - clears the form and shows a cancelled message
    @FXML
    private void handleCancel() {
        handleClear();
        messageField.setText("Assessment cancelled.");
    }

    // Navigation buttons — each button loads a different screen
    @FXML private void goReport(ActionEvent e) { switchScreen(e, "ReportDisaster"); }
    @FXML private void goAssess(ActionEvent e) { /* already here */ }
    @FXML private void goAssign(ActionEvent e) { switchScreen(e, "AssignTeam"); }
    @FXML private void goUpdate(ActionEvent e) { switchScreen(e, "UpdateStatus"); }
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
