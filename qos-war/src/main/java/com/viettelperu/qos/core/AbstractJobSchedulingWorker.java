package com.viettelperu.qos.core;

import org.slf4j.Logger;

import com.viettelperu.qos.model.entity.Job;
import com.viettelperu.qos.service.JobService;

import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
public abstract class AbstractJobSchedulingWorker {
    void processResults(Map<Future<Job>, Job> result, JobService jobService, Logger LOG) {
        for(Future<Job> jobFuture : result.keySet()) {
            try {
                Job resultJob = jobFuture.get();
                LOG.info("Job Status: name="+resultJob.getName()+" status="+resultJob.getStatus());
                jobService.update(jobFuture.get());
            } catch (Exception e) {
                e.printStackTrace();
                Job failedJob = result.get(jobFuture);
                failedJob.setStatus(Job.Status.FAILED);
                try {
                    jobService.update(failedJob);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
