package com.viettelperu.qos.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.viettelperu.qos.framework.data.JPAEntity;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


/**
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "QOS_USER", indexes = {  @Index(name="email_idx", columnList = "email", unique = true),
					@Index(name="mobilePhone_idx", columnList = "mobile_phone", unique = true),
                    @Index(name="username_idx", columnList = "username", unique = true) })
public class User extends JPAEntity<Long> implements Serializable {
	private static final long serialVersionUID = 8454862946814331767L;

	public enum Role {
        USER,
        ADMIN
    }

    private String email;
    private @JsonIgnore String password;
    private boolean enabled;
    private Role role;
    private String username;
    private String mobilePhone;

    private @JsonIgnore Integer loginCount;
    private Date currentLoginAt;
    private Date lastLoginAt;
    private @JsonIgnore String currentLoginIp;
    private @JsonIgnore String lastLoginIp;

    private static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Column @Email @NotNull @NotBlank
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore @Column(nullable = false, length = 60)
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(nullable = false)
    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Column(nullable = false)
    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Column
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    @Column(name="mobile_phone")
    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    @JsonIgnore @Column
    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    @Column
    public Date getCurrentLoginAt() {
        return currentLoginAt;
    }

    public void setCurrentLoginAt(Date currentLoginAt) {
        this.currentLoginAt = currentLoginAt;
    }

    @Column
    public Date getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Date lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    @JsonIgnore @Column
    public String getCurrentLoginIp() {
        return currentLoginIp;
    }

    public void setCurrentLoginIp(String currentLoginIp) {
        this.currentLoginIp = currentLoginIp;
    }

    @JsonIgnore @Column
    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    /**
     * Method to create the hash of the password before storing
     *
     * @param pass
     *
     * @return SHA hash digest of the password
     */
    public static synchronized String hashPassword(String pass) {
        return passwordEncoder.encode(pass);
    }

    public static synchronized boolean doesPasswordMatch(String rawPass, String encodedPass) {
        return passwordEncoder.matches(rawPass, encodedPass);
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", role=" + role +
                ", username='" + username + '\'' +
                ", loginCount=" + loginCount +
                ", currentLoginAt=" + currentLoginAt +
                ", lastLoginAt=" + lastLoginAt +
                ", currentLoginIp='" + currentLoginIp + '\'' +
                ", lastLoginIp='" + lastLoginIp + '\'' +
                '}';
    }
}
