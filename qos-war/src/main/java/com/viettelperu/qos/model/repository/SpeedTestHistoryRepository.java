package com.viettelperu.qos.model.repository;

import java.util.List;

import com.viettelperu.qos.framework.data.BaseJPARepository;
import com.viettelperu.qos.model.dto.SearchCriteriaDTO;
import com.viettelperu.qos.model.entity.SpeedTestHistory;

/**
 * CRUD operations come from Base Repo but additional operations can be defined here.
 *
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
public interface SpeedTestHistoryRepository extends BaseJPARepository<SpeedTestHistory, Long> {
    /**
     * Finds a SpeedTestHistory with the given username
     *
     * @param List<SpeedTestHistory>
     * @return
     */
    public List<SpeedTestHistory> findByUsername(String username);

    /**
     * Finds a SpeedTestHistory with the given search criteria
     *
     * @param List<SpeedTestHistory>
     * @return
     */
    public List<SpeedTestHistory> findByCriteria(SearchCriteriaDTO search);
}
