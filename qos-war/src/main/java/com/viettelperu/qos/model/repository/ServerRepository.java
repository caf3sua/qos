package com.viettelperu.qos.model.repository;

import java.util.List;

import com.viettelperu.qos.framework.data.BaseJPARepository;
import com.viettelperu.qos.model.entity.Server;

/**
 * CRUD operations come from Base Repo but additional operations can be defined here.
 *
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
public interface ServerRepository extends BaseJPARepository<Server, Long> {
    public List<Server> findAll();
}
