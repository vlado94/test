package app.manager.changedShiftBartender;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import app.employed.bartender.Bartender;
import lombok.Data;

@Data
@Entity
public class ChangedShiftBartender {
	
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
	Bartender bartender1;
	
	// sa kim smenu menjas
	@OneToOne
	@NotNull
	Bartender bartender2;
}
