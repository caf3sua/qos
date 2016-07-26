package com.viettelperu.qos.model.repository;

import java.util.List;

import com.viettelperu.qos.framework.data.BaseJPARepository;
import com.viettelperu.qos.model.entity.Job;

/**
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
public interface JobRepository extends BaseJPARepository<Job, Long> {
    /**
     *
     * @param count
     * @return
     */
    public List<Job> fetchNewJobsToBeScheduledForExecutionPerPriority(int count);

    /**
     *
     * @param count
     * @return
     */
    public List<Job> fetchFailedJobsToBeScheduledForExecutionPerPriority(int count);

    /**
     *
     * @param count
     * @return
     */
    public List<Job> fetchNewJobsToBeScheduledForExecutionPerSubmissionTimePriority(int count);

    /**
     *
     * @param count
     * @return
     */
    public List<Job> fetchFailedJobsToBeScheduledForExecutionPerSubmissionTimePriority(int count);
}
