package org.launchcode.capstonepracticetrack.models.data;

import org.launchcode.capstonepracticetrack.models.Instrument;
import org.launchcode.capstonepracticetrack.models.Session;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface SessionDao extends CrudRepository<Session, Integer> {

    public List<Session> findByInstrument_idOrderByIdDesc(int id);

    public List<Session> findByUser_id(int id);

    public List<Session> findByInstrument_id(int id);


}
