package app.employed.bartender;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
@Transactional
public class BartenderServiceImpl implements BartenderService {

	private final BartenderRepository repository;

	@Autowired
	public BartenderServiceImpl(final BartenderRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<Bartender> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public Bartender findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public Bartender save(Bartender bartender) {
		return repository.save(bartender);
	}

	@Override
	public void delete(Long id) {
		repository.delete(id);
	}

	@Override
	public Bartender findOneMailAndPass(String mail, String password) {
		List<Bartender> bartenders = (List<Bartender>) repository.findAll();
		for (int i = 0; i < bartenders.size(); i++) {
			if (bartenders.get(i).getMail().equals(mail) && bartenders.get(i).getPassword().equals(password))
				return bartenders.get(i);
		}
		return null;
	}

	@Override
	public Bartender findOneWithMail(String mail) {
		List<Bartender> list = findAll();
		for (int i = 0; i < list.size(); i++)
			if (list.get(i).getMail().equals(mail))
				return list.get(i);
		return null;
	}
}
