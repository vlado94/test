package app.dish;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
@Transactional
public class DishServiceImpl implements DishService {
	private final DishRepository repository;

	@Autowired
	public DishServiceImpl(final DishRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<Dish> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public Dish save(Dish dish) {
		return repository.save(dish);
	}

	@Override
	public Dish findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public void delete(Long id) {
		repository.delete(id);
	}
}