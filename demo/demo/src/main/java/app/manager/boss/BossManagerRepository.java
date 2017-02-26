package app.manager.boss;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface BossManagerRepository extends PagingAndSortingRepository<BossManager, Long> {

	public BossManager findByMailAndPassword(String mail, String password);

	public BossManager findByMail(String mail);
}