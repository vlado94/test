package com.bidder;

import java.util.List;

public interface BidderService {
	List<Bidder> findAll();

	Bidder save(Bidder bidder);

	Bidder findOne(Long id);

	Bidder findOne(String mail, String password);

	Bidder findOneWithMail(String mail);

	void delete(Long id);
}
