package app.employed.cook;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import app.employed.Employed;
import app.restaurant.Restaurant;
import lombok.Data;

@Data
@Entity
public class Cook extends Employed {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "COOK_ID")
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	@Column
	private TypeOfCooker typeOfCooker;

	@OneToMany(cascade={CascadeType.ALL}, mappedBy = "cook")
	//@JoinColumn(name="COOK_COOK_ID", referencedColumnName="COOK_ID")
	private List<CookOrder> orders;

	@JsonIgnore
	@ManyToOne
	private Restaurant restaurant;

}
