package it.polimi.ingsw.common;

import java.util.regex.Pattern;

/**
 * Validates a string based on a regex
 */
public class Validator {

    /**
     * The pattern to be checked
     */
    private final Pattern pattern;

    /**
     * Class constructor, compiles the regex to a <code>Pattern</code>
     *
     * @param regex The regex
     */
    public Validator(String regex) {
        pattern = Pattern.compile(regex);
    }

    /**
     * Checks if the string matches the regex
     *
     * @param string The string to be checked
     * @return true if the string matches
     */
    public boolean isValid(String string) {
        return pattern.matcher(string).matches();
    }

}
