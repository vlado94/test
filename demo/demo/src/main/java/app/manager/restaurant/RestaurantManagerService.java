package app.manager.restaurant;

import java.util.List;

public interface RestaurantManagerService {

	List<RestaurantManager> findAll();

	RestaurantManager save(RestaurantManager restaurantManager);

	RestaurantManager findOne(Long id);

	RestaurantManager findByMailAndPassword(String mail, String password);

	RestaurantManager findByMail(String mail);
}