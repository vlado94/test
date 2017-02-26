package app.restaurant;

import java.util.List;

public interface SegmentService {
	List<Segment> findAll();

	Segment save(Segment segment);

	Segment findOne(Long id);

	void delete(Long id);
	
	void deleteAll();
}
