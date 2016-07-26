package com.viettelperu.qos.model.repository.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.viettelperu.qos.framework.data.BaseHibernateJPARepository;
import com.viettelperu.qos.model.dto.SearchCriteriaDTO;
import com.viettelperu.qos.model.entity.SpeedTestHistory;
import com.viettelperu.qos.model.repository.SpeedTestHistoryRepository;

/**
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
@Repository
public class SpeedTestHistoryRepositoryImp extends BaseHibernateJPARepository<SpeedTestHistory, Long> implements SpeedTestHistoryRepository {
    private static Logger LOG = LoggerFactory.getLogger(SpeedTestHistoryRepositoryImp.class);

    @PostConstruct
    public void setUp() {
        LOG.info("SpeedTestHistoryRepository created..!");
    }

	@SuppressWarnings("unchecked")
	@Override
	public List<SpeedTestHistory> findByUsername(String username) {
//		.addOrder(Order.desc("id"))
//        .setMaxResults(10)
//        .list();
		return (List<SpeedTestHistory>) sessionFactory.getCurrentSession().createQuery("from SpeedTestHistory s where s.userName = :username order by s.id desc")
                .setParameter("username", username).list();
	}

	@Override
	public List<SpeedTestHistory> findByCriteria(SearchCriteriaDTO search) {
		// TODO Auto-generated method stub
		return null;
	}
}
