package com.viettelperu.qos.service;

import java.util.List;

import com.viettelperu.qos.framework.data.BaseService;
import com.viettelperu.qos.framework.exception.NotFoundException;
import com.viettelperu.qos.model.entity.FileUpload;

/**
 * Brings in the basic CRUD service ops from BaseService. Insert additional ops here.
 *
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
public interface FileUploadService extends BaseService<FileUpload, Long> {
	/**
     * Find by name
     *
     * @param filename
     * @return
     */
    public FileUpload findByFilename(String filename) throws NotFoundException;
    
    /**
     * Find category by name
     *
     * @param categoryName
     * @return
     */
    public FileUpload uploadFile(FileUpload file) throws NotFoundException;
    
    /**
     * Validates whether the given category already
     * exists in the system.
     *
     * @param categoryName
     *
     * @return
     */
    public boolean isCategoryPresent(String categoryName);

    /**
     * Validates whether the given category priority already
     * exists in the system.
     *
     * @param priorityId
     *
     * @return
     */
    public boolean isPriorityPresent(Integer priorityId);

    /**
     * Find category by name
     *
     * @param categoryName
     * @return
     */
    public FileUpload findByCategoryName(String categoryName) throws NotFoundException;

    /**
     * Find sub categories by parent category
     *
     * @param parentCategory
     * @return
     */
    public List<FileUpload> findSubCategories(FileUpload parentCategory) throws NotFoundException;
}
