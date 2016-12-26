package com.restaurant;

import java.util.List;

public interface RestaurantService {
	List<Restaurant> findAll();

	Restaurant save(Restaurant restaurant);

	Restaurant findOne(Long id);

	void delete(Long id);
}
