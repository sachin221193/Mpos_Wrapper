package com.vestige.wrapper.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vestige.core.domain.Area;

/**
 * 
 * @author ashutosh.sharma
 *
 */

@Repository
public interface AreaRepository extends MongoRepository<Area, String>{

	Area findByAreaId(Integer areaId);

}
