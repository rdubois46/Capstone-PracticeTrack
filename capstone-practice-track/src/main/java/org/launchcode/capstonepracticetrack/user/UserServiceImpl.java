package org.launchcode.capstonepracticetrack.user;

import org.launchcode.capstonepracticetrack.forms.UserForm;
import org.launchcode.capstonepracticetrack.models.User;
import org.launchcode.capstonepracticetrack.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;
import java.util.List;

public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public User save(UserForm userForm) throws UsernameExistsException {

        User existingUser = userDao.findByUsername(userForm.getUsername());
        if (existingUser != null) {
            throw new UsernameExistsException("That username already exists.");
        }

        User newUser = new User(
                userForm.getEmail(),
                userForm.getUsername(),
                passwordEncoder.encode(userForm.getPassword()));
        userDao.save(newUser);

        return newUser;
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }
}
