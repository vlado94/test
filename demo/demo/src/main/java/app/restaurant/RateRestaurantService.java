package app.restaurant;

import java.util.List;

public interface RateRestaurantService {
	List<RateRestaurant> findAll();

	RateRestaurant save(RateRestaurant rateRestaurant);

	RateRestaurant findOne(Long id);

	void delete(Long id);
}
