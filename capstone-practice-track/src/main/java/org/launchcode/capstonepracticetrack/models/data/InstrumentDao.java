package org.launchcode.capstonepracticetrack.models.data;

import org.launchcode.capstonepracticetrack.models.Instrument;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface InstrumentDao extends CrudRepository<Instrument, Integer> {
    List<Instrument> findByUser_id(int id);
    Instrument findByUser_idAndName(int id, String name);
    Instrument findById(int id);
}
