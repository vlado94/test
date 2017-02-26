package app.restaurant;

import java.util.List;

public interface TableService {
	List<Table> findAll();

	Table save(Table restaurant);

	Table findOne(Long id);

	void delete(Long id);
	
	void deleteAll();
}
