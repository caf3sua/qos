package com.viettelperu.qos.model.repository.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.Criteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.viettelperu.qos.framework.data.BaseHibernateJPARepository;
import com.viettelperu.qos.model.entity.Server;
import com.viettelperu.qos.model.repository.ServerRepository;

/**
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
@Repository
public class ServerRepositoryImpl extends BaseHibernateJPARepository<Server, Long> implements ServerRepository {
    private static Logger LOG = LoggerFactory.getLogger(ServerRepositoryImpl.class);

    @PostConstruct
    public void setUp() {
        LOG.info("ServerRepository created..!");
    }

	@SuppressWarnings("unchecked")
	@Override
	public List<Server> findAll() {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(clazz);
        return c.list();
	}
}
