package org.launchcode.capstonepracticetrack.forms;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserForm {
    @NotNull
    @Email(message = "Not a valid email address.")
    private String email;

    @NotNull
    @Size(min = 3, max = 20, message = "Username must be 5 - 20 characters." )
    private String fullName;

    @NotNull
    @Size(min = 5, max = 20, message = "Password must be 5 - 20 characters.")
    private String password;

    @NotNull(message = "Passwords do not match")
    private String verifyPassword;

    public UserForm() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        checkPasswordForRegistration();
    }

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
        checkPasswordForRegistration();
    }

    private void checkPasswordForRegistration() {
        if (!getPassword().equals(verifyPassword)) {
            verifyPassword = null;
        }
    }

}
