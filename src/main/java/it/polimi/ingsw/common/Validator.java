package it.polimi.ingsw.common;

import java.util.regex.Pattern;

public class Validator {

    public static final String PLAYER_NAME = "^[a-zA-Z0-9_-][a-zA-Z0-9_ .-]{1,20}[a-zA-Z0-9_-]$";

    private final Pattern pattern;

    public Validator(String regex) {
        pattern = Pattern.compile(regex);
    }

    public boolean isValid(String string) {
        return pattern.matcher(string).matches();
    }

}
