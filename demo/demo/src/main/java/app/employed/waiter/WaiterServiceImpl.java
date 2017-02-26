package app.employed.waiter;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
@Transactional
public class WaiterServiceImpl implements WaiterService {

	private final WaiterRepository repository;
	//private Waiter waiter;

	@Autowired
	public WaiterServiceImpl(final WaiterRepository repository) {
		this.repository = repository;
		//this.waiter = waiter;
	}

	@Override
	public List<Waiter> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public Waiter save(Waiter waiter) {
		return repository.save(waiter);
	}

	@Override
	public Waiter findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public void delete(Long id) {
		repository.delete(id);
	}
	
	@Override
	public Waiter findOne(String mail,String password) {
		List<Waiter> list = findAll();
		for(int i=0;i<list.size();i++)
			if(list.get(i).getMail().equals(mail) && list.get(i).getPassword().equals(password))
				return list.get(i);
		return null;
	}
	
	@Override
	public Waiter findOneWithMail(String mail) {
		List<Waiter> list = findAll();
		for(int i=0;i<list.size();i++)
			if(list.get(i).getMail().equals(mail))
				return list.get(i);
		return null;
	}

	/*@Override
	public boolean check() {
		if (waiter.getOrders().size() > 0) {
			for (int i = 0; i < waiter.getOrders().size(); i++) {
				if (waiter.getOrders().get(i).getRestaurant() != waiter.getRestaurant()) {
					return false;
				}
			}
			return true;
		}else{
			return true;
		}
	}*/

}
