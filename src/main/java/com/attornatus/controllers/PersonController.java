package com.attornatus.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.attornatus.models.Person;
import com.attornatus.services.PersonService;

@RestController
@RequestMapping("/pessoas")
public class PersonController {
	
	@Autowired
	private PersonService personService;
	
	/**
	 * Salvar o endreço da pessoa
	 * 
	 * @param person Recebe a pessoa a ser cadastrada.
	 * @return Retorna a pessoa cadastrada com seus dados.
	 */
	@PostMapping
	public ResponseEntity<Person> save(@RequestBody Person person){
		
		Person savedPerson = personService.save(person);
		
		savedPerson.add(linkTo(methodOn(PersonController.class)
									.getAll())
									.withRel("Return all People"));
		return new ResponseEntity<Person>(savedPerson, HttpStatus.CREATED);
	}
	
	/**
	 * Edita os dados de uma pessoa. Na variável de parâmetro é informado o id
	 * da pessoa a ser editada. E é recebido pelo Body os dados a serem atualizados
	 * da pessoa.
	 * 
	 * @param id			Código da pessoa a atualizar, do tipo Long
	 * @param personUpdated Dados da pessoa a atualizar
	 * @return				Retorna a pessoa atualizada.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<Person> update(@PathVariable(value="id") Long id,
									@RequestBody @Validated Person personUpdated) {
		Optional<Person> personOld = personService.findPerson(id);
		if(!personOld.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			personUpdated.setId(personOld.get().getId());
			personService.save(personUpdated);
			personUpdated.add(linkTo(methodOn(PersonController.class)
										.getAll())
										.withRel("Return all People"));
			return new ResponseEntity<Person>(personUpdated, HttpStatus.OK);
		}
	}
	
	/**
	 * Retorna a pessoa solicitada. É recebido por parâmetro o código da pessoa a 
	 * ser retornada.
	 * 
	 * @param id 		Código da pessoa a ser localizada no sistema para retornar
	 * @return Person	Pessoa localizada no sistema.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Person> getOnePerson(@PathVariable(value="id") Long id){
		Optional<Person> personFound = personService.findPerson(id);
		if (!personFound.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			personFound.get().add(linkTo(methodOn(PersonController.class)
											.getAll())
											.withRel("Return all People"));
			return new ResponseEntity<Person>(personFound.get(), HttpStatus.OK);
		}
	}
	
	/**
	 * Retorna uma lista das pessoas.
	 * 
	 * @return	List<Person> 	Retorna uma lista com as pessoas.
	 */
	@GetMapping
	public ResponseEntity<List<Person>> getAll() {
		List<Person> personList = personService.getAll();
		if (personList.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			for (Person person : personList) {
				Long id = person.getId();
				person.add(linkTo(methodOn(PersonController.class)
						.getOnePerson(id))
						.withRel("Details about this person"));
			}
			
			return new ResponseEntity<List<Person>>(personList, HttpStatus.OK);
		}
	}

}
