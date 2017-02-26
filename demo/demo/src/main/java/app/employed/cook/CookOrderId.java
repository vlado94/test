package app.employed.cook;

import java.io.Serializable;

import lombok.Data;

@Data
public class CookOrderId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long orderId;
	
	private Long cookId;
	
}
