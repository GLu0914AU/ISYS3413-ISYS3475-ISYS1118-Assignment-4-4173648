# Flight Search Project

## Overview
The Flight Search Project is a Java application designed to facilitate flight searches based on various parameters such as departure date, airport codes, seating preferences, and passenger counts. 

## Project Structure
```
FlightSearchProject
├── pom.xml
├── .gitignore
├── README.md
├── src
│   ├── main
│   │   └── java
│   │       └── flight
│   │           └── FlightSearch.java
│   └── test
│       └── java
│           └── flight
│               └── FlightSearchTest.java
```

## Setup Instructions
1. **Clone the Repository**
   ```bash
   git clone <repository-url>
   cd FlightSearchProject
   ```

2. **Build the Project**
   Ensure you have Maven installed. Run the following command to build the project:
   ```bash
   mvn clean install
   ```

3. **Run the Application**
   You can run the application using the following command:
   ```bash
   mvn exec:java -Dexec.mainClass="flight.FlightSearch"
   ```

## Usage
To use the `FlightSearch` class, create an instance and call the `runFlightSearch` method with the appropriate parameters. The method will validate the input and initialize the class attributes if the parameters are valid.

## Testing
Unit tests for the `FlightSearch` class are located in the `src/test/java/flight/FlightSearchTest.java` file. You can run the tests using:
```bash
mvn test
```

## Contributing
Contributions are welcome! Please submit a pull request or open an issue for any enhancements or bug fixes.

## License
This project is licensed under the MIT License. See the LICENSE file for more details.