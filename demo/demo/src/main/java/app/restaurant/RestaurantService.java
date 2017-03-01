package app.restaurant;

import java.util.List;

public interface RestaurantService {
	List<Restaurant> findAll();

	Restaurant save(Restaurant restaurant);

	Restaurant findOne(Long id);

	void delete(Long id);
	
	List<Restaurant> findByNameAndType(String inputStr);
}
