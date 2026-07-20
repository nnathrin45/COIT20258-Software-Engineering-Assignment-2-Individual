package test;

import model.Incident;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

// Test class for Incident.
// Verifies that incidents are created correctly, that default values
// are set properly, and that setters update the fields as expected.
public class IncidentTest {

    // A shared Incident object used across the tests below
    private Incident incident;

    // setUp() runs before every test method.
    // Creates a fresh Incident so each test starts with the same known state.
    @BeforeEach
    void setUp() {
        incident = new Incident("Fire", "Melbourne",
            LocalDateTime.of(2026, 5, 5, 10, 30),
            "Big fire in the city", "Citizen User");
    }

    // Test: a new Incident is created with the correct values.
    // Expected: object is not null and all fields match the values passed to the constructor.
    @Test
    void testIncidentCreated() {
        assertNotNull(incident);
        assertEquals("Fire", incident.getDisasterType());
        assertEquals("Melbourne", incident.getLocation());
        assertEquals("Big fire in the city", incident.getDescription());
    }

    // Test: the status of a new incident is "Reported" by default.
    // Expected: getStatus() returns "Reported" without calling setStatus().
    @Test
    void testInitialStatusIsReported() {
        assertEquals("Reported", incident.getStatus());
    }

    // Test: the severity of a new incident is empty by default.
    // Expected: getSeverity() returns "" because the officer has not assessed it yet.
    @Test
    void testInitialSeverityIsEmpty() {
        assertEquals("", incident.getSeverity());
    }

    // Test: the priority of a new incident is empty by default.
    // Expected: getPriority() returns "" because the coordinator has not set it yet.
    @Test
    void testInitialPriorityIsEmpty() {
        assertEquals("", incident.getPriority());
    }

    // Test: two different Incident objects have different IDs.
    // Expected: getIncidentId() returns a different value for each object
    // because nextId increases by 1 each time a new Incident is created.
    @Test
    void testIncidentIdIsUnique() {
        Incident incident2 = new Incident("Flood", "Brisbane",
            LocalDateTime.now(), "Flood report", "Citizen User");
        assertNotEquals(incident.getIncidentId(), incident2.getIncidentId());
    }

    // Test: setSeverity() updates the severity field correctly.
    // Expected: getSeverity() returns "High" after calling setSeverity("High").
    @Test
    void testSetSeverity() {
        incident.setSeverity("High");
        assertEquals("High", incident.getSeverity());
    }

    // Test: setPriority() updates the priority field correctly.
    // Expected: getPriority() returns "Immediate" after calling setPriority("Immediate").
    @Test
    void testSetPriority() {
        incident.setPriority("Immediate");
        assertEquals("Immediate", incident.getPriority());
    }

    // Test: setStatus() updates the status field correctly.
    // Expected: getStatus() returns "Active Response" after calling setStatus("Active Response").
    @Test
    void testSetStatus() {
        incident.setStatus("Active Response");
        assertEquals("Active Response", incident.getStatus());
    }

    // Test: toString() returns a string that contains the disaster type and location.
    // Expected: the result contains "Fire" and "Melbourne".
    @Test
    void testToStringContainsKeyInfo() {
        String result = incident.toString();
        assertTrue(result.contains("Fire"));
        assertTrue(result.contains("Melbourne"));
    }

    // Test: the reportedBy field is stored correctly.
    // Expected: getReportedBy() returns "Citizen User" as passed to the constructor.
    @Test
    void testReportedByIsSet() {
        assertEquals("Citizen User", incident.getReportedBy());
    }
}
