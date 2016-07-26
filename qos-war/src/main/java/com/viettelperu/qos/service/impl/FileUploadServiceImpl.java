package com.viettelperu.qos.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viettelperu.qos.framework.data.BaseJPAServiceImpl;
import com.viettelperu.qos.framework.exception.NotFoundException;
import com.viettelperu.qos.model.entity.Category;
import com.viettelperu.qos.model.entity.FileUpload;
import com.viettelperu.qos.model.repository.FileUploadRepository;
import com.viettelperu.qos.service.FileUploadService;

/**
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
@Service
@Transactional
public class FileUploadServiceImpl extends BaseJPAServiceImpl<FileUpload, Long> implements FileUploadService {
    private static Logger LOG = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    private @Autowired
    FileUploadRepository fileUploadRepository;

    @PostConstruct
    public void setupService() {
        LOG.info("setting up categoryService...");
        this.baseJpaRepository = fileUploadRepository;
        this.entityClass = FileUpload.class;
        this.baseJpaRepository.setupEntityClass(FileUpload.class);
        LOG.info("categoryService created...");
    }


    @Override
    public boolean isCategoryPresent(String categoryName) {
        if (fileUploadRepository.findByCategoryName(categoryName) != null) {
            return true;
        } else
            return false;
    }

    @Override
    public boolean isPriorityPresent(Integer categoryPriority) {
        if (fileUploadRepository.findByCategoryPriority(categoryPriority) != null) {
            return true;
        } else
            return false;
    }

    @Override
    public FileUpload findByCategoryName(String categoryName) throws NotFoundException {
        return null;
    }

    @Override
    public List<FileUpload> findSubCategories(FileUpload parentCategory) throws NotFoundException {
        return null;
    }


	@Override
	public FileUpload findByFilename(String filename) throws NotFoundException {
		return fileUploadRepository.findByFilename(filename);
	}


	@Override
	public FileUpload uploadFile(FileUpload file) throws NotFoundException {
		return fileUploadRepository.insert(file);
	}
}
