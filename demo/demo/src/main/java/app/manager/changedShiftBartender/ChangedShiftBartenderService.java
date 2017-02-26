package app.manager.changedShiftBartender;

import java.util.List;

public interface ChangedShiftBartenderService {

	List <ChangedShiftBartender> findAll();
	
	ChangedShiftBartender save(ChangedShiftBartender changedShift);
	
	ChangedShiftBartender findOne(Long id);
}