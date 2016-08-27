package com.viettelperu.qos.controller;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.viettelperu.qos.auth.AuthenticationFailedException;
import com.viettelperu.qos.auth.JWTTokenAuthFilter;
import com.viettelperu.qos.framework.api.APIResponse;
import com.viettelperu.qos.framework.controller.BaseController;
import com.viettelperu.qos.model.dto.UserDTO;
import com.viettelperu.qos.model.entity.User;
import com.viettelperu.qos.service.UserService;
import com.viettelperu.qos.util.EncryptionUtil;
import com.viettelperu.qos.ws.QosWebService;
import com.viettelperu.qos.ws.wsdl.radius.GetMSISDNResponse;
import com.viettelperu.qos.ws.wsdl.radius.ResultResponse;
import com.viettelperu.qos.ws.wsdl.vascm.CheckSubExistsActiveResponse;
import com.viettelperu.qos.ws.wsdl.vascm.ViewSubscriberByIsdnResponse;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 *
 * Referred: https://github.com/mpetersen/aes-example, http://niels.nu/blog/2015/json-web-tokens.html
 */
@Controller
@RequestMapping("user")
@PropertySources(@PropertySource("classpath:config.properties"))
public class UserController extends BaseController {
    private static Logger LOG = LoggerFactory.getLogger(UserController.class);
    
    private static final String USE_DOMAIN = "BiTel";

    private @Autowired UserService userService;
    private @Autowired QosWebService qosWebService;
    
    private static final String SALT = "d9116fa96df7c0b14da85ff5fdd69743";
    
    @Autowired
    private Environment env;
    
