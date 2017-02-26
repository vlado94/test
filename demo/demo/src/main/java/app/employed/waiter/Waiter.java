package app.employed.waiter;

import java.util.List;

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

import app.bill.Bill;
import app.employed.Employed;
import app.order.Orderr;
import app.restaurant.Restaurant;
import app.restaurant.Table;
import lombok.Data;

@Data
@Entity
public class Waiter extends Employed {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "WAITER_ID")
	private Long id;

	@ManyToMany
	@JoinTable(name = "WAITER_ORDERS", joinColumns = @JoinColumn(name = "WAITER_ID"), inverseJoinColumns = @JoinColumn(name = "ORDER_ID"))
	private List<Orderr> orders;
	
	@ManyToMany
	@JoinTable(name = "WAITER_TABLES", joinColumns = @JoinColumn(name = "WAITER_ID"), inverseJoinColumns = @JoinColumn(name = "TABLE_ID"))
	private List<Table> tablesForHandling;
	
	@OneToMany
	@JoinTable(name = "WAITER_BILLS", joinColumns = @JoinColumn(name = "WAITER_ID"), inverseJoinColumns = @JoinColumn(name = "BILL_ID"))
	private List<Bill> bills;
	
	@ManyToOne
	@JsonIgnore
	private Restaurant restaurant;
	
	@Column
	private double rate;
	
	@ElementCollection
	private List<Integer> numRate;
	
	public double getRate(){
		double sum = 0;
		double average = 0;
		
		if(this.numRate.size() > 0){
			for(int i = 0 ; i < this.numRate.size(); i++){
				sum += numRate.get(i);
			}
			average = sum/this.numRate.size();
		}
		return average;
	}	
}