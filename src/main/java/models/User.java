package models;

import jdbc.SQLAnnotations.*;

import java.util.Objects;

@Table(name = "users")
public class User {

    @Unique
    @Column(name = "id")
    private Long id;

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

    @Unique
    @Column(name = "nickname")
    private String nickname;

    @NotNull
    @Column(name = "access_rights")
    private AccessRights accessRights;



    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }


    public String getEmail() {
        return email;
    }
    public void setEmail(String email) throws IllegalArgumentException {
        this.email = email;
    }


    public String getPasswordHash() {
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash) throws IllegalArgumentException {
        this.passwordHash = passwordHash;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }


    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    public AccessRights getAccessRights() {
        return accessRights;
    }
    public void setAccessRights(AccessRights accessRights) {
        this.accessRights = accessRights;
    }


    @EnumSetter(field_name = "accessRights")
    public void setAccessRights(String accessRights) {
        this.accessRights = AccessRights.valueOf(accessRights);
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


