package app.order;

import java.util.List;

public interface RateServiceService {
	List<RateService> findAll();

	RateService save(RateService rateService);

	RateService findOne(Long id);

	void delete(Long id);
}
