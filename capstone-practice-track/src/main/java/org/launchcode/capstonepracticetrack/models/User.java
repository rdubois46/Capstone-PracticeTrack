package org.launchcode.capstonepracticetrack.models;



import org.hibernate.validator.constraints.Email;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class User {

    @GeneratedValue
    @Id
    private int id;

    @NotNull
    @Size(min = 3, max = 15, message = "Username must be 5 - 15 characters." )
    private String username;

    @Email
    private String email;

    @NotNull
    @Size(min = 5, max = 15, message = "Password must be 5 - 15 characters.")
    private String password;

    public User () {

    }

    public User (String username, String password) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}




