package app.manager.boss;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import app.common.User;
import lombok.Data;

//postoji samo jedan boss koji dodaje u sistem menadzere sistema
@Data
@Entity(name = "BOSS")
public class BossManager extends User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BOSS_ID")
	private Long id;
}