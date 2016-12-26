package com.restaurant;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

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
}