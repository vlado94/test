package app.manager.restaurant;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
@Transactional
public class RestaurantManagerServiceImpl implements RestaurantManagerService {

	private final RestaurantManagerRepository repository;

	@Autowired
	public RestaurantManagerServiceImpl(final RestaurantManagerRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<RestaurantManager> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public RestaurantManager save(RestaurantManager restaurantManager) {
		return repository.save(restaurantManager);
	}

	@Override
	public RestaurantManager findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public RestaurantManager findByMailAndPassword(String mail, String password) {
		return repository.findByMailAndPassword(mail, password);
	}

	@Override
	public RestaurantManager findByMail(String mail) {
		return repository.findByMail(mail);
	}
}