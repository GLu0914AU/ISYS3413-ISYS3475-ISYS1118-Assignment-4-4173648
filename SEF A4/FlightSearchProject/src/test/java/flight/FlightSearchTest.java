package flight;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * JUnit test class for FlightSearch functionality.
 * Tests all conditions specified in the test case document.
 */
public class FlightSearchTest {
    private FlightSearch flightSearch;
    private String validFutureDate;
    private String validReturnDate;

    @BeforeEach
    public void setUp() {
        flightSearch = new FlightSearch();
        
        // Generate valid future dates for testing
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        validFutureDate = LocalDate.now().plusDays(7).format(formatter);
        validReturnDate = LocalDate.now().plusDays(14).format(formatter);
    }

    /**
     * TC01 – Total passengers boundary
     * Test Data: 0 adults, 0 children, 0 infants
     * Expected Result: false
     * Tests Condition 1: Total number of passengers must be at least 1
     */
    @Test
    public void testTC01_TotalPassengersBoundary() {
        boolean result = flightSearch.runFlightSearch(
            validFutureDate, "mel", false,
            validReturnDate, "syd", "economy",
            0, 0, 0  // 0 total passengers
        );
        
        assertFalse(result, "TC01: Zero passengers should return false");
        // Verify attributes are not initialized
        assertNull(flightSearch.getDepartureDate(), "Attributes should not be initialized on failure");
    }

    /**
     * TC02 – Child in emergency row
     * Test Data: 1 adult, 1 child, emergencyRowSeating=true, seatingClass="economy"
     * Expected Result: false
     * Tests Condition 2: Children cannot be seated in emergency row seating
     */
    @Test
    public void testTC02_ChildInEmergencyRow() {
        boolean result = flightSearch.runFlightSearch(
            validFutureDate, "mel", true,  // Emergency row = true
            validReturnDate, "syd", "economy",
            1, 1, 0  // 1 adult, 1 child
        );
        
        assertFalse(result, "TC02: Children in emergency row should return false");
        assertNull(flightSearch.getDepartureDate(), "Attributes should not be initialized on failure");
    }

    /**
     * TC03 – Infant in business class
     * Test Data: 1 adult, 1 infant, emergencyRowSeating=false, seatingClass="business"
     * Expected Result: false
     * Tests Condition 3: Infants cannot be seated in business class
     */
    @Test
    public void testTC03_InfantInBusinessClass() {
        boolean result = flightSearch.runFlightSearch(
            validFutureDate, "mel", false,
            validReturnDate, "syd", "business",
            1, 0, 1  // 1 adult, 1 infant
        );
        
        assertFalse(result, "TC03: Infants in business class should return false");
        assertNull(flightSearch.getDepartureDate(), "Attributes should not be initialized on failure");
    }

    /**
     * TC04 – Too many children per adult
     * Test Data: 1 adult, 3 children
     * Expected Result: false
     * Tests Condition 4: Maximum 2 children per adult
     */
    @Test
    public void testTC04_TooManyChildrenPerAdult() {
        boolean result = flightSearch.runFlightSearch(
            validFutureDate, "mel", false,
            validReturnDate, "syd", "economy",
            1, 3, 0  // 1 adult, 3 children (exceeds 2 per adult)
        );
        
        assertFalse(result, "TC04: More than 2 children per adult should return false");
        assertNull(flightSearch.getDepartureDate(), "Attributes should not be initialized on failure");
    }

    /**
     * TC05 – Too many infants per adult
     * Test Data: 1 adult, 2 infants
     * Expected Result: false
     * Tests Condition 5: Only 1 infant per adult allowed
     */
    @Test
    public void testTC05_TooManyInfantsPerAdult() {
        boolean result = flightSearch.runFlightSearch(
            validFutureDate, "mel", false,
            validReturnDate, "syd", "economy",
            1, 0, 2  // 1 adult, 2 infants (exceeds 1 per adult)
        );
        
        assertFalse(result, "TC05: More than 1 infant per adult should return false");
        assertNull(flightSearch.getDepartureDate(), "Attributes should not be initialized on failure");
    }

