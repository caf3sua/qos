package com.viettelperu.qos.controller;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Date;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.viettelperu.qos.auth.AuthenticationFailedException;
import com.viettelperu.qos.auth.JWTTokenAuthFilter;
import com.viettelperu.qos.framework.api.APIResponse;
import com.viettelperu.qos.framework.controller.BaseController;
import com.viettelperu.qos.model.dto.CustomerDTO;
import com.viettelperu.qos.model.dto.UserDTO;
import com.viettelperu.qos.ws.QosWebService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 *
 * Referred: https://github.com/mpetersen/aes-example, http://niels.nu/blog/2015/json-web-tokens.html
 */
@Controller
@RequestMapping("user")
@PropertySource("classpath:config.properties")
public class UserController extends BaseController {
    private static Logger LOG = LoggerFactory.getLogger(UserController.class);
    
    private static final String USE_DOMAIN = "BiTel";

    private @Autowired QosWebService qosWebService;
    
    @Autowired
    private Environment env;
    
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
//        String password = decryptPassword(userDTO);
        
//        String wsUsername = env.getProperty("wsUsername");
//        ComboPooledDataSource dataSource = (ComboPooledDataSource) _applicationContext.getBean("dataSource");
        
//        String encrypted = EncryptionUtil.encrypt("12345678");
//        String decrypted = EncryptionUtil.decrypt(encrypted);
//        GetMSISDNResponse responseSoap = qosWebService.getMSISDN("nam", "12345678", "127.0.0.1");
//        System.out.println(responseSoap);
        
        LOG.info("Looking for user by username: " + userDTO.getUsername());
        //User user = userService.findByUsername(userDTO.getUsername());
        
        HashMap<String, Object> authResp = new HashMap<>();
//        if(userService.isValidPass(user, password)) {
//            LOG.info("User authenticated: "+user.getEmail());
//            userService.loginUser(user, request);
        UserDTO user = new UserDTO();
        user.setUsername("nam");
        user.setIsdn("0976075335");
        
        createAuthResponse(user, authResp);
//        } else {
//            throw new AuthenticationFailedException("Invalid username/password combination");
//        }

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
                                                  HttpServletRequest request, HttpServletResponse response) throws AuthenticationFailedException {
        Validate.isTrue(StringUtils.isNotBlank(userDTO.getIp()), "Ip Address is blank");

        LOG.info("Looking for phone number by IpAddress: " + userDTO.getIp());
//        GetMSISDNResponse responseSoap = qosWebService.getMSISDN("nam", "12345678", "127.0.0.1");
//        System.out.println(responseSoap);
        
        // TODO: dump data
        HashMap<String, Object> authResp = new HashMap<>();
        UserDTO user = new UserDTO();
        if (StringUtils.equals(userDTO.getIp(), "192.168.1.1")) {
        	user.setUsername("quang");
            user.setIsdn("0976000001");
        } else if (StringUtils.equals(userDTO.getIp(), "192.168.1.2")) {
        	user.setUsername("huy");
            user.setIsdn("0976000002");
        } else {
        	throw new AuthenticationFailedException("Cannot find ISDN by IpAddress");
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

        LOG.info("checkSubExistsActive for user by username: " + userDTO.getUsername());
        
        HashMap<String, Object> authResp = new HashMap<>();

        return APIResponse.toOkResponse(authResp);
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
    	Validate.isTrue(StringUtils.isNotBlank(userDTO.getUsername()), "Username/phone is blank");

        LOG.info("Looking for user by username: " + userDTO.getUsername());
        
        CustomerDTO customer = new CustomerDTO();
        customer.setCode("0");
        customer.setCustId(7079706l);
        customer.setFirstName("Thinhdd41");
        customer.setServiceType(1l);
        customer.setStartDatetime(new Date());
        customer.setStatus(2l);
        customer.setTelecomService("M");
        customer.setIsdn("931999896");
        
        return APIResponse.toOkResponse(customer);
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
        String password = decryptPassword(userDTO);

        LOG.info("register for user by email: "+userDTO.getEmail());
        
        return APIResponse.toErrorResponse("This function is not available now");
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

    @RequestMapping(value = "/getLocation/{username}", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse getLocation(@PathVariable String username) throws Exception {
        return APIResponse.toOkResponse("success");
    }
    
    private String decryptPassword(UserDTO userDTO) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, InvalidKeySpecException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        String passPhrase = "biZndDtCMkdeP8K0V15OKMKnSi85";
        String salt = userDTO.getSalt();
        String iv = userDTO.getIv();
        int iterationCount = userDTO.getIterations();
        int keySize = userDTO.getKeySize();

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(passPhrase.toCharArray(), hex(salt), iterationCount, keySize);
        SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(hex(iv)));
        byte[] decrypted = cipher.doFinal(base64(userDTO.getEncryptedPassword()));

        return new String(decrypted, "UTF-8");
    }

    private String base64(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    private byte[] base64(String str) {
        return Base64.decodeBase64(str);
    }

    private String hex(byte[] bytes) {
        return Hex.encodeHexString(bytes);
    }

    private byte[] hex(String str) {
        try {
            return Hex.decodeHex(str.toCharArray());
        }
        catch (DecoderException e) {
            throw new IllegalStateException(e);
        }
    }

	public QosWebService getQosWebService() {
		return qosWebService;
	}

	public void setQosWebService(QosWebService qosWebService) {
		this.qosWebService = qosWebService;
	}
}
