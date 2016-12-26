package com.common;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Data
@MappedSuperclass
public class User {

	@Email
	@NotBlank
	@Column(unique = true)
	private String mail;

	@NotBlank
	@Column
	private String password;

	@NotBlank
	@Column
	private String firstname;

	@NotBlank
	@Column
	private String lastname;

	// flag koji je inicijalno false, i kad korisnik klikne na link pri
	// registraciji postaje true
	@Column
	private String registrated;
}