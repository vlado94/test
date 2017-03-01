package app.reservation;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import app.guest.Guest;
import app.order.Orderr;
import app.restaurant.Restaurant;
import lombok.Data;

@Data
@Entity
public class Reservation {
	
	public Reservation(){
		this.guests = new ArrayList<Guest>();
		this.invitedGuests = new ArrayList<Long>();
		this.orders = new ArrayList<Orderr>();
		this.tables = new ArrayList<Long>();
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "RESERVATION_ID")
	private Long id;
	
	@Column
	private Date date;
	
	@Column
	private int hours;
	
	@Column
	private int minutes;
	
	@Column
	private int duration;
	
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "RESERVATION_GUEST", joinColumns = @JoinColumn(name = "RESERVATION_ID"), inverseJoinColumns = @JoinColumn(name = "GUEST_ID"))
	private List<Guest> guests;
	
	@ElementCollection
	private List<Long> invitedGuests;
	
	@ElementCollection
	private List<Long> tables;
	
	@JsonIgnore
	@OneToMany
	@JoinTable(name = "RESERVATION_ORDER", joinColumns = @JoinColumn(name = "RESERVATION_ID"), inverseJoinColumns = @JoinColumn(name = "ORDER_ID"))
	private List<Orderr> orders;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	private Restaurant restaurant;
	
	

}
