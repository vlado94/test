package app.manager.system;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SystemManagerRepository extends PagingAndSortingRepository<SystemManager, Long> {

	@Query("select r from SystemManager r where r.mail = ?1 and r.password = ?2")
	public SystemManager findByMailAndPassword(String mail, String password);

	public SystemManager findByMail(String mail);
}