    /**
     * TC06 – Departure date in the past
     * Test Data: departureDate="01/01/2020"
     * Expected Result: false
     * Tests Condition 6: Departure date cannot be in the past
     */
    @Test
    public void testTC06_DepartureDateInPast() {
        boolean result = flightSearch.runFlightSearch(
            "01/01/2020", "mel", false,  // Past date
            validReturnDate, "syd", "economy",
            1, 0, 0
        );
        
        assertFalse(result, "TC06: Past departure date should return false");
        assertNull(flightSearch.getDepartureDate(), "Attributes should not be initialized on failure");
    }

    /**
     * TC07 – Invalid date format
     * Test Data: departureDate="2025-10-20"
     * Expected Result: false
     * Tests Condition 7: Date must be in DD/MM/YYYY format
     */
    @Test
    public void testTC07_InvalidDateFormat() {
        boolean result = flightSearch.runFlightSearch(
            "2025-10-20", "mel", false,  // Wrong format (YYYY-MM-DD instead of DD/MM/YYYY)
            validReturnDate, "syd", "economy",
            1, 0, 0
        );
        
        assertFalse(result, "TC07: Invalid date format should return false");
        assertNull(flightSearch.getDepartureDate(), "Attributes should not be initialized on failure");
    }

    /**
     * TC08 – Return date before departure
     * Test Data: departureDate="23/11/2025", returnDate="22/11/2025"
     * Expected Result: false
     * Tests Condition 8: Return date cannot be before departure date
     */
    @Test
    public void testTC08_ReturnDateBeforeDeparture() {
        boolean result = flightSearch.runFlightSearch(
            "23/11/2025", "mel", false,
            "22/11/2025", "syd", "economy",  // Return before departure
            1, 0, 0
        );
        
        assertFalse(result, "TC08: Return date before departure should return false");
        assertNull(flightSearch.getDepartureDate(), "Attributes should not be initialized on failure");
    }

    /**
     * TC09 – Invalid seating class
     * Test Data: seatingClass="luxury"
     * Expected Result: false
     * Tests Condition 9: Seating class must be economy, premium economy, business, or first
     */
    @Test
    public void testTC09_InvalidSeatingClass() {
        boolean result = flightSearch.runFlightSearch(
            validFutureDate, "mel", false,
            validReturnDate, "syd", "luxury",  // Invalid seating class
            1, 0, 0
        );
        
        assertFalse(result, "TC09: Invalid seating class should return false");
        assertNull(flightSearch.getDepartureDate(), "Attributes should not be initialized on failure");
    }

    /**
     * TC10 – Emergency row in first class
     * Test Data: emergencyRowSeating=true, seatingClass="first"
     * Expected Result: false
     * Tests Condition 10: Only economy class can have emergency row seating
     */
    @Test
    public void testTC10_EmergencyRowInFirstClass() {
        boolean result = flightSearch.runFlightSearch(
            validFutureDate, "mel", true,  // Emergency row
            validReturnDate, "syd", "first",
            1, 0, 0
        );
        
        assertFalse(result, "TC10: Emergency row in first class should return false");
        assertNull(flightSearch.getDepartureDate(), "Attributes should not be initialized on failure");
    }

    /**
     * TC11 – Invalid airport code
     * Test Data: departureAirportCode="xyz", destinationAirportCode="mel"
     * Expected Result: false
     * Tests Condition 11: Airport codes must be valid (syd, mel, lax, cdg, del, pvg, doh)
     */
    @Test
    public void testTC11_InvalidAirportCode() {
        boolean result = flightSearch.runFlightSearch(
            validFutureDate, "xyz", false,  // Invalid airport code
            validReturnDate, "mel", "economy",
            1, 0, 0
        );
        
        assertFalse(result, "TC11: Invalid airport code should return false");
        assertNull(flightSearch.getDepartureDate(), "Attributes should not be initialized on failure");
    }

