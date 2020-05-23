package it.polimi.ingsw.client.data;

import java.util.Optional;

public class LoginData extends AbstractData {

    private final String name;
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

    public LoginData withName(String name) {
        return new LoginData(null, name, age);
    }

    public LoginData withAge(int age) {
        return new LoginData(null, name, age);
    }

}
