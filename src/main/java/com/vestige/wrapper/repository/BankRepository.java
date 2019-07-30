package com.vestige.wrapper.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vestige.core.domain.Bank;

/**
 * 
 * @author ashutosh.sharma
 *
 */
@Repository
public interface BankRepository extends MongoRepository<Bank, String>{

	Bank findByBankId(Integer bankId);

}
