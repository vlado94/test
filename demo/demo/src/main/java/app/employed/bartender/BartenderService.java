package app.employed.bartender;

import java.util.List;


public interface BartenderService {

	List<Bartender> findAll();

	Bartender save(Bartender bartender);

	Bartender findOne(Long id);
	
	Bartender findOneMailAndPass(String mail,String password);

	Bartender findOneWithMail(String mail);
	
	void delete(Long id);

}
