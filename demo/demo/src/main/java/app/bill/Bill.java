package app.bill;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;

import app.reservation.Reservation;
import lombok.Data;

@Data
@Entity
public class Bill {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BILL_ID")
	private Long id;
	
	@Column
	private Date date;
	
	@Column
	private int total;
	
	@ManyToOne
	@JoinTable(name = "BILL_RESERVATION", joinColumns = @JoinColumn(name = "BILL_ID"), inverseJoinColumns = @JoinColumn(name = "RESERVATION_ID"))
	private Reservation reservation;
	
	
	
}
