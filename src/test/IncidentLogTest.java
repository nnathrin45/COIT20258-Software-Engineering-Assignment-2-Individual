package test;

import model.IncidentLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

// Test class for IncidentLog (Creative Feature 1).
// Verifies that log entries are created correctly, that the timestamp
// is set automatically, and that different action types are stored properly.
public class IncidentLogTest {

    // A shared IncidentLog object used across the tests below
    private IncidentLog log;

    // setUp() runs before every test method.
    // Creates a fresh IncidentLog so each test starts with the same known state.
    @BeforeEach
    void setUp() {
        log = new IncidentLog(1000, "Reported", "Citizen User", "Initial report");
    }

    // Test: a new IncidentLog is created with the correct values.
    // Expected: object is not null and all fields match the values passed to the constructor.
    @Test
    void testLogCreated() {
        assertNotNull(log);
        assertEquals(1000, log.getIncidentId());
        assertEquals("Reported", log.getAction());
        assertEquals("Citizen User", log.getPerformedBy());
        assertEquals("Initial report", log.getNotes());
    }

    // Test: the timestamp is set automatically when the log is created.
    // Expected: getTimestamp() does not return null.
    @Test
    void testTimestampIsAutoSet() {
        assertNotNull(log.getTimestamp());
    }

    // Test: the timestamp is very close to the current time.
    // Expected: the difference between the log timestamp and now is less than 5 seconds.
    // This confirms that LocalDateTime.now() was called inside the constructor.
    @Test
    void testTimestampIsRecent() {
        LocalDateTime now = LocalDateTime.now();
        long secondsDiff = java.time.Duration.between(log.getTimestamp(), now).getSeconds();
        assertTrue(secondsDiff < 5);
    }

    // Test: different action types can be stored in separate IncidentLog objects.
    // Expected: each log stores the correct action string that was passed to it.
    @Test
    void testDifferentActions() {
        IncidentLog assessLog = new IncidentLog(1000, "Severity Assessed",
            "Officer User", "Set to High");
        assertEquals("Severity Assessed", assessLog.getAction());

        IncidentLog assignLog = new IncidentLog(1000, "Team Assigned",
            "Coordinator User", "Fire Unit A assigned");
        assertEquals("Team Assigned", assignLog.getAction());
    }

    // Test: toString() returns a string that contains the action and the user name.
    // Expected: the result contains "Reported" and "Citizen User".
    @Test
    void testToStringContainsAction() {
        String result = log.toString();
        assertTrue(result.contains("Reported"));
        assertTrue(result.contains("Citizen User"));
    }
}
