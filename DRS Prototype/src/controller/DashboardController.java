package controller;

import data.DataStore;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Incident;

// This controller handles the Incident Dashboard screen.
// A Disaster Management Manager uses this screen to monitor all active incidents.
// It shows a summary table with severity colour coding and a red alert banner
// when any Critical incident exists (Creative Feature 2).
public class DashboardController {

    // Table and columns connected to Dashboard.fxml
    @FXML private TableView<Incident> incidentTable;   // main table showing all incidents
    @FXML private TableColumn<Incident, String> idColumn;        // Incident ID column
    @FXML private TableColumn<Incident, String> typeColumn;      // Disaster Type column
    @FXML private TableColumn<Incident, String> locationColumn;  // Location column
    @FXML private TableColumn<Incident, String> severityColumn;  // Severity column (colour coded)
    @FXML private TableColumn<Incident, String> priorityColumn;  // Priority column
    @FXML private TableColumn<Incident, String> statusColumn;    // Status column

    // Summary count labels at the top of the screen
    @FXML private Label totalLabel;     // shows total number of incidents
    @FXML private Label criticalLabel;  // shows count of Critical incidents
    @FXML private Label highLabel;      // shows count of High incidents
    @FXML private Label mediumLabel;    // shows count of Medium incidents
    @FXML private Label lowLabel;       // shows count of Low incidents

    // Alert banner — only visible when there is at least one active Critical incident
    @FXML private HBox alertBanner;  // the red banner container (Creative Feature 2)
    @FXML private Label alertText;   // the warning message inside the banner

    // This method runs automatically when the screen opens
    @FXML
    public void initialize() {
        // Tell each column which field of Incident to display
        idColumn.setCellValueFactory(cell ->
            new javafx.beans.property.SimpleStringProperty("INC-" + cell.getValue().getIncidentId()));
        typeColumn.setCellValueFactory(cell ->
            new javafx.beans.property.SimpleStringProperty(cell.getValue().getDisasterType()));
        locationColumn.setCellValueFactory(cell ->
            new javafx.beans.property.SimpleStringProperty(cell.getValue().getLocation()));

        // Show "-" if severity or priority has not been set yet
        severityColumn.setCellValueFactory(cell ->
            new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getSeverity().isEmpty() ? "-" : cell.getValue().getSeverity()));
        priorityColumn.setCellValueFactory(cell ->
            new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getPriority().isEmpty() ? "-" : cell.getValue().getPriority()));
        statusColumn.setCellValueFactory(cell ->
            new javafx.beans.property.SimpleStringProperty(cell.getValue().getStatus()));

        // Override how the severity column renders each cell
        // so we can apply a different colour depending on the severity value (Creative Feature 2)
        severityColumn.setCellFactory(col -> new TableCell<Incident, String>() {
            @Override
            protected void updateItem(String severity, boolean empty) {
                super.updateItem(severity, empty);
                if (empty || severity == null) {
                    // Empty cell — clear the text and remove any colour style
                    setText(null);
                    setStyle("");
                } else {
                    // Set the text and apply the matching colour for this severity level
                    setText(severity);
                    setStyle("-fx-font-weight: bold; -fx-text-fill: " + getSeverityColour(severity) + ";");
                }
            }
        });

        // Load all incident data into the table and update the summary counts
        loadDashboard();
    }

    // Returns a hex colour code based on the severity level (Creative Feature 2)
    // Critical = red, High = dark orange, Medium = orange, Low = green
    private String getSeverityColour(String severity) {
        if (severity.equals("Critical")) return "#FF0000";
        if (severity.equals("High"))     return "#FF8C00";
        if (severity.equals("Medium"))   return "#FFA500";
        if (severity.equals("Low"))      return "#27AE60";
        return "#888888"; // default grey if severity is not set
    }

    // Refresh button — reloads all incident data from DataStore
    @FXML
    private void handleRefresh() {
        loadDashboard();
    }

    // Loads all incidents into the table and updates the summary counts and alert banner
    private void loadDashboard() {
        // Clear the table and reload all incidents from DataStore
        incidentTable.getItems().clear();
        incidentTable.getItems().addAll(DataStore.incidents);

        // Count how many active incidents belong to each severity level.
        // Resolved incidents are excluded from the count because they are no longer active.
        int total = DataStore.incidents.size();
        int critical = 0, high = 0, medium = 0, low = 0;

        for (Incident inc : DataStore.incidents) {
            // Skip incidents that have already been resolved
            if (!inc.getStatus().equals("Resolved")) {
                if (inc.getSeverity().equals("Critical"))    critical++;
                else if (inc.getSeverity().equals("High"))   high++;
                else if (inc.getSeverity().equals("Medium")) medium++;
                else if (inc.getSeverity().equals("Low"))    low++;
            }
        }

        // Update the summary count labels at the top of the screen
        totalLabel.setText("Total: " + total);
        criticalLabel.setText("Critical: " + critical);
        highLabel.setText("High: " + high);
        mediumLabel.setText("Medium: " + medium);
        lowLabel.setText("Low: " + low);

        // Show the red alert banner only if there are active Critical incidents (Creative Feature 2)
        // Banner is hidden when all Critical incidents have been resolved
        if (critical > 0) {
            alertBanner.setStyle("-fx-background-color: #FF0000; -fx-padding: 10;");
            alertText.setText(" ALERT: " + critical + " Critical incident(s) require immediate attention! (creative feature)");
            alertText.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
            alertBanner.setVisible(true);
            alertBanner.setManaged(true);  // makes the banner take up space in the layout
        } else {
            // No active Critical incidents — hide the banner completely
            alertBanner.setVisible(false);
            alertBanner.setManaged(false);  // removes the banner from the layout so it takes no space
        }
    }

    // Navigation buttons — each button loads a different screen
    @FXML private void goReport(ActionEvent e) { switchScreen(e, "ReportDisaster"); }
    @FXML private void goAssess(ActionEvent e) { switchScreen(e, "AssessSeverity"); }
    @FXML private void goAssign(ActionEvent e) { switchScreen(e, "AssignTeam"); }
    @FXML private void goUpdate(ActionEvent e) { switchScreen(e, "UpdateStatus"); }
    @FXML private void goDashboard(ActionEvent e) { /* already on this screen — do nothing */ }

    // Loads the given FXML screen and replaces the current window content
    private void switchScreen(ActionEvent e, String screenName) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/" + screenName + ".fxml"));
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Could not load screen: " + ex.getMessage());
            alert.showAndWait();
        }
    }
}