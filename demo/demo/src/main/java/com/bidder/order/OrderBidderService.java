package com.bidder.order;

import java.util.List;

public interface OrderBidderService {
	List<OrderBidder> findAll();

	OrderBidder save(OrderBidder order);

	OrderBidder findOne(Long id);

	void delete(Long id);
}
