package app.restaurant;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.NotBlank;

import app.bidder.Bidder;
import app.dish.Dish;
import app.drink.Drink;
import app.employed.bartender.Bartender;
import app.employed.cook.Cook;
import app.employed.waiter.Waiter;
import app.manager.changedShiftBartender.ChangedShiftBartender;
import app.manager.changedShiftCook.ChangedShiftCook;
import app.manager.changedShiftWaiter.ChangedShiftWaiter;
import app.manager.restaurant.RestaurantManager;
import app.order.Orderr;
import app.restaurant.restaurantOrder.RestaurantOrderr;
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

	@OneToMany
	@JoinTable(name = "RESTAURANT_MANAGERS_OF_RESTAURANT", joinColumns = @JoinColumn(name = "RESTAURANT_ID"), inverseJoinColumns = @JoinColumn(name = "RESTAURANT_MANAGER_ID"))
	private List<RestaurantManager> restaurantManagers;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "RESTAURANT_DISH", joinColumns = @JoinColumn(name = "RESTAURANT_ID"), inverseJoinColumns = @JoinColumn(name = "DISH_ID"))
	private List<Dish> food;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "RESTAURANT_DRINK", joinColumns = @JoinColumn(name = "RESTAURANT_ID"), inverseJoinColumns = @JoinColumn(name = "DRINK_ID"))
	private List<Drink> drinks;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "RESTAURANT_SEGMENT", joinColumns = @JoinColumn(name = "RESTAURANT_ID"), inverseJoinColumns = @JoinColumn(name = "SEGMENT_ID"))
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

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "RESTAURANT_BIDDERS", joinColumns = @JoinColumn(name = "RESTAURANT_ID"), inverseJoinColumns = @JoinColumn(name = "BIDDER_ID"))
	private List<Bidder> bidders;

	@Column
	private Double summRate;


	@OneToMany
	@JoinTable(name = "RESTAURANT_RATES", joinColumns = @JoinColumn(name = "RESTAURANT_ID"), inverseJoinColumns = @JoinColumn(name = "RATE_RESTAURANT_ID"))
	private List<RateRestaurant> rateRestaurant;

	@OneToMany
	@JoinTable(name = "RESTAURANT_ORDER", joinColumns = @JoinColumn(name = "RESTAURANT_ID"), inverseJoinColumns = @JoinColumn(name = "ORDER_ID"))
	private List<Orderr> order;
	
	
	//lista porudzbina restorana za ponudjace
	@OneToMany
	//@OneToMany
	@JoinTable(name = "RESTAURANT_RESTAURANT_ORDER", joinColumns = @JoinColumn(name = "RESTAURANT_ID"), inverseJoinColumns = @JoinColumn(name = "RESTAURANT_ORDER_ID"))
	private List<RestaurantOrderr> restaurantOrders;
	
	@Column
	@NotBlank
	private String country;

	
	@Column
	@NotBlank
	private String description;

	@Column
	@NotBlank
	private String city;
	
	@Column
	@NotBlank
	private String street;
	
	@Column
	@NotBlank
	private String number;
	
	@OneToMany
	@JoinTable(name = "RESTAURANT_CHANGED_SHIFTS_COOKS", joinColumns = @JoinColumn(name = "RESTAURANT_ID"), inverseJoinColumns = @JoinColumn(name = "SHIFT_ID"))
	private List<ChangedShiftCook> changedShiftsForCooks;
	
	
	@OneToMany
	@JoinTable(name = "RESTAURANT_CHANGED_SHIFTS_BARTENDERS", joinColumns = @JoinColumn(name = "RESTAURANT_ID"), inverseJoinColumns = @JoinColumn(name = "SHIFT_ID"))
	private List<ChangedShiftBartender> changedShiftsForBartenders;
	
	
	@OneToMany
	@JoinTable(name = "RESTAURANT_CHANGED_SHIFTS_WAITERS", joinColumns = @JoinColumn(name = "RESTAURANT_ID"), inverseJoinColumns = @JoinColumn(name = "SHIFT_ID"))
	private List<ChangedShiftWaiter> changedShiftsForWaiters;
	
	public double getSummRate(){
		double sum = 0;
		double average = 0;
		for(int i = 0 ; i < this.rateRestaurant.size(); i++){
			sum += rateRestaurant.get(i).getRate();
			
		}
		average = sum/this.rateRestaurant.size();
		
		return average;
		
	}
	
}