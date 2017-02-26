package app.offer;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import app.bidder.Bidder;
import lombok.Data;

@Data
@Entity
public class Offer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "OFFER_ID")
	private Long id;
	
	@NotNull
	@Column
	@Min(0)
	private Long price;

	@NotNull
	@Column
	@Min(0)
	private Integer garanty;

	@NotNull
	@Column
	private Date posibleDelivery;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Bidder bidder;
	
	//0 -active 1--acctepted 2--rejected
	//@NotBlank
	@Column
	private String accepted;
}
