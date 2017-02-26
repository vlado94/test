package app.guest;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface GuestRepository extends PagingAndSortingRepository<Guest, Long> {

	public Guest findByMailAndPassword(String mail, String password);

	public Guest findByMail(String mail);

	public Guest findByRegistrated(String regNum);

	public List<Guest> findByFirstnameContaining(String regnum);

	public List<Guest> findByLastnameContaining(String regnum);

	
}