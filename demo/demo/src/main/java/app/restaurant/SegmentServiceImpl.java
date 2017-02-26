package app.restaurant;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;


@Service
@Transactional
public class SegmentServiceImpl implements SegmentService {
	private final SegmentRepository repository;
	
	@Autowired
	public SegmentServiceImpl(final SegmentRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<Segment> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public Segment save(Segment segment) {
		return repository.save(segment);
	}

	@Override
	public Segment findOne(Long id) {
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
