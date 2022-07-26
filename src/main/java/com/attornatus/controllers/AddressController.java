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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.attornatus.models.Address;
import com.attornatus.services.AddressService;

@RestController
@RequestMapping("/enderecos")
public class AddressController {
	
	@Autowired
	private AddressService addressService;
	
	/**
	 * Salvar o endereço da pessoa
	 * 
	 * @param address Objeto do tipo Address(contendo dados do endereço e o código 
	 * 		  da pessoa que o endereço pertence)
	 * @return Retorna um Objeto do tipo Address, contendo o endereço que foi salvo
	 */
	@PostMapping
	public ResponseEntity<Address> save(@RequestBody Address address) {
		
		Address savedAddress = addressService.save(address);
		
		if (savedAddress == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			if (savedAddress.getPrincipal()) {
				addressService.changePrincipal(savedAddress.getId());
			}
			return new ResponseEntity<Address>(savedAddress, HttpStatus.CREATED);
		}
	}
	
	/**
	 * Retorna a lista de ENDEREÇOS DA PESSOA informada como parâmetro pelo {id} dela
	 * É feita uma busca que retorna um LIST
	 * 
	 * @param id É o código da pessoa que esse método precisa retornar os endereços
	 * @return List<Address> é retornado uma lista contendo todos os endereços da pessoa
	 * 		   informada através da variável de parâmetro {id}
	 */
	@GetMapping("/pessoa/{id}")
	public ResponseEntity<List<Address>> getOneAddress(@PathVariable(value="id") Long id){
		List<Address> addressList = addressService.findPersonAdresses(id);
		if (addressList.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<List<Address>>(addressList, HttpStatus.OK);
		}
	}
	
	/**
	 * Torna um endereço principal, ou seja, atribui valor true para o endreço passado como
	 * parâmetro. É nessário informar apenas o id desse objeto, os demais atributos desse 
	 * objeto não precisam ser informados.
	 * Ex: do Json a enviar 
	 * {
	 * 	"id": 3
	 * }
	 * 
	 * @param address Objeto do tipo "Address" contendo o código do endereço a se tornar
	 * 		  PRINCIPAL
	 * @return é retornado o endereço atualizado com status PRINCIPAL=true
	 */
	@PatchMapping("/tornarprincipal")
	public ResponseEntity<Address> updatePrincipal(@RequestBody @Validated Address address){
		Optional<Address> principalAddress = addressService.changePrincipal(address.getId());
		if(!principalAddress.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			principalAddress.get().add(linkTo(methodOn(PersonController.class)
												.getOnePerson(principalAddress.get()
																			.getPerson().getId()))
												.withRel("Return details about owner of this address"));
			return new ResponseEntity<Address>(principalAddress.get(), HttpStatus.OK);
		}
	}

}
