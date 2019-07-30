package com.vestige.wrapper.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vestige.core.domain.Logs;

/**
 * 
 * @author ashutosh.sharma
 *
 */
@Repository
public interface LogsRepository extends MongoRepository<Logs, String>{
	
}
