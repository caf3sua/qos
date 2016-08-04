package com.viettelperu.qos.model.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.viettelperu.qos.framework.data.BaseHibernateJPARepository;
import com.viettelperu.qos.model.entity.User;
import com.viettelperu.qos.model.repository.UserRepository;

/**
 *
 * User Repository Implementation
 *
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
@Repository
public class UserRepositoryImpl extends BaseHibernateJPARepository<User, Long> implements UserRepository {
    private static Logger LOG = LoggerFactory.getLogger(UserRepositoryImpl.class);

    @Override
    public User findByEmail(String email) {
        return (User) sessionFactory.getCurrentSession().createQuery("from User u where u.email = :email")
                .setParameter("email", email).uniqueResult();
    }
    
    @Override
    public User findByUsername(String username) {
        return (User) sessionFactory.getCurrentSession().createQuery("from User u where u.username = :username")
                .setParameter("username", username).uniqueResult();
    }
}
