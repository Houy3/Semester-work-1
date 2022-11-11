package models.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record PasswordValidator(String regex, int minLength, int maxLength) implements Validator<String>{


    @Override
    public void check(String object) throws IllegalArgumentException {
        if (object == null || object.equals("")) {
            throw new IllegalArgumentException("Password is empty. ");
        }

        if (object.length() < minLength) {
            throw new IllegalArgumentException("The password must contain at least " + minLength + " characters.");
        }

        if (object.length() > maxLength) {
            throw new IllegalArgumentException("The password must contain no more than " + maxLength + " characters.");
        }

        Pattern pattern2 = Pattern.compile(regex);
        Matcher matcher2 = pattern2.matcher(object);
        if (!matcher2.find()) {
            throw new IllegalArgumentException(
                    """
                    Allowed characters for the password:
                        - Capital Latin letters: from A to Z
                        - Lowercase Latin letters: from a to z
                        - Numbers from 0 to 9
                    """);
        }
    }
}
