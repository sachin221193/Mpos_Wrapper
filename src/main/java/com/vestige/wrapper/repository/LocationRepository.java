package com.vestige.wrapper.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vestige.core.domain.Location;

/**
 * 
 * @author ashutosh.sharma
 *
 */
@Repository
public interface LocationRepository extends MongoRepository<Location, String>{

	Location findByLocationId(Integer locationId);

}
