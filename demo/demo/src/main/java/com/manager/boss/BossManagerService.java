package com.manager.boss;

import java.util.List;

public interface BossManagerService {

	List <BossManager> findAll();
	
	BossManager save(BossManager guest);
	
	BossManager findByMailAndPassword(String mail,String password);

	BossManager findByMail(String mail);
	
	BossManager findOne(Long id);
}