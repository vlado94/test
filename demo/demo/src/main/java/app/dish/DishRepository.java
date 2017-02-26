package app.dish;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface DishRepository extends PagingAndSortingRepository<Dish, Long> {

}
