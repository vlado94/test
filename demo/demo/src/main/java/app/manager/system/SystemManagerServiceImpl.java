package app.manager.system;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
@Transactional
public class SystemManagerServiceImpl implements SystemManagerService {
	private final SystemManagerRepository repository;

	@Autowired
	public SystemManagerServiceImpl(final SystemManagerRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<SystemManager> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public SystemManager save(SystemManager systemManager) {
		return repository.save(systemManager);
	}

	@Override
	public SystemManager findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public SystemManager findOne(String mail, String password) {
		return repository.findByMailAndPassword(mail, password);
	}
	
	@Override
	public SystemManager findOneWithMail(String mail) {
		return repository.findByMail(mail);
	}

	@Override
	public void delete(Long id) {
		repository.delete(id);
	}
}