package models.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record EmailValidator(String regex) implements Validator<String>{

    @Override
    public void check(String object) throws IllegalArgumentException {
        if (object == null || object.equals("")) {
            throw new IllegalArgumentException("Email is empty. ");
        }

        Pattern pattern2 = Pattern.compile(regex);
        Matcher matcher2 = pattern2.matcher(object);
        if (!matcher2.find()) {
            throw new IllegalArgumentException("Email is incorrect. ");
        }
    }
}
