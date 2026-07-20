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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

// This controller handles the Report Disaster screen.
// A citizen uses this screen to submit a new disaster report.
// After submitting, the system creates an Incident and saves it to DataStore.
public class ReportDisasterController {

    // Input fields connected to ReportDisaster.fxml
    @FXML private ComboBox<String> disasterTypeBox;  // dropdown to select the type of disaster
    @FXML private TextField locationField;           // text field to enter the disaster location
    @FXML private DatePicker datePicker;             // calendar picker for the date of the incident
    @FXML private Spinner<Integer> hourSpinner;      // spinner to select the hour (0-23)
    @FXML private Spinner<Integer> minuteSpinner;    // spinner to select the minute (0-59)
    @FXML private TextArea descriptionArea;          // text area to describe the disaster
    @FXML private TextArea supportingArea;           // optional extra details (not required)
    @FXML private TextField messageField;            // shows confirmation message after submitting
    @FXML private TextField incidentIdField;         // shows the generated incident ID after submitting

    // This method runs automatically when the screen opens
    @FXML
    public void initialize() {
        // Add the five disaster type options to the dropdown
        disasterTypeBox.getItems().addAll("Fire", "Hurricane", "Earthquake", "Flood", "Other");

        // Set the default date to today and default time to the current hour and minute
        datePicker.setValue(LocalDate.now());
        hourSpinner.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, LocalTime.now().getHour()));
        minuteSpinner.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, LocalTime.now().getMinute()));

        // Load the five sample response teams into DataStore if they are not loaded yet
        DataStore.initSampleTeams();
    }

    // Submit Report button — validates all required fields then creates and saves the incident
    @FXML
    private void handleSubmit() {
        // Check that all required fields have been filled in before continuing
        if (disasterTypeBox.getValue() == null) {
            showError("Please select a disaster type.");
            return;
        }
        if (locationField.getText().trim().isEmpty()) {
            showError("Please enter a location.");
            return;
        }
        if (datePicker.getValue() == null) {
            showError("Please select a date.");
            return;
        }
        if (descriptionArea.getText().trim().isEmpty()) {
            showError("Please enter a description.");
            return;
        }

        // Combine the selected date and time into one LocalDateTime value
        LocalDateTime reportedAt = LocalDateTime.of(
            datePicker.getValue(),
            LocalTime.of(hourSpinner.getValue(), minuteSpinner.getValue()));

        // Create a new Incident object using the values from the form
        Incident incident = new Incident(
            disasterTypeBox.getValue(),
            locationField.getText(),
            reportedAt,
            descriptionArea.getText(),
            "Citizen User");

        // Save the new incident to the shared DataStore so other screens can access it
        DataStore.incidents.add(incident);

        // Record this action in the activity log (Creative Feature 1)
        DataStore.logs.add(new IncidentLog(
            incident.getIncidentId(),
            "Reported",
            "Citizen User",
            "Initial disaster report submitted"));

        // Format the date and time for display in the confirmation message
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String idLabel = "INC-" + incident.getIncidentId();

        // Show the confirmation message and the generated incident ID on screen
        messageField.setText("Report submitted on " + reportedAt.format(fmt));
        incidentIdField.setText(idLabel);

        // Show a popup with the incident ID and the total number of incidents so far
        showInfo("Report Submitted",
            "Incident ID: " + idLabel + "\nTotal incidents: " + DataStore.incidents.size());
    }

    // Clear button — resets all input fields back to their default values
    @FXML
    private void handleClear() {
        disasterTypeBox.setValue(null);
        locationField.clear();
        datePicker.setValue(LocalDate.now());
        descriptionArea.clear();
        supportingArea.clear();
        messageField.clear();
        incidentIdField.clear();
    }

    // Cancel button — clears the form and shows a cancelled message
    @FXML
    private void handleCancel() {
        handleClear();
        messageField.setText("Report cancelled.");
    }

    // Navigation buttons — each button loads a different screen
    @FXML private void goReport(ActionEvent e) { /* already on this screen — do nothing */ }
    @FXML private void goAssess(ActionEvent e) { switchScreen(e, "AssessSeverity"); }
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