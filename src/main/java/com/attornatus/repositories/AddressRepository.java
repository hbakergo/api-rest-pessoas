package com.attornatus.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.attornatus.models.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long>{

	@Query("select a from Address a where person.id = ?1")
	List<Address> findByPersonId(Long id);
	
	@Modifying
	@Transactional
	@Query("update Address a set a.principal= "
			+ " CASE WHEN (a.id = ?1) THEN true "
			+ "WHEN (a.id != ?1) THEN false END")
	int changePrincipal(Long id);

}
