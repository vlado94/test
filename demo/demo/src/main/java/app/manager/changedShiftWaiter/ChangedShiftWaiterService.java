package app.manager.changedShiftWaiter;

import java.util.List;

public interface ChangedShiftWaiterService {

	List <ChangedShiftWaiter> findAll();
	
	ChangedShiftWaiter save(ChangedShiftWaiter changedShift);
	
	ChangedShiftWaiter findOne(Long id);
}