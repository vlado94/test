package com.guest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.common.User;

import lombok.Data;

@Data
@Entity
public class Guest extends User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "GUEST_ID")
	private Long id;

	// zakomentarisano zbog bug-a kod metode getLoggedUser u commonController
/*
	//lista restorana gde je sve bio 
	@ManyToMany
	@JoinTable(name = "GUEST_RESTAURANT", joinColumns = @JoinColumn(name = "GUEST_ID"), inverseJoinColumns = @JoinColumn(name = "RESTAURANT_ID"))
	private List<Restaurant> visitedRestaurants;
*/

}