package models.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NicknameValidator implements Validator<String>{

    public final String regex;

    public NicknameValidator(String regex) {
        this.regex = regex;
    }
    @Override
    public void check(String object) throws IllegalArgumentException {
        if (object == null || object.equals("")) {
            throw new IllegalArgumentException("Ник не заполнен");
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
