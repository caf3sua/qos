package com.viettelperu.qos.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viettelperu.qos.framework.data.BaseJPAServiceImpl;
import com.viettelperu.qos.framework.exception.NotFoundException;
import com.viettelperu.qos.model.dto.SearchCriteriaDTO;
import com.viettelperu.qos.model.entity.Category;
import com.viettelperu.qos.model.entity.SpeedTestHistory;
import com.viettelperu.qos.model.repository.SpeedTestHistoryRepository;
import com.viettelperu.qos.service.SpeedTestHistoryService;

/**
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
@Service
@Transactional
public class SpeedTestHistoryServiceImpl extends BaseJPAServiceImpl<SpeedTestHistory, Long> implements SpeedTestHistoryService {
    private static Logger LOG = LoggerFactory.getLogger(SpeedTestHistoryServiceImpl.class);

    private @Autowired
    SpeedTestHistoryRepository speedTestHistoryRepository;

    @PostConstruct
    public void setupService() {
        LOG.info("setting up speedTestHistoryService...");
        this.baseJpaRepository = speedTestHistoryRepository;
        this.entityClass = SpeedTestHistory.class;
        this.baseJpaRepository.setupEntityClass(SpeedTestHistory.class);
        LOG.info("speedTestHistoryService created...");
    }

	@Override
	public List<SpeedTestHistory> findByUsername(String username) throws NotFoundException {
		List<SpeedTestHistory> history = speedTestHistoryRepository.findByUsername(username);

//        if(history==null || history.size()==0) {
//            throw new NotFoundException("History for username: "+ username + ", not found");
//        }

        return history;
	}

	@Override
	public List<SpeedTestHistory> findByCriteria(SearchCriteriaDTO search) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public boolean deleteById(Long id) throws NotFoundException {
		try {
			SpeedTestHistory history = findById(id);
			
			if (null == history) {
				throw new NotFoundException("History record not found");
			}
			
			// Remove
			delete(history);
		} catch (Exception e) {
			return false;
		}
		// TODO Auto-generated method stub
		return true;
	}
}
