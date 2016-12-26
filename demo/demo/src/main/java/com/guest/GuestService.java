package com.guest;

import java.util.List;

public interface GuestService {
	List<Guest> findAll();

	Guest save(Guest guest);

	Guest findOne(Long id);

	Guest findByMailAndPassword(String mail,String password);
	
	Guest findByMail(String mail);
	
	void activate(String regNum);
	
	List<Guest> findByFirstAndLastName(String inputStr);
}
