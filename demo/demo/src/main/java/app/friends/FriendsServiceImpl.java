package app.friends;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
@Transactional
public class FriendsServiceImpl implements FriendsService {
	private final FriendsRepository repository;

	@Autowired
	public FriendsServiceImpl(final FriendsRepository repository) {
		this.repository = repository;
	}

	@Override
	public void save(Friends friends) {
		repository.save(friends);
	}

	@Override
	public List<Friends> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public void remove(Friends friends) {
		repository.delete(friends);
	}

	@Override
	public void remove(Long id) {
		repository.delete(id);
	}
	
	
	
}