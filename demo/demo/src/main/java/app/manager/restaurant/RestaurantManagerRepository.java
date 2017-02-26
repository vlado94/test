package app.manager.restaurant;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RestaurantManagerRepository extends PagingAndSortingRepository<RestaurantManager, Long> {

	@Query("select r from RestaurantManager r where r.mail = ?1 and r.password = ?2")
	public RestaurantManager findByMailAndPassword(String mail, String password);

	public RestaurantManager findByMail(String mail);
}