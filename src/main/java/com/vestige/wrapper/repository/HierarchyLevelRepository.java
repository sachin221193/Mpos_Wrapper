package com.vestige.wrapper.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vestige.core.domain.HierarchyLevel;

/**
 * 
 * @author ashutosh.sharma
 *
 */
@Repository
public interface HierarchyLevelRepository extends MongoRepository<HierarchyLevel, String>{

	HierarchyLevel findByHierarchyLevelId(Integer hierarchyLevelId);

	
}
