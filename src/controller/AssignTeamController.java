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
import model.ResponseTeam;

// This controller handles the Assign Response Team screen.
// A Response Coordinator uses this screen to set a priority level
// and assign an available response team to an assessed incident.
public class AssignTeamController {

    // Input fields connected to AssignTeam.fxml
    @FXML private ComboBox<String> incidentBox;        // dropdown to pick an assessed incident
    @FXML private TextField typeField;                 // shows the disaster type (read-only)
    @FXML private TextField locationField;             // shows the location (read-only)
    @FXML private TextField severityField;             // shows the severity level (read-only)
    @FXML private TextField currentPriorityField;      // shows the current priority (read-only)
    @FXML private ComboBox<String> priorityBox;        // dropdown to set the response priority
    @FXML private ComboBox<String> teamBox;            // dropdown showing available teams only
    @FXML private TextField messageField;              // shows confirmation message after assigning

    // Stores the incident that the coordinator selected from the dropdown
    private Incident selectedIncident;

    // This method runs automatically when the screen opens
    @FXML
    public void initialize() {
        // Add the four priority options to the dropdown
        priorityBox.getItems().addAll("Immediate", "High", "Medium", "Low");

        // Load assessed incidents and available teams into their dropdowns
        loadIncidents();
        loadTeams();
    }

    // Reads incidents from DataStore - only shows incidents that have been assessed
    private void loadIncidents() {
        incidentBox.getItems().clear();
        for (Incident inc : DataStore.incidents) {
            // Only show incidents that have been assessed (severity is not empty)
            if (!inc.getSeverity().isEmpty()) {
                String label = "INC-" + inc.getIncidentId() + " - " + inc.getDisasterType()
                             + " (" + inc.getSeverity() + ")";
                incidentBox.getItems().add(label);
            }
        }
    }

    // Reads teams from DataStore — only shows teams that are currently available
    private void loadTeams() {
        teamBox.getItems().clear();
        for (ResponseTeam team : DataStore.teams) {
            if (team.isAvailable()) {
                teamBox.getItems().add(team.getTeamName() + " (" + team.getDepartment() + ")");
            }
        }
    }

    // Refresh button — reloads both incident list and team list
    @FXML
    private void handleRefresh() {
        loadIncidents();
        loadTeams();
    }

    // Runs when the coordinator picks an incident from the dropdown
    // Fills in the detail fields so the coordinator can review the incident
    @FXML
    private void handleIncidentSelected() {
        String selected = incidentBox.getValue();
        if (selected == null) return;

        // Extract the incident ID number from the label e.g. "INC-1000 - Fire (High)"
        int id = Integer.parseInt(selected.substring(4, selected.indexOf(" ")));
        selectedIncident = DataStore.findIncidentById(id);

        if (selectedIncident != null) {
            // Fill in the detail fields with the selected incident's data
            typeField.setText(selectedIncident.getDisasterType());
            locationField.setText(selectedIncident.getLocation());
            severityField.setText(selectedIncident.getSeverity());

            // Show current priority or "Not yet set" if no priority has been assigned
            currentPriorityField.setText(
                selectedIncident.getPriority().isEmpty() ? "Not yet set" : selectedIncident.getPriority());

            // If a priority was already set before, pre-select it in the dropdown
            if (!selectedIncident.getPriority().isEmpty()) {
                priorityBox.setValue(selectedIncident.getPriority());
            }
        }
    }

    // Assign Team button — validates the form, assigns the team, and updates the incident
    @FXML
    private void handleAssign() {
        // Check that all required fields are filled in
        if (selectedIncident == null) {
            showError("Please select an incident first.");
            return;
        }
        if (priorityBox.getValue() == null) {
            showError("Please select a priority level.");
            return;
        }
        if (teamBox.getValue() == null) {
            showError("Please select a response team.");
            return;
        }

        // Save the priority and update the incident status to Active Response
        selectedIncident.setPriority(priorityBox.getValue());
        selectedIncident.setStatus("Active Response");

        // Extract just the team name from "Fire Unit A (Fire Department)"
        String teamName = teamBox.getValue().substring(0, teamBox.getValue().indexOf(" ("));

        // Find the team in DataStore and mark it as unavailable
        for (ResponseTeam team : DataStore.teams) {
            if (team.getTeamName().equals(teamName)) {
                team.setAvailable(false);
                break;
            }
        }

        // Record this action in the activity log (Creative Feature 1)
        DataStore.logs.add(new IncidentLog(
            selectedIncident.getIncidentId(),
            "Team Assigned",
            "Coordinator User",
            "Priority: " + priorityBox.getValue() + ". Team: " + teamBox.getValue()));

        // Show confirmation on screen and in a popup
        messageField.setText("Team " + teamName + " assigned with " + priorityBox.getValue() + " priority");
        showInfo("Team Assigned",
            "INC-" + selectedIncident.getIncidentId() + "\nTeam: " + teamName
            + "\nPriority: " + priorityBox.getValue());

        // Reload the team list so the assigned team no longer appears
        loadTeams();
    }

    // Clear button — resets all fields back to empty
    @FXML
    private void handleClear() {
        incidentBox.setValue(null);
        typeField.clear();
        locationField.clear();
        severityField.clear();
        currentPriorityField.clear();
        priorityBox.setValue(null);
        teamBox.setValue(null);
        messageField.clear();
        selectedIncident = null;
    }

    // Cancel button — clears the form and shows a cancelled message
    @FXML
    private void handleCancel() {
        handleClear();
        messageField.setText("Assignment cancelled.");
    }

    // Navigation buttons — each button loads a different screen
    @FXML private void goReport(ActionEvent e) { switchScreen(e, "ReportDisaster"); }
    @FXML private void goAssess(ActionEvent e) { switchScreen(e, "AssessSeverity"); }
    @FXML private void goAssign(ActionEvent e) { /* already on this screen — do nothing */ }
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