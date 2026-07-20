package model;

// ResponseTeam represents a response team that can be assigned to a disaster incident.
// Five sample teams are created automatically in DataStore.initSampleTeams()
// when the application starts.
// When a team is assigned to an incident, its available flag is set to false
// so it no longer appears in the team dropdown on the Assign Team screen.
public class ResponseTeam {

    private int teamId;          // unique ID for this team e.g. 1, 2, 3
    private String teamName;     // name of the team e.g. "Fire Unit A"
    private String department;   // department this team belongs to e.g. "Fire Department"
    private boolean available;   // true = team is free to be assigned, false = already assigned

    // Constructor — creates a new ResponseTeam with the given details.
    // available starts as true because a newly created team is not yet assigned anywhere.
    public ResponseTeam(int teamId, String teamName, String department) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.department = department;
        this.available = true;  // all teams start as available
    }

    // Getters — used by AssignTeamController to display teams in the dropdown
    public int getTeamId()        { return teamId; }
    public String getTeamName()   { return teamName; }
    public String getDepartment() { return department; }

    // Returns true if this team is available to be assigned, false if already assigned
    public boolean isAvailable()  { return available; }

    // Sets whether this team is available or not.
    // Called by AssignTeamController with false after a team is assigned to an incident.
    public void setAvailable(boolean available) { this.available = available; }

    // Returns the team name and department as a single line of text.
    // Used for display in the team dropdown on the Assign Team screen.
    @Override
    public String toString() {
        return teamName + " (" + department + ")";
    }
}
