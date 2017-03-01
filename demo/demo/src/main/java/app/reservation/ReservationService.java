package app.reservation;

import java.util.List;


public interface ReservationService {
	List<Reservation> findAll();

	Reservation save(Reservation reservation);

	Reservation findOne(Long id);

	void delete(Long id);
	
}
