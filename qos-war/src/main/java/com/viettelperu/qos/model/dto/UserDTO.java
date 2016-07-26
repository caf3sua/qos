package com.viettelperu.qos.model.dto;

/**
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
public class UserDTO {
	String ip;
	String isdn;
    String email;
    String password;
    String username;
    String encryptedPassword;
    String iv;
    String salt;
    int keySize;
    int iterations;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getKeySize() {
        return keySize;
    }

    public void setKeySize(int keySize) {
        this.keySize = keySize;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the isdn
	 */
	public String getIsdn() {
		return isdn;
	}

	/**
	 * @param isdn the isdn to set
	 */
	public void setIsdn(String isdn) {
		this.isdn = isdn;
	}
    
}
