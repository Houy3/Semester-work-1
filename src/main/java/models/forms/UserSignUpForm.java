package models.forms;

import java.util.Objects;

public class UserSignUpForm {

    private String email;

    private String passwordHash;

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserSignUpForm(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserSignUpForm that = (UserSignUpForm) o;

        if (!Objects.equals(email, that.email)) return false;
        return Objects.equals(passwordHash, that.passwordHash);
    }

    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + (passwordHash != null ? passwordHash.hashCode() : 0);
        return result;
    }
}
