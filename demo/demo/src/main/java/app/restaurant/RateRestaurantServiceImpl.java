package app.restaurant;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
@Transactional
public class RateRestaurantServiceImpl implements RateRestaurantService{

	private final RateRestaurantRepository repository;
	
	@Autowired
	public RateRestaurantServiceImpl(final RateRestaurantRepository repository){
		this.repository = repository;
	}
	
	@Override
	public List<RateRestaurant> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public RateRestaurant save(RateRestaurant rateRestaurant) {
		return repository.save(rateRestaurant);
	}

	@Override
	public RateRestaurant findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public void delete(Long id) {
		repository.delete(id);
	}

}
