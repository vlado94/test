package app.drink;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
@Transactional
public class DrinkServiceImpl implements DrinkService {
	private final DrinkRepository repository;

	@Autowired
	public DrinkServiceImpl(final DrinkRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<Drink> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public Drink save(Drink drink) {
		return repository.save(drink);
	}

	@Override
	public Drink findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public void delete(Long id) {
		repository.delete(id);
	}
}