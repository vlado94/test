package app.drink;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface DrinkRepository extends PagingAndSortingRepository<Drink, Long> {

}
