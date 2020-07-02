package it.polimi.ingsw.client.data;

import java.util.Optional;

public class LoginData extends AbstractData {

    /**
     * The player's name
     */
    private final String name;

    /**
     * The player's age
     */
    private final Integer age;

    public LoginData(String lastMessage, String name, Integer age) {
        super(lastMessage);

        this.name = name;
        this.age = age;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<Integer> getAge() {
        return Optional.ofNullable(age);
    }

    /**
     * Create a new Login data with a given name
     * @param name The name
     * @return The new LoginData
     */
    public LoginData withName(String name) {
        return new LoginData(null, name, age);
    }

    /**
     * Create a new Login data with a given age
     * @param age The age
     * @return The new LoginData
     */
    public LoginData withAge(int age) {
        return new LoginData(null, name, age);
    }

}
