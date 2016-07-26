package com.viettelperu.qos.model.repository.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.viettelperu.qos.framework.data.BaseHibernateJPARepository;
import com.viettelperu.qos.model.entity.FileUpload;
import com.viettelperu.qos.model.repository.FileUploadRepository;

/**
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
@Repository
public class FileUploadRepositoryImpl extends BaseHibernateJPARepository<FileUpload, Long> implements FileUploadRepository {
    private static Logger LOG = LoggerFactory.getLogger(FileUploadRepositoryImpl.class);

    @PostConstruct
    public void setUp() {
        LOG.info("fileUploadRepository created..!");
    }

    @Override
    public FileUpload findByCategoryName(String categoryName) {
        return (FileUpload) sessionFactory.getCurrentSession().createQuery("from FileUpload c where c.name = :categoryName")
                .setParameter("categoryName", categoryName).uniqueResult();
    }

    @Override
    public FileUpload findByCategoryPriority(Integer categoryPriority) {
        return (FileUpload) sessionFactory.getCurrentSession().createQuery("from FileUpload c where c.priority = :categoryPriority")
                .setParameter("categoryPriority", categoryPriority).uniqueResult();
    }

    @Override
    public List<FileUpload> findSubCategories(FileUpload parentCategory) {
        return (List<FileUpload>) sessionFactory.getCurrentSession().createQuery("from FileUpload c where c.parentCategory = :parentCategory")
                .setParameter("parentCategory", parentCategory).list();
    }

	@Override
	public FileUpload findByFilename(String filename) {
		return (FileUpload) sessionFactory.getCurrentSession().createQuery("from FileUpload c where c.filename = :filename")
                .setParameter("filename", filename).uniqueResult();
	}
}
