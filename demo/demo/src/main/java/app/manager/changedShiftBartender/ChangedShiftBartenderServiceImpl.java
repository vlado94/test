package app.manager.changedShiftBartender;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
@Transactional
public class ChangedShiftBartenderServiceImpl implements ChangedShiftBartenderService {
	private final ChangedShiftBartenderRepository repository;

	@Autowired
	public ChangedShiftBartenderServiceImpl(final ChangedShiftBartenderRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<ChangedShiftBartender> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public ChangedShiftBartender save(ChangedShiftBartender changedShift) {
		return repository.save(changedShift);
	}

	@Override
	public ChangedShiftBartender findOne(Long id) {
		return repository.findOne(id);
	}
}