    /**
     * TC12A – All valid inputs (Variation A)
     * Test Data: 2 adults, 2 children, 2 infants, economy, non-emergency, valid dates
     * Expected Result: true
     * Tests that valid input with multiple passengers initializes attributes correctly
     */
    @Test
    public void testTC12A_AllValidInputs_MultiplePassengers() {
        boolean result = flightSearch.runFlightSearch(
            validFutureDate, "mel", false,
            validReturnDate, "syd", "economy",
            2, 2, 2  // 2 adults, 2 children, 2 infants (all valid ratios)
        );
        
        assertTrue(result, "TC12A: All valid inputs should return true");
        // Verify all attributes are initialized correctly
        assertEquals(validFutureDate, flightSearch.getDepartureDate());
        assertEquals("mel", flightSearch.getDepartureAirportCode());
        assertFalse(flightSearch.isEmergencyRowSeating());
        assertEquals(validReturnDate, flightSearch.getReturnDate());
        assertEquals("syd", flightSearch.getDestinationAirportCode());
        assertEquals("economy", flightSearch.getSeatingClass());
        assertEquals(2, flightSearch.getAdultPassengerCount());
        assertEquals(2, flightSearch.getChildPassengerCount());
        assertEquals(2, flightSearch.getInfantPassengerCount());
    }

    /**
     * TC12B – All valid inputs (Variation B)
     * Test Data: 1 adult, 1 child, economy, emergencyRow=false
     * Expected Result: true
     * Tests valid input with child passenger in non-emergency economy
     */
    @Test
    public void testTC12B_AllValidInputs_AdultAndChild() {
        boolean result = flightSearch.runFlightSearch(
            validFutureDate, "lax", false,
            validReturnDate, "cdg", "economy",
            1, 1, 0  // 1 adult, 1 child
        );
        
        assertTrue(result, "TC12B: Valid adult and child should return true");
        assertEquals("lax", flightSearch.getDepartureAirportCode());
        assertEquals("cdg", flightSearch.getDestinationAirportCode());
        assertEquals(1, flightSearch.getAdultPassengerCount());
        assertEquals(1, flightSearch.getChildPassengerCount());
        assertEquals(0, flightSearch.getInfantPassengerCount());
    }

    /**
     * TC12C – All valid inputs (Variation C)
     * Test Data: 3 adults, 0 children, 0 infants, business class
     * Expected Result: true
     * Tests valid input with only adults in business class
     */
    @Test
    public void testTC12C_AllValidInputs_AdultsOnlyBusinessClass() {
        boolean result = flightSearch.runFlightSearch(
            validFutureDate, "del", false,
            validReturnDate, "pvg", "business",
            3, 0, 0  // 3 adults only
        );
        
        assertTrue(result, "TC12C: Valid adults-only business class should return true");
        assertEquals("del", flightSearch.getDepartureAirportCode());
        assertEquals("pvg", flightSearch.getDestinationAirportCode());
        assertEquals("business", flightSearch.getSeatingClass());
        assertEquals(3, flightSearch.getAdultPassengerCount());
        assertEquals(0, flightSearch.getChildPassengerCount());
        assertEquals(0, flightSearch.getInfantPassengerCount());
    }

    /**
     * TC12D – All valid inputs (Variation D)
     * Test Data: 1 adult, 1 infant, premium economy, valid dates
     * Expected Result: true
     * Tests valid input with infant in premium economy
     */
    @Test
    public void testTC12D_AllValidInputs_AdultAndInfantPremiumEconomy() {
        boolean result = flightSearch.runFlightSearch(
            validFutureDate, "doh", false,
            validReturnDate, "syd", "premium economy",
            1, 0, 1  // 1 adult, 1 infant
        );
        
        assertTrue(result, "TC12D: Valid adult and infant in premium economy should return true");
        assertEquals("doh", flightSearch.getDepartureAirportCode());
        assertEquals("syd", flightSearch.getDestinationAirportCode());
        assertEquals("premium economy", flightSearch.getSeatingClass());
        assertEquals(1, flightSearch.getAdultPassengerCount());
        assertEquals(0, flightSearch.getChildPassengerCount());
        assertEquals(1, flightSearch.getInfantPassengerCount());
    }
}