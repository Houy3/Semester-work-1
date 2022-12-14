package models.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record NicknameValidator(String regex) implements Validator<String> {

    @Override
    public void check(String object) throws IllegalArgumentException {
        if (object == null || object.equals("")) {
            throw new IllegalArgumentException("Nickname is empty. ");
        }

        Pattern pattern2 = Pattern.compile(regex);
        Matcher matcher2 = pattern2.matcher(object);
        if (!matcher2.find()) {
            throw new IllegalArgumentException(
                    """
                            Allowed characters for the nickname:
                                - Capital Latin letters: from A to Z
                                - Lowercase Latin letters: from a to z
                                - Numbers from 0 to 9
                            """);
        }
    }
}