    /** Used to get bean */
    @Autowired
    private ApplicationContext _applicationContext;
    /**
     * Authenticate a user
     *
     * @param userDTO
     * @return
     */
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST, headers = {JSON_API_CONTENT_HEADER})
    public @ResponseBody APIResponse authenticate(@RequestBody UserDTO userDTO,
                                                  HttpServletRequest request, HttpServletResponse response) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, AuthenticationFailedException {
    	Validate.isTrue(StringUtils.isNotBlank(userDTO.getUsername()), "Username/phone is blank");
        Validate.isTrue(StringUtils.isNotBlank(userDTO.getEncryptedPassword()), "Encrypted password is blank");
        String password = EncryptionUtil.decryptPassword(userDTO);

        LOG.info("Looking for user by username: " + userDTO.getUsername());
        User user = userService.findByUsername(userDTO.getUsername());
        
        HashMap<String, Object> authResp = new HashMap<>();
        if(userService.isValidPass(user, password)) {
            LOG.info("User authenticated: "+user.getEmail());
            userService.loginUser(user, request);
            createAuthResponse(user, authResp);
        } else {
            throw new AuthenticationFailedException("Invalid username/password combination");
        }

        return APIResponse.toOkResponse(authResp);
    }
    
    /**
     * Authenticate a user
     *
     * @param userDTO
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/getMSISDN", method = RequestMethod.POST, headers = {JSON_API_CONTENT_HEADER})
    public @ResponseBody APIResponse getMSISDN(@RequestBody UserDTO userDTO,
                                                  HttpServletRequest request, HttpServletResponse response) throws Exception {
        Validate.isTrue(StringUtils.isNotBlank(userDTO.getIp()), "Ip Address is blank");

        LOG.info("Looking for phone number by IpAddress: " + userDTO.getIp());
        HashMap<String, Object> authResp = new HashMap<>();
        UserDTO user = new UserDTO();
        
        boolean dumpMode = Boolean.parseBoolean(env.getProperty("webservice_dump_mode"));
        if (dumpMode) {
        	String applicationType = userDTO.getApplicationType();
        	if (StringUtils.equalsIgnoreCase(applicationType, "Android")) {
        		user.setUsername("0976000001");
                user.setIsdn("0976000001");
            } else if (StringUtils.equalsIgnoreCase(applicationType, "iOs")) {
            	user.setUsername("0976000002");
                user.setIsdn("0976000002");
            } else {
            	throw new AuthenticationFailedException("Cannot find ISDN by IpAddress");
            }
        } else {
        	// Check mode encrypt
        	String modeEncrypt = env.getProperty("mode_enable_encrypt");
            LOG.debug("Mode encrypt :" + modeEncrypt);
	        // Get wsUsername/wsPassword
	    	String wsUsernameEncrypted = env.getProperty("wsUsername_getMSISDN");
	    	String wsUsername = EncryptionUtil.decrypt(wsUsernameEncrypted, modeEncrypt);
	    	String wsPasswordEncrypted = env.getProperty("wsPassword_getMSISDN");
	    	String wsPassword = EncryptionUtil.decrypt(wsPasswordEncrypted, modeEncrypt);
	        GetMSISDNResponse responseSoap = qosWebService.getMSISDN(wsUsername, wsPassword, userDTO.getIp());
	        
	        // Get information
	        ResultResponse result = responseSoap.getReturn();
	        if (null != result) {
	        	if (result.getCode() == 0) {
	                user.setIsdn(result.getDesc());
	                user.setUsername(result.getDesc());
	        	}
	        } else {
	        	throw new AuthenticationFailedException("Cannot find ISDN by IpAddress");
	        }
        }
        
        createAuthResponse(user, authResp);

        return APIResponse.toOkResponse(authResp);
    }
    
    /**
     * Authenticate a user
     *
     * @param userDTO
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/checkSubExistsActive", method = RequestMethod.POST, headers = {JSON_API_CONTENT_HEADER})
    public @ResponseBody APIResponse checkSubExistsActive(@RequestBody UserDTO userDTO,
                                                  HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Validate.isTrue(StringUtils.isNotBlank(userDTO.getUsername()), "Username/phone is blank");

        LOG.info("checkSubExistsActive for ISDN: " + userDTO.getIsdn());
        
    	// Check mode encrypt
    	String modeEncrypt = env.getProperty("mode_enable_encrypt");
        LOG.debug("Mode encrypt :" + modeEncrypt);
        // Get wsUsername/wsPassword
    	String wsUsernameEncrypted = env.getProperty("wsUsername_checkSubExistsActive");
    	String wsUsername = EncryptionUtil.decrypt(wsUsernameEncrypted, modeEncrypt);
    	String wsPasswordEncrypted = env.getProperty("wsPassword_checkSubExistsActive");
    	String wsPassword = EncryptionUtil.decrypt(wsPasswordEncrypted, modeEncrypt);
        
    	LOG.info("Looking for user by ISDN: " + userDTO.getIsdn());
    	CheckSubExistsActiveResponse responseSoap = qosWebService.checkSubExistsActive(wsUsername, wsPassword, userDTO.getIsdn());

        return APIResponse.toOkResponse(responseSoap);
    }
    
    /**
     * Authenticate a user
     *
     * @param userDTO
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/viewSubscriberByIsdn", method = RequestMethod.POST, headers = {JSON_API_CONTENT_HEADER})
    public @ResponseBody APIResponse viewSubscriberByIsdn(@RequestBody UserDTO userDTO,
                                                  HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Validate.isTrue(StringUtils.isNotBlank(userDTO.getIsdn()), "Username/phone is blank");

    	// Check mode encrypt
    	String modeEncrypt = env.getProperty("mode_enable_encrypt");
        LOG.debug("Mode encrypt :" + modeEncrypt);
        
    	// Get wsUsername/wsPassword
    	String wsUsernameEncrypted = env.getProperty("wsUsername_viewSubscriberByIsdn");
    	String wsUsername = EncryptionUtil.decrypt(wsUsernameEncrypted, modeEncrypt);
    	String wsPasswordEncrypted = env.getProperty("wsPassword_viewSubscriberByIsdn");
    	String wsPassword = EncryptionUtil.decrypt(wsPasswordEncrypted, modeEncrypt);
    	
        LOG.info("Looking for user by ISDN: " + userDTO.getIsdn());
        ViewSubscriberByIsdnResponse responseSoap = qosWebService.viewSubscriberByIsdn(wsUsername, wsPassword, userDTO.getIsdn());
        
        return APIResponse.toOkResponse(responseSoap);
    }
    

    /**
     * Register new user
     * POST body expected in the format - {"user":{"userName":"User Name", "email":"something@somewhere.com"}}
     *
     * @param userDTO
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST, headers = {JSON_API_CONTENT_HEADER})
    public @ResponseBody APIResponse register(@RequestBody UserDTO userDTO,
                                              HttpServletRequest request) throws NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException {
    	Validate.isTrue(StringUtils.isNotBlank(userDTO.getEmail()), "Email is blank");
        Validate.isTrue(StringUtils.isNotBlank(userDTO.getEncryptedPassword()), "Encrypted password is blank");
        Validate.isTrue(StringUtils.isNotBlank(userDTO.getUsername()), "User name is blank");
        String password = EncryptionUtil.decryptPassword(userDTO);

        LOG.info("Looking for user by email: "+userDTO.getEmail());
        if(userService.isEmailExists(userDTO.getEmail())) {
            return APIResponse.toErrorResponse("Email is taken");
        }

        LOG.info("Creating user: "+userDTO.getEmail());
        
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        user.setPassword(password);
        user.setEnabled(true);
        user.setRole(User.Role.USER);
        // Encrypt
        user.setAlgorithm("SHA1");
        user.setSalt(SALT);
        
        userService.registerUser(user, request);

        HashMap<String, Object> authResp = new HashMap<>();
        createAuthResponse(user, authResp);

        return APIResponse.toOkResponse(authResp);
    }

    private void createAuthResponse(User user, HashMap<String, Object> authResp) {
        String token = Jwts.builder()
        		.setSubject(user.getUsername())
                .claim("role", USE_DOMAIN)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, JWTTokenAuthFilter.JWT_KEY).compact();
        authResp.put("token", token);
        authResp.put("user", user);
    }
    
    private void createAuthResponse(UserDTO user, HashMap<String, Object> authResp) {
        String token = Jwts.builder()
        		.setSubject(user.getUsername())
                .claim("role", USE_DOMAIN)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, JWTTokenAuthFilter.JWT_KEY).compact();
        authResp.put("token", token);
        authResp.put("user", user);
    }

    /**
     * Logs out a user by deleting the session
     *
     * @param userDTO
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.DELETE)
    public @ResponseBody APIResponse logout(@RequestBody UserDTO userDTO) {
        return APIResponse.toOkResponse("success");
    }

    public QosWebService getQosWebService() {
		return qosWebService;
	}

	public void setQosWebService(QosWebService qosWebService) {
		this.qosWebService = qosWebService;
	}
}
