package com.viettelperu.qos.service.impl;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viettelperu.qos.framework.data.BaseJPAServiceImpl;
import com.viettelperu.qos.model.entity.Job;
import com.viettelperu.qos.model.repository.JobRepository;
import com.viettelperu.qos.service.MailJobService;

/**
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
@Service
@Transactional
public class MailJobServiceImpl extends BaseJPAServiceImpl<Job, Long> implements MailJobService {
    private static Logger LOG = LoggerFactory.getLogger(MailJobServiceImpl.class);

    protected @Autowired
    JobRepository jobRepository;

    @PostConstruct
    public void setupService() {
        LOG.info("setting up mailJobService...");
        this.baseJpaRepository = jobRepository;
        this.entityClass = Job.class;
        this.baseJpaRepository.setupEntityClass(Job.class);
        LOG.info("mailJobService created...");
    }

    @Override
    public void sendConfirmationMail(String username) {

    }
}
