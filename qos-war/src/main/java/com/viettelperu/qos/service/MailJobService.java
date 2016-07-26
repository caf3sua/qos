package com.viettelperu.qos.service;

import com.viettelperu.qos.framework.data.BaseService;
import com.viettelperu.qos.model.entity.Job;

/**
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
public interface MailJobService extends BaseService<Job, Long> {

    /**
     * Sends the confirmation mail to user.
     *
     * @param user
     */
    public void sendConfirmationMail(String username);
}
