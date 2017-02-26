package app.employed.cook;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
@Transactional
public class CookOrderServiceImpl implements CookOrderService{

	private final CookOrderRepository repository;

	@Autowired
	public CookOrderServiceImpl(final CookOrderRepository repository) {
		this.repository = repository;
	}

	
	@Override
	public List<CookOrder> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public CookOrder save(CookOrder cookOrder) {
		return repository.save(cookOrder);
	}

	@Override
	public CookOrder findOne(CookOrderId id) {
		return repository.findOne(id);
	}

	@Override
	public void delete(CookOrder entity) {
		repository.delete(entity);
	}

}
