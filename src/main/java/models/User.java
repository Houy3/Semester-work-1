package models;

import models.validators.EmailValidator;
import models.validators.PasswordValidator;
import models.validators.Validator;

import java.util.Objects;

public class User {

    private Long id;
    private String email;
    private String passwordHash;
    private AccessRights accessRights;

    private static final Validator<String> emailValidator = new EmailValidator(
            "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$"
    );;
    private static final Validator<String> passwordValidator = new PasswordValidator(
            "^[a-zA-Z0-9]*$", 5, 32
    );


    public User() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPassword(String password) throws IllegalArgumentException {
        passwordValidator.check(password);
        this.passwordHash = String.valueOf(Objects.hash(password));
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws IllegalArgumentException {
        emailValidator.check(email);
        this.email = email;
    }

    public AccessRights getAccessRights() {
        return accessRights;
    }

    public void setAccessRights(AccessRights accessRights) {
        this.accessRights = accessRights;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id) - Objects.hash(email) + Objects.hash(accessRights);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", accessRights=" + accessRights +
                '}';
    }

    public enum AccessRights {
        REGULAR,
        ADMIN
    }

}


