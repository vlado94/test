package app.guest;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
@Transactional
public class GuestServiceImpl implements GuestService {
	private final GuestRepository repository;

	@Autowired
	public GuestServiceImpl(final GuestRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<Guest> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public Guest save(Guest guest) {
		return repository.save(guest);
	}

	@Override
	public Guest findOne(Long id) {
		return repository.findOne(id);
	}

	// ovo se kasnije na repo spusta
	@Override
	public Guest findByMailAndPassword (String mail, String password) {
		return repository.findByMailAndPassword(mail, password);
	}

	@Override
	public Guest findByMail(String mail) {
		return repository.findByMail(mail);
	}

	@Override
	public void activate(String regNum) {
		Guest guest = repository.findByRegistrated(regNum);
		guest.setRegistrated("1");
	}

	//treba da se iskljuci taj sam koji je logovan
	@Override
	public List<Guest> findByFirstAndLastName(String inputStr) {
		List<Guest> list1 = new ArrayList<Guest>();
		list1.addAll(repository.findByFirstnameContaining(inputStr));
		list1.addAll(repository.findByLastnameContaining(inputStr));
		return list1;
	}	
}