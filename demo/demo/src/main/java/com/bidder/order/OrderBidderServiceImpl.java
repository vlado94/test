package com.bidder.order;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
@Transactional
public class OrderBidderServiceImpl implements OrderBidderService {
	private final OrderBidderRepository repository;

	@Autowired
	public OrderBidderServiceImpl(final OrderBidderRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<OrderBidder> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public OrderBidder save(OrderBidder order) {
		return repository.save(order);
	}

	@Override
	public OrderBidder findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public void delete(Long id) {
		repository.delete(id);
	}

}
