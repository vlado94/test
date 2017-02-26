package app.manager.changedShiftCook;

import java.util.List;

public interface ChangedShiftCookService {

	List <ChangedShiftCook> findAll();
	
	ChangedShiftCook save(ChangedShiftCook changedShift);
	
	ChangedShiftCook findOne(Long id);
}