package app.friends;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import app.guest.Guest;
import lombok.Data;

//klasa koja ima parove prijatelja
@Entity
@Data
public class Friends {
	
	public static final String PENDING = "Pending";
	public static final String ACCEPTED = "Friends";
	public static final String REJECTED = "Rejected";
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "FRIEND_ID")
	private Long id;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "FRIEND_SEND_REQUEST")
	private Guest friendSendRequest;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "FRIEND_RECIVE_REQUEST")
	private Guest friendReciveRequest;

	@Column
	private String status;	//pending, accepted, rejected

	public Friends(Guest friendSendRequest, Guest friendReciveRequest, String status) {
		super();
		this.friendSendRequest = friendSendRequest;
		this.friendReciveRequest = friendReciveRequest;
		this.status = status;;
	}

	public Friends() {
	}

}