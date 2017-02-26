package app.bidder;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BidderRepository extends PagingAndSortingRepository<Bidder, Long>{


	@Query("select r from Bidder r where r.mail = ?1 and r.password = ?2")
	public Bidder findByMailAndPassword(String mail, String password);
	
	public Bidder findByMail(String mail);
}
