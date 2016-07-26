package com.viettelperu.qos.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viettelperu.qos.framework.data.BaseJPAServiceImpl;
import com.viettelperu.qos.model.entity.Job;
import com.viettelperu.qos.model.repository.JobRepository;
import com.viettelperu.qos.service.JobService;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
@Service
@Transactional
public class JobServiceImpl extends BaseJPAServiceImpl<Job, Long> implements JobService {
    private static Logger LOG = LoggerFactory.getLogger(JobServiceImpl.class);

    protected @Autowired
    JobRepository jobRepository;

    @PostConstruct
    public void setupService() {
        LOG.info("setting up jobService...");
        this.baseJpaRepository = jobRepository;
        this.entityClass = Job.class;
        this.baseJpaRepository.setupEntityClass(Job.class);
        LOG.info("jobService created...");
    }

    @Override
    public List<Job> fetchNewJobsToBeScheduledForExecutionPerPriority(int count) {
        return jobRepository.fetchNewJobsToBeScheduledForExecutionPerPriority(count);
    }

    @Override
    public List<Job> fetchFailedJobsToBeScheduledForExecutionPerPriority(int count) {
        return jobRepository.fetchFailedJobsToBeScheduledForExecutionPerPriority(count);
    }

    @Override
    public List<Job> fetchNewJobsToBeScheduledForExecutionPerSubmissionTimePriority(int count) {
        return jobRepository.fetchNewJobsToBeScheduledForExecutionPerSubmissionTimePriority(count);
    }

    @Override
    public List<Job> fetchFailedJobsToBeScheduledForExecutionPerSubmissionTimePriority(int count) {
        return jobRepository.fetchFailedJobsToBeScheduledForExecutionPerSubmissionTimePriority(count);
    }
}