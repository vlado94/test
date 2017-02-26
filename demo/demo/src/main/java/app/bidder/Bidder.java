package app.bidder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import app.common.User;
import lombok.Data;

@Data
@Entity
public class Bidder extends User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BIDDER_ID")
	private Long id;
}
