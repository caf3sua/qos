package com.viettelperu.qos.model.repository;

import java.util.List;

import com.viettelperu.qos.framework.data.BaseJPARepository;
import com.viettelperu.qos.model.entity.FileUpload;

/**
 * CRUD operations come from Base Repo but additional operations can be defined here.
 *
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
public interface FileUploadRepository extends BaseJPARepository<FileUpload, Long> {
	/**
     * Finds a file with the given filename
     *
     * @param filename
     * @return
     */
    public FileUpload findByFilename(String filename);
    
    /**
     * Finds a category with the given categoryName
     *
     * @param categoryName
     * @return
     */
    public FileUpload findByCategoryName(String categoryName);

    /**
     * Finds a category with the given categoryPriority
     *
     * @param categoryPriority
     * @return
     */
    public FileUpload findByCategoryPriority(Integer categoryPriority);

    /**
     * Finds sub categories with the given parentCategory
     *
     * @param parentCategory
     * @return
     */
    public List<FileUpload> findSubCategories(FileUpload parentCategory);
}
