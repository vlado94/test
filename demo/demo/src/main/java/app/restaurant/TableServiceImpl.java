package app.restaurant;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;


@Service
@Transactional
public class TableServiceImpl implements TableService {
	private final TableRepository repository;
	
	@Autowired
	public TableServiceImpl(final TableRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public List<Table> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public Table save(Table table) {
		return repository.save(table);
	}

	@Override
	public Table findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public void delete(Long id) {
		repository.delete(id);
	}

	@Override
	public void deleteAll() {
		repository.deleteAll();
	}

}
