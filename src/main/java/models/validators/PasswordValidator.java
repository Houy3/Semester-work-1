package models.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator implements Validator<String>{

    private final String regex;
    private final int minLength;
    private final int maxLength;


    public PasswordValidator(String regex, int minLength, int maxLength) {
        this.regex = regex;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }


    @Override
    public void check(String object) throws IllegalArgumentException {
        if (object == null || object.equals("")) {
            throw new IllegalArgumentException("Пароль не заполнен");
        }

        if (object.length() < minLength) {
            throw new IllegalArgumentException("Пароль должен содержать не менее " + minLength + " символов");
        }

        if (object.length() > maxLength) {
            throw new IllegalArgumentException("Пароль должен содержать не более " + maxLength + " символов");
        }

        Pattern pattern2 = Pattern.compile(regex);
        Matcher matcher2 = pattern2.matcher(object);
        if (!matcher2.find()) {
            throw new IllegalArgumentException(
                    """
                    Разрешённые символы для пароля:
                    - Заглавные латинские буквы: от A до Z
                    - Строчные латинские буквы: от a до z
                    - Цифры от 0 до 9""");
        }
    }
}
