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
import javax.persistence.OneToMany;

import app.reservation.Reservation;
import lombok.Data;

@Data
@Entity
public class Table {
	
	public static final String NOT_EXISTS = "Not Exists";
	public static final String EXISTS = "Exists";
	public static final String RESERVED = "Reserved";
	

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
	
	@OneToMany(cascade = CascadeType.ALL)
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
	}
	
	
}
