package org.launchcode.capstonepracticetrack.models.data;

import org.launchcode.capstonepracticetrack.models.Instrument;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface InstrumentDao extends CrudRepository<Instrument, Integer> {

}
