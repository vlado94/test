package com.employed.cook;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.employed.Employed;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.order.Orderr;
import com.restaurant.Restaurant;

import lombok.Data;

@Data
@Entity
public class Cook extends Employed {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "COOK_ID")
	private Long id;

	@OneToMany
	@JoinTable(name = "COOK_ORDERS", joinColumns = @JoinColumn(name = "COOK_ID"), inverseJoinColumns = @JoinColumn(name = "ORDER_ID"))
	private List<Orderr> orders;

	@JsonIgnore
	@ManyToOne
	private Restaurant restaurant;

}
