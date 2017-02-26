package app.restaurant;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;

import app.guest.Guest;
import lombok.Data;

@Data
@Entity
public class RateRestaurant {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "RATE_RESTAURANT_ID")
	private Long id;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinTable(name = "RATE_RESTAURANT_GUEST", joinColumns = @JoinColumn(name = "RATE_RESTAURANT_ID"), inverseJoinColumns = @JoinColumn(name = "GUEST_ID"))
	private Guest guest;
	
	@Column
	private int rate;
	
	

}
