package com.viettelperu.qos.model.repository;

import java.util.List;

import com.viettelperu.qos.framework.data.BaseJPARepository;
import com.viettelperu.qos.model.entity.Category;

/**
 * CRUD operations come from Base Repo but additional operations can be defined here.
 *
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
public interface CategoryRepository extends BaseJPARepository<Category, Long> {
    /**
     * Finds a category with the given categoryName
     *
     * @param categoryName
     * @return
     */
    public Category findByCategoryName(String categoryName);

    /**
     * Finds a category with the given categoryPriority
     *
     * @param categoryPriority
     * @return
     */
    public Category findByCategoryPriority(Integer categoryPriority);

    /**
     * Finds sub categories with the given parentCategory
     *
     * @param parentCategory
     * @return
     */
    public List<Category> findSubCategories(Category parentCategory);
}
