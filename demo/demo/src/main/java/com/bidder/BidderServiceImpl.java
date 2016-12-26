package com.bidder;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
@Transactional
public class BidderServiceImpl implements BidderService {
	private final BidderRepository repository;

	@Autowired
	public BidderServiceImpl(final BidderRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<Bidder> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public Bidder save(Bidder bidder) {
		return repository.save(bidder);
	}

	@Override
	public Bidder findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public void delete(Long id) {
		repository.delete(id);
	}

	// ovo se kasnije na repo spusta
	@Override
	public Bidder findOne(String mail, String password) {
		List<Bidder> bidders = (List<Bidder>) repository.findAll();
		for (int i = 0; i < bidders.size(); i++) {
			if (bidders.get(i).getMail().equals(mail) && bidders.get(i).getMail().equals(password))
				return bidders.get(i);
		}
		return null;
	}

	@Override
	public Bidder findOneWithMail(String mail) {
		List<Bidder> list = findAll();
		for (int i = 0; i < list.size(); i++)
			if (list.get(i).getMail().equals(mail))
				return list.get(i);
		return null;
	}
}
