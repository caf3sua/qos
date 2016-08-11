package com.viettelperu.qos.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viettelperu.qos.auth.AuthenticationFailedException;
import com.viettelperu.qos.framework.data.BaseJPAServiceImpl;
import com.viettelperu.qos.framework.exception.EmailNotFoundException;
import com.viettelperu.qos.framework.exception.NotFoundException;
import com.viettelperu.qos.model.entity.User;
import com.viettelperu.qos.model.repository.UserRepository;
import com.viettelperu.qos.service.UserService;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Service impl class to implement services for accessing the User object entity.
 * This class acts as an interface between the outer world and the UserRepository
 *
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
@Service
@Transactional
public class UserServiceImpl extends BaseJPAServiceImpl<User, Long> implements UserService {
    private @Autowired UserRepository userRepository;

    @PostConstruct
    public void setupService() {
        this.baseJpaRepository = userRepository;
        this.entityClass = User.class;
        this.baseJpaRepository.setupEntityClass(User.class);
    }


    @Override
    public boolean isValidPass(User user, String rawPass) {
        return User.doesPasswordMatch(rawPass, user.getSalt(), user.getPassword());
    }


    @Override
    public User registerUser(User user, HttpServletRequest request) {
        user.setPassword(User.hashPassword(user.getSalt() + user.getPassword()));
        user.setCurrentLoginAt(new Date());
        user.setCurrentLoginIp(request.getRemoteHost());
        user.setLoginCount(0);
        return userRepository.insert(user);
    }


    @Override
    public User loginUser(final User user, final HttpServletRequest request) {
        user.setLastLoginAt(user.getCurrentLoginAt());
        user.setLastLoginIp(user.getCurrentLoginIp());
        user.setCurrentLoginAt(new Date());
        user.setCurrentLoginIp(request.getRemoteHost());
        user.setLoginCount(user.getLoginCount() + 1);
        user.setUpdatedAt(new Date());

        return userRepository.update(user);
    }

    @Override
    public boolean isEmailExists(String email) {
        if (userRepository.findByEmail(email) != null) {
            return true;
        } else
            return false;
    }


    @Override
    public User findByEmail(String email) throws EmailNotFoundException {
        User user = userRepository.findByEmail(email);

        if(user != null) {
            return user;
        } else {
            throw new EmailNotFoundException("User not found for email: "+email);
        }
    }


	@Override
	public User findByUsername(String username) throws AuthenticationFailedException {
		User user = userRepository.findByUsername(username);

        if(user != null) {
            return user;
        } else {
            throw new EmailNotFoundException("User not found for username: "+username);
        }
	}
}
