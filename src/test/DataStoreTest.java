package test;

import data.DataStore;
import model.Incident;
import model.IncidentLog;
import model.ResponseTeam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

// Test class for DataStore.
// Verifies that incidents, teams, and logs can be added and retrieved correctly,
// and that the helper methods findIncidentById() and findLogsByIncidentId() work as expected.
public class DataStoreTest {

    // setUp() runs before every single test method.
    // It clears all three lists so that data from one test
    // does not affect the result of the next test.
    @BeforeEach
    void setUp() {
        DataStore.incidents.clear();
        DataStore.teams.clear();
        DataStore.logs.clear();
    }

    // Test: adding an incident to DataStore.incidents saves it correctly.
    // Expected: list size is 1 and the disaster type matches what was added.
    @Test
    void testAddIncident() {
        Incident inc = new Incident("Fire", "Melbourne",
            LocalDateTime.now(), "Test fire", "Citizen");
        DataStore.incidents.add(inc);

        assertEquals(1, DataStore.incidents.size());
        assertEquals("Fire", DataStore.incidents.get(0).getDisasterType());
    }

    // Test: calling initSampleTeams() once loads exactly 5 teams.
    // Expected: DataStore.teams has 5 entries.
    @Test
    void testInitSampleTeams() {
        DataStore.initSampleTeams();
        assertEquals(5, DataStore.teams.size());
    }

    // Test: calling initSampleTeams() twice should not create duplicate teams.
    // Expected: DataStore.teams still has only 5 entries after calling twice.
    @Test
    void testInitSampleTeamsOnlyOnce() {
        DataStore.initSampleTeams();
        DataStore.initSampleTeams();
        assertEquals(5, DataStore.teams.size());
    }

    // Test: findIncidentById() returns the correct incident when the ID exists.
    // Expected: the returned incident is not null and its disaster type is "Flood".
    @Test
    void testFindIncidentById() {
        Incident inc = new Incident("Flood", "Brisbane",
            LocalDateTime.now(), "Flood report", "Citizen");
        DataStore.incidents.add(inc);

        Incident found = DataStore.findIncidentById(inc.getIncidentId());
        assertNotNull(found);
        assertEquals("Flood", found.getDisasterType());
    }

    // Test: findIncidentById() returns null when no incident has the given ID.
    // Expected: result is null because ID 99999 does not exist in DataStore.
    @Test
    void testFindIncidentByIdNotFound() {
        Incident found = DataStore.findIncidentById(99999);
        assertNull(found);
    }

    // Test: findLogsByIncidentId() returns only the logs that belong to the given incident.
    // Two logs are added for incident 1000 and one log for incident 2000.
    // Expected: findLogsByIncidentId(1000) returns 2 entries, findLogsByIncidentId(2000) returns 1.
    @Test
    void testFindLogsByIncidentId() {
        DataStore.logs.add(new IncidentLog(1000, "Reported", "Citizen", "Note 1"));
        DataStore.logs.add(new IncidentLog(1000, "Assessed", "Officer", "Note 2"));
        DataStore.logs.add(new IncidentLog(2000, "Reported", "Citizen", "Note 3"));

        assertEquals(2, DataStore.findLogsByIncidentId(1000).size());
        assertEquals(1, DataStore.findLogsByIncidentId(2000).size());
    }

    // Test: setting a team's availability to false works correctly.
    // Expected: the team is available by default, and becomes unavailable after setAvailable(false).
    @Test
    void testTeamAvailability() {
        DataStore.initSampleTeams();
        ResponseTeam firstTeam = DataStore.teams.get(0);

        assertTrue(firstTeam.isAvailable());   // team should be available before assignment

        firstTeam.setAvailable(false);
        assertFalse(firstTeam.isAvailable());  // team should be unavailable after assignment
    }
}
