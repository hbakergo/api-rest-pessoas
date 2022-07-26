package com.attornatus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.attornatus.models.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{

}
