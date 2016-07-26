package com.viettelperu.qos.service;

import java.util.List;

import com.viettelperu.qos.framework.data.BaseService;
import com.viettelperu.qos.framework.exception.NotFoundException;
import com.viettelperu.qos.model.dto.SearchCriteriaDTO;
import com.viettelperu.qos.model.entity.SpeedTestHistory;

/**
 * Brings in the basic CRUD service ops from BaseService. Insert additional ops here.
 *
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
public interface SpeedTestHistoryService extends BaseService<SpeedTestHistory, Long> {

    /**
     * Find SpeedTestHistory by username
     *
     * @param List<SpeedTestHistory>
     * @return
     */
    public List<SpeedTestHistory> findByUsername(String username) throws NotFoundException;

    /**
     * Find sub categories by parent category
     *
     * @param parentCategory
     * @return
     */
    public List<SpeedTestHistory> findByCriteria(SearchCriteriaDTO search) throws NotFoundException;
    
    /**
     * Find sub categories by parent category
     *
     * @param parentCategory
     * @return
     */
    public boolean deleteById(Long id) throws NotFoundException;
}
