package org.launchcode.capstonepracticetrack.user;

import org.launchcode.capstonepracticetrack.forms.UserForm;
import org.launchcode.capstonepracticetrack.models.User;

public interface UserService {
    public User save(UserForm userForm) throws UsernameExistsException;
    public User findByUsername(String username);
}
