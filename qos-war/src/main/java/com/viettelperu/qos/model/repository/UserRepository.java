package com.viettelperu.qos.model.repository;

import com.viettelperu.qos.framework.data.BaseJPARepository;
import com.viettelperu.qos.model.entity.User;

/**
 *
 * DD Repository for User related actions and events
 *
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
public interface UserRepository extends BaseJPARepository<User, Long> {
    /**
     * Finds a user with the given email
     *
     * @param email
     * @return
     */
    public User findByEmail(String email);
    
    /**
     * Finds a user with the given email
     *
     * @param email
     * @return
     */
    public User findByUsername(String email);
}
