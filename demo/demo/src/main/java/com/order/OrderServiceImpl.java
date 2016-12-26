package com.order;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
	private final OrderRepository repository;

	@Autowired
	public OrderServiceImpl(final OrderRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<Orderr> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public Orderr save(Orderr order) {
		return repository.save(order);
	}

	@Override
	public Orderr findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public void delete(Long id) {
		repository.delete(id);
	}

}
