package it.polimi.ingsw.common;

import java.util.regex.Pattern;

public class Validator {

    private final Pattern pattern;

    public Validator(String regex) {
        pattern = Pattern.compile(regex);
    }

    public boolean isValid(String string) {
        return pattern.matcher(string).matches();
    }

}
