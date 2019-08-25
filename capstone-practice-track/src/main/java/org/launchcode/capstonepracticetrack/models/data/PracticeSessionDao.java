package org.launchcode.capstonepracticetrack.models.data;

import org.launchcode.capstonepracticetrack.models.PracticeSession;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface PracticeSessionDao extends CrudRepository<PracticeSession, Integer> {

    public List<PracticeSession> findByInstrument_idOrderByIdDesc(int id);

    public List<PracticeSession> findByUser_id(int id);

    public List<PracticeSession> findByInstrument_id(int id);


}
