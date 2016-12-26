package com.order;
                           
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.dish.Dish;
import com.drink.Drink;
import com.restaurant.Table;

import lombok.Data;

@Data
@Entity
public class Orderr {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ORDER_ID")
	private Long id;
	
	/*@Column
	private boolean status;*/
	
	@Enumerated(EnumType.STRING)
	@Column
	private DishStatus dishStatus;
	
	@Enumerated(EnumType.STRING)
	@Column
	private DrinkStatus drinkStatus;

	@ManyToMany
	@JoinTable(name = "ORDER_DRINKS", joinColumns = @JoinColumn(name = "ORDER_ID"), inverseJoinColumns = @JoinColumn(name = "DRINK_ID"))
	private List<Drink> drinks;

	@ManyToMany
	@JoinTable(name = "ORDER_FOOD", joinColumns = @JoinColumn(name = "ORDER_ID"), inverseJoinColumns = @JoinColumn(name = "DISH_ID"))
	private List<Dish> food;
	/*
	@OneToOne(cascade = CascadeType.MERGE)
	@JoinTable(name = "ORDER_RESTAURANT", joinColumns = @JoinColumn(name = "ORDER_ID"), inverseJoinColumns = @JoinColumn(name = "RESTAURANT_ID"))
	private Restaurant restaurant;
	*/
	@ManyToOne
	@JoinTable(name = "ORDER_TABLE", joinColumns = @JoinColumn(name = "ORDER_ID"), inverseJoinColumns = @JoinColumn(name = "TABLE_ID"))
	private Table table;
}
