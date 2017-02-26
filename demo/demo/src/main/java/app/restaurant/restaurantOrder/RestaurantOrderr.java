package app.restaurant.restaurantOrder;

import java.sql.Date;
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

import app.dish.Dish;
import app.drink.Drink;
import app.offer.Offer;
import lombok.Data;

@Data
@Entity
public class RestaurantOrderr {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "RESTAURANT_ORDER_ID")
	private Long id;

	@Column
	private String orderActive;
	
	@Column
	private Date startDate;

	@NotNull
	@Column
	private Date endDate;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Drink drink;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Dish dish;
	
	@NotNull
	@Column
	private Integer count;
	
	@OneToMany
	@JoinTable(name = "RESTAURANT_OFFER", joinColumns = @JoinColumn(name = "RESTAURANT_ORDER_ID"), inverseJoinColumns = @JoinColumn(name = "OFFER_ID"))
	private List<Offer> offers;
	
	@Column
	private Long idFromChoosenBidder;
}
