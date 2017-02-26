package app.manager.system;

import java.util.List;

public interface SystemManagerService {
	List<SystemManager> findAll();

	SystemManager save(SystemManager systemManager);

	SystemManager findOne(Long id);

	SystemManager findOne(String mail, String password);

	SystemManager findOneWithMail(String mail);
	
	void delete(Long id);
}
