package app.manager.changedShiftCook;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import app.employed.cook.Cook;
import lombok.Data;

@Data
@Entity
public class ChangedShiftCook {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "SHIFT_ID")
	private Long id;

	// za koji dan
	@Column
	@NotNull
	Date date;
	
	// kome menjas
	@OneToOne
	@NotNull
	Cook cook1;
	
	// sa kim smenu menjas
	@OneToOne
	@NotNull
	Cook cook2;
}
