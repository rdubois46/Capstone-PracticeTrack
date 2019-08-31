package org.launchcode.capstonepracticetrack.user;

public class UsernameExistsException extends Exception {

    public UsernameExistsException(String message) {
        super(message);
    }
}
