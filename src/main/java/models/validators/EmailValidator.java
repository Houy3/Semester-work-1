package models.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements Validator<String>{

    private final String regex;

    public EmailValidator(String regex) {
        this.regex = regex;
    }

    @Override
    public void check(String object) throws IllegalArgumentException {
        if (object == null || object.equals("")) {
            throw new IllegalArgumentException("Почта не заполнена");
        }

        Pattern pattern2 = Pattern.compile(regex);
        Matcher matcher2 = pattern2.matcher(object);
        if (!matcher2.find()) {
            throw new IllegalArgumentException("Некорректная почта");
        }
    }
}
