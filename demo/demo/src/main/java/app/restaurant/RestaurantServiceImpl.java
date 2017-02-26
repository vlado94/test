package app.restaurant;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import app.guest.Guest;

@Service
@Transactional
public class RestaurantServiceImpl implements RestaurantService {
	private final RestaurantRepository repository;

	@Autowired
	public RestaurantServiceImpl(final RestaurantRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<Restaurant> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public Restaurant save(Restaurant restaurant) {
		return repository.save(restaurant);
	}

	@Override
	public Restaurant findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public void delete(Long id) {
		repository.delete(id);
	}

	@Override
	public List<Restaurant> findByNameAndType(String inputStr) {
		List<Restaurant> list1 = new ArrayList<Restaurant>();
		list1.addAll(repository.findByNameContaining(inputStr));
		list1.addAll(repository.findByDescriptionContaining(inputStr));
		return list1;
	}
}