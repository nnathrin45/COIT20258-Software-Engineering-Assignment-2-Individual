package test;

import model.ResponseTeam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Test class for ResponseTeam.
// Verifies that teams are created correctly, that availability
// starts as true by default, and that setAvailable() works properly.
public class ResponseTeamTest {

    // A shared ResponseTeam object used across the tests below
    private ResponseTeam team;

    // setUp() runs before every test method.
    // Creates a fresh ResponseTeam so each test starts with the same known state.
    @BeforeEach
    void setUp() {
        team = new ResponseTeam(1, "Fire Unit A", "Fire Department");
    }

    // Test: a new ResponseTeam is created with the correct values.
    // Expected: object is not null and all fields match the values passed to the constructor.
    @Test
    void testTeamCreated() {
        assertNotNull(team);
        assertEquals(1, team.getTeamId());
        assertEquals("Fire Unit A", team.getTeamName());
        assertEquals("Fire Department", team.getDepartment());
    }

    // Test: a new ResponseTeam is available by default.
    // Expected: isAvailable() returns true without calling setAvailable().
    @Test
    void testTeamIsAvailableByDefault() {
        assertTrue(team.isAvailable());
    }

    // Test: setAvailable(false) marks the team as unavailable.
    // Expected: isAvailable() returns false after the team is assigned to an incident.
    @Test
    void testSetUnavailable() {
        team.setAvailable(false);
        assertFalse(team.isAvailable());
    }

    // Test: setAvailable(true) makes the team available again after being set to unavailable.
    // Expected: isAvailable() returns true after calling setAvailable(true).
    @Test
    void testSetAvailableAgain() {
        team.setAvailable(false);
        team.setAvailable(true);
        assertTrue(team.isAvailable());
    }

    // Test: toString() returns a string that contains the team name and department.
    // Expected: the result contains "Fire Unit A" and "Fire Department".
    @Test
    void testToStringFormat() {
        String result = team.toString();
        assertTrue(result.contains("Fire Unit A"));
        assertTrue(result.contains("Fire Department"));
    }
}
