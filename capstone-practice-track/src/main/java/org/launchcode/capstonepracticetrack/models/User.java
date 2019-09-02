package org.launchcode.capstonepracticetrack.models;



import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
public class User implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    @NotBlank
    private String username;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Instrument> instruments = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<PracticeSession> practiceSessions = new ArrayList<>();

    public User () {

    }

    public User(@NotBlank String email, @NotBlank String username, @NotBlank String password) {
        //May not need these validators?
        if (email == null || email.length() == 0 || !isValidEmail(email))
            throw new IllegalArgumentException("Email may not be blank");

        if (username == null || username.length() == 0)
            throw new IllegalArgumentException("username may not be blank");

        if (password == null || password.length() == 0)
            throw new IllegalArgumentException("password may not be blank");

        this.email = email;
        this.username = username;
        this.password = password;
    }

    private static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("\\S+@\\S+");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public List<String> getRoles() {
        ArrayList<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        return roles;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User user = (User) obj;
        return this.username.equals(user.username);
    }

    public void addInstrument(Instrument instrument) {
        this.instruments.add(instrument);
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

    public List<Instrument> getInstruments() {
        return instruments;
    }

    public void setInstruments(List<Instrument> instruments) {
        this.instruments = instruments;
    }

    public List<PracticeSession> getPracticeSessions() {
        return practiceSessions;
    }

    public void setPracticeSessions(List<PracticeSession> practiceSessions) {
        this.practiceSessions = practiceSessions;
    }
}




