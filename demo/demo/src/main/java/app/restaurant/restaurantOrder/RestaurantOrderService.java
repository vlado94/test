package app.restaurant.restaurantOrder;

import java.util.List;

public interface RestaurantOrderService {
	List<RestaurantOrderr> findAll();

	RestaurantOrderr save(RestaurantOrderr restaurantOrderr);

	RestaurantOrderr findOne(Long id);
}
