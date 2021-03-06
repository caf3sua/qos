package com.viettelperu.qos.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.viettelperu.qos.framework.api.APIResponse;
import com.viettelperu.qos.framework.controller.BaseController;
import com.viettelperu.qos.model.dto.JobDTO;
import com.viettelperu.qos.model.entity.Category;
import com.viettelperu.qos.model.entity.Job;
import com.viettelperu.qos.service.CategoryService;
import com.viettelperu.qos.service.JobService;

import java.util.Date;

/**
 * Job submission and status APIs
 *
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
@Controller
@RequestMapping("job")
public class JobController extends BaseController {
    private static Logger LOG = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private JobService jobService;

    @Autowired
    private CategoryService categoryService;

    /**
     * Method to handle creation of the job by extracting the jobInfo json from
     * POST body expected in the format - {"name":"job1", "metadataJsonString":"{}", "callbackUrl":"", "catId":1}
     *
     * @param jobDTO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST, headers = {JSON_API_CONTENT_HEADER})
    public @ResponseBody
    APIResponse submitJob(@RequestBody JobDTO jobDTO) throws Exception {
    	LOG.info("Submit job: "+ jobDTO);
        Long catId = jobDTO.getCatId();

        if(catId==null) {
            throw new IllegalArgumentException("categoryId is required to prioritize");
        }

        Category category = categoryService.findById(catId);

        Job job = new Job();
        job.setName(jobDTO.getName());
        job.setMetadataJson(jobDTO.getMetadataJsonString());
        job.setCategory(category);
        job.setCallbackUrl(jobDTO.getCallbackURL());
        job.setSubmitTime(new Date(System.currentTimeMillis()));
        job.setStatus(Job.Status.NEW);
        job.setRetryCount(0);

        jobService.insert(job);
        return APIResponse.toOkResponse(job);
    }

    /**
     * Method to get the status of a job by given id
     * GET
     *
     * @param jobId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/status/{jobId}", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse jobStatus(@PathVariable Long jobId) throws Exception {
        Job job = jobService.findById(jobId);
        return APIResponse.toOkResponse(job);
    }
}
