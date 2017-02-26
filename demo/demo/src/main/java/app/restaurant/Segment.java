package app.restaurant;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import lombok.Data;

@Data
@Entity
public class Segment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "SEGMENT_ID")
	private Long id;

	@Column
	private String name;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "SEGMENT_TABLE", joinColumns = @JoinColumn(name = "SEGMENT_ID"), inverseJoinColumns = @JoinColumn(name = "TABLE_ID"))
	private List<Table> tables;
	
	public Segment(String name){
		this.name = name;
	}
	
	public Segment(){
	}
}