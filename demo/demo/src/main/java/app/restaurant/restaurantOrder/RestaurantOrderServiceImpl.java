package app.restaurant.restaurantOrder;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
@Transactional
public class RestaurantOrderServiceImpl implements RestaurantOrderService {
	private final RestaurantOrderRepository repository;

	@Autowired
	public RestaurantOrderServiceImpl(final RestaurantOrderRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<RestaurantOrderr> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public RestaurantOrderr save(RestaurantOrderr restaurantOrderr) {
		return repository.save(restaurantOrderr);
	}

	@Override
	public RestaurantOrderr findOne(Long id) {
		return repository.findOne(id);
	}
}