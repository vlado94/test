package app.restaurant;

import org.springframework.data.repository.PagingAndSortingRepository;
import java.lang.String;
import app.restaurant.Restaurant;
import java.util.List;

import app.guest.Guest;
import app.restaurant.Description;

public interface RestaurantRepository extends PagingAndSortingRepository<Restaurant, Long> {

	
	public List<Restaurant> findByDescriptionContaining(String description);
	
	public List<Restaurant> findByNameContaining(String regnum);
	
}
