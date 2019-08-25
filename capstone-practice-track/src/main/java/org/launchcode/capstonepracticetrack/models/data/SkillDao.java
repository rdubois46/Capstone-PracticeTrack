package org.launchcode.capstonepracticetrack.models.data;

import org.launchcode.capstonepracticetrack.models.Skill;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface SkillDao extends CrudRepository<Skill, Integer> {
    public List<Skill> findByInstrument_id(int id);
    public List<Skill> findByInstrument_idAndName(int id, String name);
}
