package app.drink;

import java.util.List;

public interface DrinkService {
	List<Drink> findAll();

	Drink save(Drink drink);

	Drink findOne(Long id);

	void delete(Long id);
}
