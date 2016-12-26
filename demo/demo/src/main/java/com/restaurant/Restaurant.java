package com.restaurant;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.bidder.Bidder;
import com.dish.Dish;
import com.drink.Drink;
import com.employed.bartender.Bartender;
import com.employed.cook.Cook;
import com.employed.waiter.Waiter;
import com.manager.restaurant.RestaurantManager;
import com.order.Orderr;
import lombok.Data;

@Data
@Entity
public class Restaurant {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "RESTAURANT_ID")
	private Long id;

	@Column
	@NotBlank
	private String name;

	@NotNull
	@OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "RESTAURANT_MANAGER_ID")
	private RestaurantManager restaurantManager;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "RESTAURANT_DISH", joinColumns = @JoinColumn(name = "RESTAURANT_ID"), inverseJoinColumns = @JoinColumn(name = "DISH_ID"))
	private List<Dish> food;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "RESTAURANT_DRINK", joinColumns = @JoinColumn(name = "RESTAURANT_ID"), inverseJoinColumns = @JoinColumn(name = "DRINK_ID"))
	private List<Drink> drinks;

	@OneToMany(mappedBy = "restaurant")
	private List<Segment> segments;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "RESTAURANT_WAITERS", joinColumns = @JoinColumn(name = "RESTAURANT_ID"), inverseJoinColumns = @JoinColumn(name = "WAITER_ID"))
	private List<Waiter> waiters;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "RESTAURANT_COOKS", joinColumns = @JoinColumn(name = "RESTAURANT_ID"), inverseJoinColumns = @JoinColumn(name = "COOK_ID"))
	private List<Cook> cooks;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "RESTAURANT_BARTENDERS", joinColumns = @JoinColumn(name = "RESTAURANT_ID"), inverseJoinColumns = @JoinColumn(name = "BARTENDER_ID"))
	private List<Bartender> bartenders;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "RESTAURANT_BIDDERS", joinColumns = @JoinColumn(name = "RESTAURANT_ID"), inverseJoinColumns = @JoinColumn(name = "BIDDER_ID"))
	private List<Bidder> bidders;

	@Column
	private Integer summRate;

	@Column
	private Integer numRate;

	@OneToMany
	@JoinTable(name = "RESTAURANT_ORDER", joinColumns = @JoinColumn(name = "RESTAURANT_ID"), inverseJoinColumns = @JoinColumn(name = "ORDER_ID"))
	private List<Orderr> order;
}