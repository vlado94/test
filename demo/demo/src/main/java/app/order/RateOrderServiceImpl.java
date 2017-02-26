package app.order;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
@Transactional
public class RateOrderServiceImpl implements RateOrderService{

	private final RateOrderRepository repository;
	
	@Autowired
	public RateOrderServiceImpl(final RateOrderRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public List<RateOrder> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public RateOrder save(RateOrder rateOrder) {
		return repository.save(rateOrder);
	}

	@Override
	public RateOrder findOne(Long id) {
		// TODO Auto-generated method stub
		return repository.findOne(id);
	}

	@Override
	public void delete(Long id) {
		repository.delete(id);
		
	}

	
	
}
