package app.manager.boss;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import app.manager.system.SystemManager;
import app.manager.system.SystemManagerService;

@RestController
@RequestMapping("/bossManager")
public class BossManagerController {

	private BossManagerService serviceBossManager;
	private SystemManagerService serviceSystemManager;
	private HttpSession httpSession;

	@Autowired
	public BossManagerController(final HttpSession httpSession, final SystemManagerService serviceSystemManager,
			final BossManagerService serviceBossManager) {
		this.serviceSystemManager = serviceSystemManager;
		this.serviceBossManager = serviceBossManager;
		this.httpSession = httpSession;
	}

	@SuppressWarnings("unused")
	@GetMapping("/checkRights")
	public boolean checkRights() {
		try {
			BossManager bossManager = ((BossManager) httpSession.getAttribute("user"));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@GetMapping(path="/boss")
	public BossManager findBossManager() {
		return ((BossManager) httpSession.getAttribute("user"));
	}
	
	@GetMapping
	public ResponseEntity<List<SystemManager>> findAllSystemMenagers() {
		return new ResponseEntity<>(serviceSystemManager.findAll(), HttpStatus.OK);
	}

	// 2.9
	// dodavanje novog sistemskog menadzera sistema
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void saveSystemManager(@Valid @RequestBody SystemManager systemManager) {
		// zastita u slucaju da mu npr preko postmana posaljemo id,da se
		// izignorise
		systemManager.setId(null);
		serviceSystemManager.save(systemManager);
	}

	// nalazenje jednog menadzera sistema
	@GetMapping(path = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public SystemManager findOne(@PathVariable Long id) {
		SystemManager systemManager = serviceSystemManager.findOne(id);
		Optional.ofNullable(systemManager).orElseThrow(() -> new ResourceNotFoundException("resourceNotFound!"));
		return systemManager;
	}

	@PutMapping(path = "/{id}")
	public BossManager updateBossManager(@PathVariable Long id, @Valid @RequestBody BossManager bossManager) {
		Optional.ofNullable(serviceBossManager.findOne(id))
				.orElseThrow(() -> new ResourceNotFoundException("resourceNotFound!"));
		bossManager.setId(id);
		return serviceBossManager.save(bossManager);
	}
}