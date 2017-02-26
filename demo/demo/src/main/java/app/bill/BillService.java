package app.bill;

import java.util.List;


public interface BillService {
	List<Bill> findAll();

	Bill save(Bill bill);

	Bill findOne(Long id);

	void delete(Long id);
}
