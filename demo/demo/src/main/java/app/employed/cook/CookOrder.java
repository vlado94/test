package app.employed.cook;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import app.order.Orderr;
import lombok.Data;

@Data
@Entity
@IdClass(CookOrderId.class)
public class CookOrder {
	
	@Id
	@Column(name = "ORDER_ID")
	private Long orderId;
	
	@Id
	@Column(name = "COOK_ID")
	private Long cookId;
	
	
	//@Id
	@ManyToOne
	@JoinColumn(name="COOK_ID", insertable=false, updatable=false)
	@JsonIgnore
	private Cook cook;
	
	//@Id
	@ManyToOne
	@JoinColumn(name="ORDER_ID", insertable=false, updatable=false)
	@JsonIgnore
	private Orderr order;
	
	@Enumerated(EnumType.STRING)
	@Column
	private DishStatus dishStatus;

}
