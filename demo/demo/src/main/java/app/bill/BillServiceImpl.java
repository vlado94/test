package app.bill;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
@Transactional
public class BillServiceImpl implements BillService{
	private final BillRepository repository;
	
	@Autowired
	public BillServiceImpl(final BillRepository repository){
		this.repository = repository;
	}
	
	@Override
	public List<Bill> findAll() {
		return Lists.newArrayList(repository.findAll());
		
	}

	@Override
	public Bill save(Bill bill) {
		return repository.save(bill);
	}

	@Override
	public Bill findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public void delete(Long id) {
		repository.delete(id);
	}

}
