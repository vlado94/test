package app.reservation;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import app.guest.Guest;
import app.restaurant.Restaurant;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {
	private final ReservationRepository repository;
	
	@Autowired
	public ReservationServiceImpl(final ReservationRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public List<Reservation> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public Reservation findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public void delete(Long id) {
		repository.delete(id);
	}

	@Override
	public Reservation save(Reservation reservation) {
		return repository.save(reservation);
	}


}
