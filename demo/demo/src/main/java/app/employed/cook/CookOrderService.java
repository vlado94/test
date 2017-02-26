package app.employed.cook;

import java.util.List;

public interface CookOrderService {
	List<CookOrder> findAll();

	CookOrder save(CookOrder cookOrder);

	void delete(CookOrder entity);

	CookOrder findOne(CookOrderId id);
}
