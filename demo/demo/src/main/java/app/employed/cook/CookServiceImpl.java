package app.employed.cook;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
@Transactional
public class CookServiceImpl implements CookService {

	private final CookRepository repository;

	@Autowired
	public CookServiceImpl(final CookRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<Cook> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public Cook save(Cook cook) {
		return repository.save(cook);
	}

	@Override
	public Cook findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public Cook findOne(String mail, String password) {
		List<Cook> list = findAll();
		for (int i = 0; i < list.size(); i++)
			if (list.get(i).getMail().equals(mail) && list.get(i).getPassword().equals(password))
				return list.get(i);
		return null;
	}

	@Override
	public void delete(Long id) {
		repository.delete(id);
	}

	@Override
	public Cook findOneWithMail(String mail) {
		List<Cook> list = findAll();
		for (int i = 0; i < list.size(); i++)
			if (list.get(i).getMail().equals(mail))
				return list.get(i);
		return null;
	}
}
