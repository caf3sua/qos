package com.viettelperu.qos.service;

import java.util.List;

import com.viettelperu.qos.framework.data.BaseService;
import com.viettelperu.qos.model.entity.Server;

/**
 * Brings in the basic CRUD service ops from BaseService. Insert additional ops here.
 *
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
public interface ServerService extends BaseService<Server, Long> {
	/**
     * Validates whether the given category already
     * exists in the system.
     *
     * @param categoryName
     *
     * @return
     */
    public List<Server> findAll();
}
