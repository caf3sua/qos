package com.viettelperu.qos.model.entity;

/**
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
public class MailerJob extends Job {
    enum MailType {
        CONFIRMATION, PASSWORD_RESET
    }

    public static MailerJob buildMailerJobForUser(String username, MailType mailType) {
        MailerJob mailerJob = new MailerJob();
        //mailerJob.set
        return mailerJob;
    }
}
