package app.manager.changedShiftWaiter;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
@Transactional
public class ChangedShiftWaiterServiceImpl implements ChangedShiftWaiterService {
	private final ChangedShiftWaiterRepository repository;

	@Autowired
	public ChangedShiftWaiterServiceImpl(final ChangedShiftWaiterRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<ChangedShiftWaiter> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public ChangedShiftWaiter save(ChangedShiftWaiter changedShift) {
		return repository.save(changedShift);
	}

	@Override
	public ChangedShiftWaiter findOne(Long id) {
		return repository.findOne(id);
	}
}