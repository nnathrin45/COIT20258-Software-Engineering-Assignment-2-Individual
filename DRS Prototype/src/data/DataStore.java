package data;

import model.Incident;
import model.IncidentLog;
import model.ResponseTeam;
import java.util.ArrayList;
import java.util.List;

// DataStore is the shared in-memory storage for the entire application.
// All five screens read from and write to this class directly.
// Using static lists means every screen accesses the same data
// without needing to pass objects between controllers.
public class DataStore {

    // List of all disaster incidents reported in this session
    public static List<Incident> incidents = new ArrayList<>();

    // List of all response teams — loaded once when the app starts
    public static List<ResponseTeam> teams = new ArrayList<>();

    // List of all activity log entries for every incident (Creative Feature 1)
    public static List<IncidentLog> logs = new ArrayList<>();

    // Creates five sample response teams and adds them to the teams list.
    // This method is called once in ReportDisasterController.initialize().
    // The if-check prevents duplicate teams if the method is accidentally called again.
    public static void initSampleTeams() {
        if (teams.isEmpty()) {
            teams.add(new ResponseTeam(1, "Fire Unit A",    "Fire Department"));
            teams.add(new ResponseTeam(2, "Fire Unit B", "Fire Department")); // Add more team 
            teams.add(new ResponseTeam(3, "Medical Team A", "Hospital"));
            teams.add(new ResponseTeam(4, "Medical Team B", "Hospital"));
            teams.add(new ResponseTeam(5, "Police Squad A", "Law Enforcement"));
            teams.add(new ResponseTeam(6, "Police Squad B", "Law Enforcement"));
            teams.add(new ResponseTeam(7, "Rescue Team A",  "Emergency Services"));
            teams.add(new ResponseTeam(8, "Rescue Team B",  "Emergency Services"));
            teams.add(new ResponseTeam(9, "Utility Crew A", "Public Works"));
            teams.add(new ResponseTeam(10, "Utility Crew B", "Public Works"));
        }
    }

    // Searches the incidents list for an incident with the given ID.
    // Returns the matching Incident object, or null if no match is found.
    // Used by AssessSeverityController, AssignTeamController, and UpdateStatusController
    // to retrieve a specific incident after the user picks one from the dropdown.
    public static Incident findIncidentById(int id) {
        for (Incident inc : incidents) {
            if (inc.getIncidentId() == id) {
                return inc;  // found a match — return it immediately
            }
        }
        return null;  // no incident with that ID was found
    }

    // Searches the logs list and returns all log entries that belong to the given incident ID.
    // Returns an empty list if no logs exist for that incident yet.
    // Used by UpdateStatusController to display the activity log timeline (Creative Feature 1).
    public static List<IncidentLog> findLogsByIncidentId(int id) {
        List<IncidentLog> result = new ArrayList<>();
        for (IncidentLog log : logs) {
            if (log.getIncidentId() == id) {
                result.add(log);  // add this log entry to the result list
            }
        }
        return result;
    }
}