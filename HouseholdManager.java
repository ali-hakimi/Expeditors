
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HouseholdManager {

    private static final Logger LOGGER = Logger.getLogger(HouseholdManager.class.getName());
    private static final String DEFAULT_FILE_PATH = "input.txt";

    private final Map<String, List<Person>> households;
    private final List<Person> persons;

    public HouseholdManager() {
        persons = new ArrayList<>();
        households = new HashMap<>();
    }

    /**
     * Loads data from a file and processes each line.
     *
     * @param filePath the path to the file to be loaded
     * @return true if the data was loaded successfully, false otherwise
     */
    public boolean loadData(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            filePath = DEFAULT_FILE_PATH;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!processLine(line)) {
                    return false;
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading file: " + filePath, e);
            return false;
        }

        System.out.println(String.format("Data for path %s loaded successfully.", filePath));
        return true;
    }

    /**
     * Processes a single line of CSV data.
     *
     * @param line the line to be processed
     * @return true if the line was processed successfully, false otherwise
     */
    private boolean processLine(String line) {
        String[] parts = parseCSVLine(line);
        if (parts.length != 6) {
            LOGGER.log(Level.SEVERE, "Invalid data: {0} parts count:{1}", new Object[]{line, parts.length});
            return false;
        }

        try {
            var person = new Person(parts[0], parts[1], parts[2], parts[3], parts[4], Integer.parseInt(parts[5]));
            persons.add(person);
            String normalizedAddress = person.getNormalizedAddress();
            households.computeIfAbsent(normalizedAddress, _ -> new ArrayList<>()).add(person);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Error parsing integer: {0}", e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Parses a line of CSV data into an array of strings.
     *
     * @param line the line to be parsed
     * @return an array of strings representing the parsed data
     */
    private String[] parseCSVLine(String line) {
        List<String> parts = new ArrayList<>();
        Matcher matcher = Pattern.compile("\"([^\"]*)\"").matcher(line);
        while (matcher.find()) {
            parts.add(matcher.group(1));
        }
        return parts.toArray(String[]::new);
    }

    public static void main(String[] args) {
        HouseholdManager manager = new HouseholdManager();
        if (manager.loadData("input.txt")) {

            System.out.println("Households and number of occupants:");
            manager.households.forEach((address, occupants) -> {
                System.out.println(address + ": " + occupants.size());
            });

            System.out.println("\nOccupants older than 18, sorted by last name then first name:");
            manager.persons.stream()
                    .filter(p -> p.getAge() > 18)
                    .sorted(Comparator.comparing(Person::getLastName)
                            .thenComparing(Person::getFirstName))
                    .forEach(System.out::println);
        }
    }
}
