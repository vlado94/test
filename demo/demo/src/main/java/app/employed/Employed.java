package app.employed;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import app.common.User;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class Employed extends User {

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column
	private ClothesSize clothesSize;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column
	private ShoesSize shoesSize;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	@Column
	private DefaultShift defaultShift;
}
