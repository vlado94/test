package app.restaurant;

import java.util.ArrayList;
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
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

import app.reservation.Reservation;
import lombok.Data;

@Data
@Entity
public class Table {
	
	public static final String NOT_EXISTS = "Not Exists";
	public static final String EXISTS = "Exists";
	public static final String RESERVED = "Reserved";
	
	@Version
	@Column
	private Integer version;
	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "TABLE_ID")
	private Long id;
	
	@Column
	private String name;
	
	/*@ManyToOne
	@JoinColumn(name = "ORDER_ID")
	private Orderr order;*/
	
	@Column
	private int xPos;
	
	@Column 
	private int yPos;
	
	@Column
	private String status;
	
	@Column
	private String segmentName;
	
	
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "TABLE_RESERVATION", joinColumns = @JoinColumn(name = "TABLE_ID"), inverseJoinColumns = @JoinColumn(name = "RESERVATION_ID"))
	private List<Reservation> reservations;
	
	public Table(String name, int xPos, int yPos, String status){
		super();
		this.name = name;
		this.xPos = xPos;
		this.yPos = yPos;
		this.status = status;
	}
	
	public Table() {
		// TODO Auto-generated constructor stub
		this.reservations = new ArrayList<Reservation>();
	}
	
	
}
