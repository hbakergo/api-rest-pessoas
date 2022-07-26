package com.attornatus.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.attornatus.models.Address;
import com.attornatus.repositories.AddressRepository;

@Service
public class AddressService {

	@Autowired
	private AddressRepository addressRepository;
	
	/**
	 * Salvar um endereço.
	 * 
	 * @param address	Endereço a ser salvo
	 * @return			Retorna null caso tenha falha, senão retorna
	 * 					o endereço salvo.
	 */
	public Address save(Address address) {
		try {
			return addressRepository.save(address);
		} catch(Exception e) {
			return null;
		}
		
	}
	
	/**
	 * Retorna a lista de endereços de uma pessoa consultando na tabela
	 * "adresses" pelo {id} informado no parâmetro. Esse "id" refere-se 
	 * ao código da pessoa ao qual esse endereço pertence.
	 * 
	 * @param id	É o id da pessoa que esse método irá retornar os
	 * 		        endereços
	 * @return		List<Address> contendo os endereços da pessoa
	 */
	public List<Address> findPersonAdresses(Long id){
		return addressRepository.findByPersonId(id);
	}
	
	// Tornar endereço como principal da pessoa informada
	
	/**
	 * Tornar endereço informado no parâmetro como principal. Esse método 
	 * invoca o método do repositório Address criado para fazer update
	 * onde torna os demais endereços com status de principal "false" e 
	 * o endereço informado no parâmetro como status de principal "true".
	 * 
	 * @param id É o código do endereço que deve se tornar principal.
	 * @return	 Retorna o endereço atualizado com status principal.
	 */
	public Optional<Address> changePrincipal(Long id) {
		Optional<Address> addressToChange = addressRepository.findById(id);
		if (addressToChange.isPresent()) {
			addressRepository.changePrincipal(id);
		}
		return addressRepository.findById(id);
	}
}
