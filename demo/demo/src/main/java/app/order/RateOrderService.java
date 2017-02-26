package app.order;

import java.util.List;

public interface RateOrderService {
	List<RateOrder> findAll();

	RateOrder save(RateOrder rateOrder);

	RateOrder findOne(Long id);

	void delete(Long id);
}
