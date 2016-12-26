package com.order;

import java.util.List;

public interface OrderService {
	List<Orderr> findAll();

	Orderr save(Orderr order);

	Orderr findOne(Long id);

	void delete(Long id);
}
