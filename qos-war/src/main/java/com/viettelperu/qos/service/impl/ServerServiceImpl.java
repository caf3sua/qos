package com.viettelperu.qos.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viettelperu.qos.framework.data.BaseJPAServiceImpl;
import com.viettelperu.qos.model.entity.Server;
import com.viettelperu.qos.model.repository.ServerRepository;
import com.viettelperu.qos.service.ServerService;

/**
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
@Service
@Transactional
public class ServerServiceImpl extends BaseJPAServiceImpl<Server, Long> implements ServerService {
    private static Logger LOG = LoggerFactory.getLogger(ServerServiceImpl.class);

    private @Autowired
    ServerRepository serverRepository;

    @PostConstruct
    public void setupService() {
        LOG.info("setting up categoryService...");
        this.baseJpaRepository = serverRepository;
        this.entityClass = Server.class;
        this.baseJpaRepository.setupEntityClass(Server.class);
        LOG.info("categoryService created...");
    }

	@Override
	public List<Server> findAll() {
		return serverRepository.findAll();
	}

}
