package com.drink;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
public class Drink {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "DRINK_ID")
	private Long id;

	@NotNull
	@Column
	private String name;

	@Column
	private String text;

	@NotNull
	@Column
	private Integer price;

	@NotNull
	@Column
	private Integer count;

	@Column
	private Integer summRate;

	@Column
	private Integer numRate;
}