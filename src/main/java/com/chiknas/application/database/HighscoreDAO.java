package com.chiknas.application.database;

import com.chiknas.application.HighScore;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * com.chiknas.application.database.HighscoreDAO, created on 05/07/2019 16:15 <p>
 * @author NikolaosK
 */
@Repository
public interface HighscoreDAO extends PagingAndSortingRepository<HighScore, Long> {
  List<HighScore> findAll(Sort sort);
}
