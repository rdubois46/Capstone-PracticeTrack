package org.launchcode.capstonepracticetrack.models.data;

import org.launchcode.capstonepracticetrack.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface UserDao extends CrudRepository <User, Integer> {
    public User findByUsername(String username);
}
