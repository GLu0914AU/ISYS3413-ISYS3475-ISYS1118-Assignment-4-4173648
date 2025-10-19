package flight;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Arrays;
import java.util.List;

public class FlightSearch {
   private String  departureDate;
   private String  departureAirportCode;
   private boolean emergencyRowSeating;
   private String  returnDate;
   private String  destinationAirportCode; 
   private String  seatingClass;
   private int     adultPassengerCount;
   private int     childPassengerCount;
   private int     infantPassengerCount;

   // Valid airport codes (all lowercase as per requirement)
   private static final List<String> VALID_AIRPORTS = Arrays.asList("syd", "mel", "lax", "cdg", "del", "pvg", "doh");
   
   // Valid seating classes (all lowercase as per requirement)
   private static final List<String> VALID_SEATING_CLASSES = Arrays.asList("economy", "premium economy", "business", "first");

   /**
    * Validates flight search criteria and initializes class attributes if valid.
    * 
    * @param departureDate Date of departure in DD/MM/YYYY format
    * @param departureAirportCode Three-letter airport code (lowercase)
    * @param emergencyRowSeating Boolean indicating if emergency row seating is requested
    * @param returnDate Return date in DD/MM/YYYY format
    * @param destinationAirportCode Three-letter destination airport code (lowercase)
    * @param seatingClass Class of seating (economy, premium economy, business, first)
    * @param adultPassengerCount Number of adult passengers
    * @param childPassengerCount Number of child passengers (2-11 years)
    * @param infantPassengerCount Number of infant passengers (<2 years)
    * @return true if all validations pass, false otherwise
    */
   public boolean runFlightSearch(String departureDate, String departureAirportCode, boolean emergencyRowSeating, 
                                  String returnDate, String destinationAirportCode, String seatingClass, 
                                  int adultPassengerCount, int childPassengerCount, int infantPassengerCount) {
      
      // Condition 1: Total passengers must be between 1 and 9
      int totalPassengers = adultPassengerCount + childPassengerCount + infantPassengerCount;
      if (totalPassengers < 1 || totalPassengers > 9) {
         return false;
      }

      // Condition 2: Children cannot be seated in emergency row seating or first class
      if (childPassengerCount > 0) {
         if (emergencyRowSeating || "first".equals(seatingClass)) {
            return false;
         }
      }

      // Condition 3: Infants cannot be seated in emergency row seating or business class
      if (infantPassengerCount > 0) {
         if (emergencyRowSeating || "business".equals(seatingClass)) {
            return false;
         }
      }

      // Condition 4: Up to 2 children per adult
      if (childPassengerCount > adultPassengerCount * 2) {
         return false;
      }

      // Condition 5: Only one infant per adult
      if (infantPassengerCount > adultPassengerCount) {
         return false;
      }

      // Condition 6 & 7: Validate departure date format and ensure it's not in the past
      LocalDate departureDateObj = validateAndParseDate(departureDate);
      if (departureDateObj == null || departureDateObj.isBefore(LocalDate.now())) {
         return false;
      }

      // Condition 7 & 8: Validate return date format and ensure it's not before departure
      LocalDate returnDateObj = validateAndParseDate(returnDate);
      if (returnDateObj == null || returnDateObj.isBefore(departureDateObj)) {
         return false;
      }

      // Condition 9: Validate seating class
      if (seatingClass == null || !VALID_SEATING_CLASSES.contains(seatingClass.toLowerCase())) {
         return false;
      }

      // Condition 10: Only economy class can have emergency row seating
      if (emergencyRowSeating && !"economy".equals(seatingClass)) {
         return false;
      }

      // Condition 11: Validate airport codes and ensure they're different
      if (departureAirportCode == null || destinationAirportCode == null ||
          !VALID_AIRPORTS.contains(departureAirportCode.toLowerCase()) ||
          !VALID_AIRPORTS.contains(destinationAirportCode.toLowerCase()) ||
          departureAirportCode.equalsIgnoreCase(destinationAirportCode)) {
         return false;
      }

      // All validations passed - initialize class attributes
      this.departureDate = departureDate;
      this.departureAirportCode = departureAirportCode;
      this.emergencyRowSeating = emergencyRowSeating;
      this.returnDate = returnDate;
      this.destinationAirportCode = destinationAirportCode;
      this.seatingClass = seatingClass;
      this.adultPassengerCount = adultPassengerCount;
      this.childPassengerCount = childPassengerCount;
      this.infantPassengerCount = infantPassengerCount;

      return true;
   }

   /**
    * Validates and parses a date string in DD/MM/YYYY format with strict validation.
    * 
    * @param dateString Date string to validate
    * @return LocalDate object if valid, null otherwise
    */
   private LocalDate validateAndParseDate(String dateString) {
      if (dateString == null || dateString.isEmpty()) {
         return null;
      }

      try {
         // Use strict resolver style to enforce valid dates (e.g., 29/02/2026 would fail)
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu")
                                                       .withResolverStyle(ResolverStyle.STRICT);
         return LocalDate.parse(dateString, formatter);
      } catch (DateTimeParseException e) {
         return null;
      }
   }

   // Getter methods for testing attribute initialization
   public String getDepartureDate() { return departureDate; }
   public String getDepartureAirportCode() { return departureAirportCode; }
   public boolean isEmergencyRowSeating() { return emergencyRowSeating; }
   public String getReturnDate() { return returnDate; }
   public String getDestinationAirportCode() { return destinationAirportCode; }
   public String getSeatingClass() { return seatingClass; }
   public int getAdultPassengerCount() { return adultPassengerCount; }
   public int getChildPassengerCount() { return childPassengerCount; }
   public int getInfantPassengerCount() { return infantPassengerCount; }
}