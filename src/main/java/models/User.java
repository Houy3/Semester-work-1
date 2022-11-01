package models;

import jdbc.SQLAnnotations.*;
import jdbc.SQLAnnotations.Enum;
import models.encryptors.Encryptor;
import models.encryptors.PasswordEncryptor;
import models.validators.EmailValidator;
import models.validators.NicknameValidator;
import models.validators.PasswordValidator;
import models.validators.Validator;

import java.util.Objects;

@Table(name = "users")
public class User {

    @Id(isInsertReturnId = false)
    @Column(name = "id")
    private Long id;


    @NotNull
    @Unique
    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @NotNull
    @Unique
    @Column(name = "nickname")
    private String nickname;

    @NotNull
    @Enum
    @Column(name = "access_rights")
    private AccessRights accessRights;

    private static final Validator<String> emailValidator = new EmailValidator(
            "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$"
    );;
    private static final Validator<String> passwordValidator = new PasswordValidator(
            "^[a-zA-Z0-9]*$", 5, 32
    );
    private static final Encryptor<String> passwordEncryptor = new PasswordEncryptor();

    private static final Validator<String> nicknameValidator = new NicknameValidator(
            "^[a-zA-Z0-9]*$"
    );

    public User() {}

    @Getter(field_name = "id")
    public Long getId() {
        return id;
    }

    @Setter(field_name = "id")
    public void setId(Long id) {
        this.id = id;
    }

    @Getter(field_name = "email")
    public String getEmail() {
        return email;
    }

    @Setter(field_name = "email")
    public void setEmail(String email) throws IllegalArgumentException {
        emailValidator.check(email);
        this.email = email;
    }

    @Getter(field_name = "passwordHash")
    public String getPasswordHash() {
        return passwordHash;
    }

    @Setter(field_name = "passwordHash")
    public void setPasswordHash(String passwordHash) throws IllegalArgumentException {
        this.passwordHash = passwordHash;
    }

    public void setPassword(String password) throws IllegalArgumentException {
        passwordValidator.check(password);
        this.passwordHash = passwordEncryptor.encrypt(password);
    }

    @Getter(field_name = "name")
    public String getName() {
        return name;
    }

    @Setter(field_name = "name")
    public void setName(String name) {
        this.name = name;
    }

    @Getter(field_name = "surname")
    public String getSurname() {
        return surname;
    }

    @Setter(field_name = "surname")
    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Getter(field_name = "nickname")
    public String getNickname() {
        return nickname;
    }

    @Setter(field_name = "nickname")
    public void setNickname(String nickname) {
        nicknameValidator.check(nickname);
        this.nickname = nickname;
    }

    @Getter(field_name = "accessRights")
    public AccessRights getAccessRights() {
        return accessRights;
    }

    @Setter(field_name = "accessRights")
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
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", nickname='" + nickname + '\'' +
                ", accessRights=" + accessRights +
                '}';
    }

    public enum AccessRights {
        REGULAR,
        ADMIN
    }

}


