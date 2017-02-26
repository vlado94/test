package app.order;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
@Transactional
public class RateServiceServiceImpl implements RateServiceService{

	private final RateServiceRepository repository;
	
	@Autowired
	public RateServiceServiceImpl(final RateServiceRepository repository){
		this.repository = repository;
	}
	
	@Override
	public List<RateService> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public RateService save(RateService rateService) {
		return repository.save(rateService);
	}

	@Override
	public RateService findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public void delete(Long id) {
		repository.delete(id);
	}

}
