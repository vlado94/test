package app.employed.cook;

import java.util.List;

public interface CookService {

	List<Cook> findAll();
	
	Cook save(Cook cook);
	
	Cook findOne(Long id);
	
	Cook findOne(String mail,String password);
	
	Cook findOneWithMail(String mail);
	
	void delete(Long id);
}
