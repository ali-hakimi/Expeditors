
import java.text.Normalizer;

public class Person {

    private final String firstName;
    private final String lastName;
    private final String address;
    private final String city;
    private final String state;
    private final int age;

    public Person(String firstName, String lastName, String address, String city, String state, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.state = state;
        this.age = age;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getAge() {
        return age;
    }

    public String getFullAddress() {
        return address + ", " + city + ", " + state;
    }

    /**
     * Normalizes an address by removing punctuation and converting to
     * lowercase.
     *
     * @param address the address to be normalized
     * @return the normalized address
     */
    public String getNormalizedAddress() {
        return Normalizer.normalize(this.getFullAddress(), Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("[\\p{Punct}]", "")
                .replaceAll("\\s+", " ")
                .toLowerCase()
                .trim();
    }

    @Override
    public String toString() {
        return capitalize(firstName) + " " + capitalize(lastName) + ", " + address + ", " + capitalize(city) + ", " + state.toUpperCase() + ", " + age;
    }

    // Capitalize the first letter of a string
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}
