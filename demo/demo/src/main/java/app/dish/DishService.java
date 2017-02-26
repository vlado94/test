package app.dish;

import java.util.List;

public interface DishService {
	List<Dish> findAll();

	Dish save(Dish dish);

	Dish findOne(Long id);

	void delete(Long id);
}
