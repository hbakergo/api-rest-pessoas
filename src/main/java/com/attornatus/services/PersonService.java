package com.attornatus.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.attornatus.models.Person;
import com.attornatus.repositories.PersonRepository;

@Service
public class PersonService {

	@Autowired
	private PersonRepository personRepository;
	
	/**
	 * Salvar uma pessoa.
	 * 
	 * @param person Objeto do tipo Pessoa
	 * @return Person, retorna a pessoa cadastrada em casso de sucesso
	 * 		   Null, retorna null caso tenha falha ao salvar 
	 */
	public Person save(Person person) {
		try {
			return personRepository.save(person);
		} catch(Exception e) {
			return null;
		}
		
	}
	
	/**
	 * Editar dados de uma pessoa. É enviado o id da pessoa a ser 
	 * editado, e um objeto com os dados da pessoa a serem
	 * atualizados.
	 * 
	 * @param id 		Código da pessoa a ser editada do tipo Long
	 * @param personNew Objeto do tipo Person contendo os dados
	 * 					a serem atualizados no cadastro dessa pessoa
	 * @return			True se a atualiação ocorreu corretamente
	 */
	public boolean update(Long id, Person personNew) {
		Optional<Person> personOld = personRepository.findById(id);
		if (!personOld.isPresent()) {
			return false;
		} else {
			personNew.setId(personOld.get().getId());
			personRepository.save(personNew);
			return true;
		}
	}
	
	/**
	 * Retornar uma lista com as pessoas cadastradas no sistema.
	 * 
	 * @return List<Person> Retorna uma lista de pessoas.
	 */
	public List<Person> getAll() {
		return personRepository.findAll();
	}
	
	/**
	 * Encontrar pessoa no sistema.
	 * 
	 * @param id	Código de pessoa a ser localizada no sistema
	 * @return 		Retorna null se a pessoa
	 */
	public Optional<Person> findPerson(Long id) {
		return personRepository.findById(id); 
	}
	
	
}
