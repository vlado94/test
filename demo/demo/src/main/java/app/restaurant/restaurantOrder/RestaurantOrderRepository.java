package app.restaurant.restaurantOrder;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface RestaurantOrderRepository extends PagingAndSortingRepository<RestaurantOrderr, Long> {	
}