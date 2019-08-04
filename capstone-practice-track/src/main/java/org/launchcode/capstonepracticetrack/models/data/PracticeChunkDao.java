package org.launchcode.capstonepracticetrack.models.data;

import org.launchcode.capstonepracticetrack.models.PracticeChunk;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface PracticeChunkDao extends CrudRepository<PracticeChunk, Integer> {
}
