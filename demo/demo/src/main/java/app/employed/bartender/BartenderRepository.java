package app.employed.bartender;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BartenderRepository extends PagingAndSortingRepository<Bartender, Long>{
	/*@Query("select r from Bartender r where r.mail = ?1 and r.password = ?2")
	public Bartender findOneMailAndPass(String mail, String password);
	
	public Bartender findOneWithMail(String mail);*/
}
