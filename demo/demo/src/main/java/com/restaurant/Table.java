package com.restaurant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.order.Orderr;

import lombok.Data;

@Data
@Entity
public class Table {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "TABLE_ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "SEGMENT_ID")
	private Segment segment;
	
	@ManyToOne
	@JoinColumn(name = "ORDER_ID")
	private Orderr order;
	
}
