package app.manager.changedShiftCook;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
@Transactional
public class ChangedShiftCookServiceImpl implements ChangedShiftCookService {
	private final ChangedShiftCookRepository repository;

	@Autowired
	public ChangedShiftCookServiceImpl(final ChangedShiftCookRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<ChangedShiftCook> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public ChangedShiftCook save(ChangedShiftCook changedShift) {
		return repository.save(changedShift);
	}

	@Override
	public ChangedShiftCook findOne(Long id) {
		return repository.findOne(id);
	}
}