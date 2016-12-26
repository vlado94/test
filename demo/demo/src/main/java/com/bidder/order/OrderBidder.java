package com.bidder.order;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.Data;

@Data
@Entity
public class OrderBidder {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ORDER_BIDDER_ID")
	private Long id;
	
	@ManyToMany
	@JoinTable(name = "ORDER_BIDDER_DRINKS", joinColumns = @JoinColumn(name = "ORDER_BIDDER_ID"), inverseJoinColumns = @JoinColumn(name = "DRINK_ORDER_ID"))
	private List<DrinkOrder> drinks;

	@ManyToMany
	@JoinTable(name = "ORDER_BIDDER_FOOD", joinColumns = @JoinColumn(name = "ORDER_BIDDER_ID"), inverseJoinColumns = @JoinColumn(name = "DISH_ORDER_ID"))
	private List<DishOrder> food;
	
}
