package com.attornatus.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "adresses")
public class Address extends RepresentationModel<Address> implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 70, nullable=false)
	private String address;
	
	@Column(length = 9)
	private String cep;
	
	@Column(length = 20)
	private String number;
	
	@Column(length = 70)
	private String city;
	
	private Boolean principal = false;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_person", nullable = false)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Person person;

}
