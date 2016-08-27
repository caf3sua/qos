package com.viettelperu.qos.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.security.SecureRandom;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.viettelperu.qos.framework.controller.BaseController;
import com.viettelperu.qos.model.dto.FileUploadDTO;
import com.viettelperu.qos.util.RandomUtil;

/**
 * File upload creation and get APIs
 *
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
@Controller
@RequestMapping("file")
@PropertySources(@PropertySource("classpath:config.properties"))
public class FileUploadController extends BaseController {
    private static Logger LOG = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private Environment env;
    
    /**
     * Method to get the category by given id
     * GET
     *
     * @param catId
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/download", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity downloadFile(HttpServletRequest request, @RequestParam(required = false) Integer n) throws Exception {
    	// Hard-code
    	String filename = RandomUtil.randomFilename() + ".bin";
    	String primaryType = "application";
    	String subType = "octet-stream";
    	
        // No file found based on the supplied filename
    	int blockSize = 1024;
        if (n != null) {
        	blockSize = n;
        }
        
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[blockSize * 1024];
        random.nextBytes(bytes);
        InputStreamResource isr = new InputStreamResource(new ByteArrayInputStream(bytes));

        // Generate the http headers with the file properties
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("content-disposition", "attachment; filename=" + filename);
        headers.add("Content-Length", String.valueOf(bytes.length));
        headers.add("Accept-Ranges", "bytes");
        headers.setContentType( new MediaType(primaryType, subType) );

        return new ResponseEntity<>(isr, headers, HttpStatus.OK);
    }
    
    /**
     * Method to get the category by given name
     * GET
     *
     * @param catName
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/upload", method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}, headers = "content-type=multipart/*")
    public @ResponseBody
    ResponseEntity uploadFile(MultipartHttpServletRequest request) throws Exception {
        // Get client's origin
        String clientOrigin = request.getHeader("origin");
        
    	// Generate the http headers with the file properties
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE, OPTIONS");
        headers.add("Access-Control-Allow-Headers", "accept, content-type, optional-header");
        
        FileUploadDTO fileUpload = null;
    	try {
            Iterator<String> itr = request.getFileNames();

            while (itr.hasNext()) {
                String uploadedFile = itr.next();
                MultipartFile file = request.getFile(uploadedFile);
                String mimeType = file.getContentType();
                String filename = file.getOriginalFilename();
                fileUpload = new FileUploadDTO(filename, mimeType);

                boolean isWriteFile = Boolean.parseBoolean(env.getProperty("write_file_upload"));
                LOG.info("Key isWriteFile :" + isWriteFile);
                
                if (Boolean.TRUE.equals(isWriteFile)) {
                	// Save file
                    String realPathtoUploads =  env.getProperty("path_to_upload_dir");
                    if(! new File(realPathtoUploads).exists()) {
                        new File(realPathtoUploads).mkdir();
                    }

                    LOG.info("Save file to realPathtoUploads = {}", realPathtoUploads);

                    String orgName = file.getOriginalFilename();
                    String filePath = realPathtoUploads + File.separator + orgName;
                    File dest = new File(filePath);
                    file.transferTo(dest);
                }
            }
        }
        catch (Exception e) {
        	return new ResponseEntity<>(fileUpload, headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(fileUpload, headers, HttpStatus.OK);
    }
}