package app.common;

import java.util.NoSuchElementException;
import java.util.Random;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import app.bidder.Bidder;
import app.bidder.BidderService;
import app.employed.bartender.Bartender;
import app.employed.bartender.BartenderService;
import app.employed.cook.Cook;
import app.employed.cook.CookService;
import app.employed.waiter.Waiter;
import app.employed.waiter.WaiterService;
import app.guest.Guest;
import app.guest.GuestService;
import app.manager.boss.BossManager;
import app.manager.boss.BossManagerService;
import app.manager.restaurant.RestaurantManager;
import app.manager.restaurant.RestaurantManagerService;
import app.manager.system.SystemManager;
import app.manager.system.SystemManagerService;

@RestController
@RequestMapping("/commonController")
public class CommonController {

	private HttpSession httpSession;

	private BossManagerService bossManagerService;
	private RestaurantManagerService restaurantManagerService;
	private GuestService guestService;
	private SystemManagerService systemManagerService;
	private BidderService bidderService;
	private BartenderService bartenderService;
	private CookService cookService;
	private WaiterService waiterService;
	private JavaMailSender javaMailSender;

	@Autowired
	public CommonController(final HttpSession httpSession, final BossManagerService bossManagerService,
			final RestaurantManagerService restaurantManagerService, final GuestService guestService,
			final SystemManagerService systemManagerService, final BidderService bidderService,
			final CookService cookService, final WaiterService waiterService, final BartenderService bartenderService,
			final JavaMailSender javaMailSender) {
		this.httpSession = httpSession;
		this.bossManagerService = bossManagerService;
		this.restaurantManagerService = restaurantManagerService;
		this.guestService = guestService;
		this.systemManagerService = systemManagerService;
		this.bidderService = bidderService;
		this.bartenderService = bartenderService;
		this.cookService = cookService;
		this.waiterService = waiterService;
		this.javaMailSender = javaMailSender;
	}

	@PostMapping(path = "/logIn")
	public ResponseEntity<String> logIn(@RequestBody User userInput) {
		User user = null;
		String userType = "";
		Long id = 0l;
		if (bossManagerService.findByMailAndPassword(userInput.getMail(), userInput.getPassword()) != null) {
			user = bossManagerService.findByMailAndPassword(userInput.getMail(), userInput.getPassword());
			id = bossManagerService.findByMailAndPassword(userInput.getMail(), userInput.getPassword()).getId();
			userType = "boss";
		} else if (restaurantManagerService.findByMailAndPassword(userInput.getMail(),
				userInput.getPassword()) != null) {
			user = restaurantManagerService.findByMailAndPassword(userInput.getMail(), userInput.getPassword());
			id = restaurantManagerService.findByMailAndPassword(userInput.getMail(), userInput.getPassword()).getId();
			userType = "restaurant";
		} else if (systemManagerService.findOne(userInput.getMail(), userInput.getPassword()) != null) {
			user = systemManagerService.findOne(userInput.getMail(), userInput.getPassword());
			id = systemManagerService.findOne(userInput.getMail(), userInput.getPassword()).getId();
			userType = "system";
		} else if (guestService.findByMailAndPassword(userInput.getMail(), userInput.getPassword()) != null) {
			user = guestService.findByMailAndPassword(userInput.getMail(), userInput.getPassword());
			id = guestService.findByMailAndPassword(userInput.getMail(), userInput.getPassword()).getId();
			if (user.getRegistrated().equals("1"))
				userType = "guest";
			else
				userType = "guestNotActivated";
		} else if (bidderService.findOne(userInput.getMail(), userInput.getPassword()) != null) {
			user = bidderService.findOne(userInput.getMail(), userInput.getPassword());
			id = bidderService.findOne(userInput.getMail(), userInput.getPassword()).getId();
			userType = "bidder";
		} else if (bartenderService.findOne(userInput.getMail(), userInput.getPassword()) != null) {
			user = bartenderService.findOne(userInput.getMail(), userInput.getPassword());
			id = bartenderService.findOne(userInput.getMail(), userInput.getPassword()).getId();
			userType = "bartender";
		} else if (waiterService.findOne(userInput.getMail(), userInput.getPassword()) != null) {
			user = waiterService.findOne(userInput.getMail(), userInput.getPassword());
			id = waiterService.findOne(userInput.getMail(), userInput.getPassword()).getId();
			userType = "waiter";
		} else if (cookService.findOne(userInput.getMail(), userInput.getPassword()) != null) {
			user = cookService.findOne(userInput.getMail(), userInput.getPassword());
			id = cookService.findOne(userInput.getMail(), userInput.getPassword()).getId();
			userType = "cook";
		}
		if (user != null) {
			httpSession.setAttribute("user", user);
			if (!user.getRegistrated().equals("0") || userType.equals("guest") || userType.equals("guestNotActivated"))
				return new ResponseEntity<>(userType, HttpStatus.OK);
			return new ResponseEntity<>("" + id, HttpStatus.OK);
		} else
			throw new NoSuchElementException("Ne postoji korisnik sa tim parametrima.");
	}

	@GetMapping(path = "/logOut")
	public void logOut() {

		httpSession.invalidate();
	}

	@GetMapping(path = "/getLoggedUser")
	public User getLoggedUser() {
		return (User) httpSession.getAttribute("user");
	}

	// registracija gosta
	@PostMapping(path = "/registration")
	@ResponseStatus(HttpStatus.CREATED)
	public void save(@Valid @RequestBody Guest guest) {
		guest.setId(null);
		Random rand = new Random();
		int randomNum = rand.nextInt(1000) + 2;
		guest.setRegistrated(Integer.toString(randomNum));

		guestService.save(guest);

		// ----Salje mejl sam sebi, zbog testiranja...
		try {
			SimpleMailMessage mail = new SimpleMailMessage();
			mail.setTo("isarestorani2@gmail.com");
			mail.setFrom("isarestorani2@gmail.com");
			mail.setSubject("Activation link");
			mail.setText("Your activation link is: http://localhost:8080/#/activation/" + randomNum);

			javaMailSender.send(mail);
		} catch (Exception m) {
			m.printStackTrace();
		}
	}

	// izmena sifre nakon prvog logovanja
	@PutMapping(path = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void update(@PathVariable Long id, @Valid @RequestBody User userInput) {
		if (bossManagerService.findByMail(userInput.getMail()) != null) {
			BossManager bossManager = bossManagerService.findOne(id);
			bossManager.setId(id);
			bossManager.setRegistrated("1");
			bossManager.setPassword(userInput.getPassword());
			bossManagerService.save(bossManager);
		} else if (restaurantManagerService.findByMail(userInput.getMail()) != null) {
			RestaurantManager restaurantManager = restaurantManagerService.findOne(id);
			restaurantManager.setId(id);
			restaurantManager.setRegistrated("1");
			restaurantManager.setPassword(userInput.getPassword());
			restaurantManagerService.save(restaurantManager);
		} else if (systemManagerService.findOneWithMail(userInput.getMail()) != null) {
			SystemManager systemManager = systemManagerService.findOne(id);
			systemManager.setId(id);
			systemManager.setRegistrated("1");
			systemManager.setPassword(userInput.getPassword());
			systemManagerService.save(systemManager);
		} else if (bidderService.findOneWithMail(userInput.getMail()) != null) {
			Bidder bidder = bidderService.findOne(id);
			bidder.setId(id);
			bidder.setRegistrated("1");
			bidder.setPassword(userInput.getPassword());
			bidderService.save(bidder);
		} else if (bartenderService.findOneWithMail(userInput.getMail()) != null) {
			Bartender bartender = bartenderService.findOne(id);
			bartender.setId(id);
			bartender.setRegistrated("1");
			bartender.setPassword(userInput.getPassword());
			bartenderService.save(bartender);
		} else if (cookService.findOneWithMail(userInput.getMail()) != null) {
			Cook cook = cookService.findOne(id);
			cook.setId(id);
			cook.setRegistrated("1");
			cook.setPassword(userInput.getPassword());
			cookService.save(cook);
		} else if (waiterService.findOneWithMail(userInput.getMail()) != null) {
			Waiter waiter = waiterService.findOne(id);
			waiter.setId(id);
			waiter.setRegistrated("1");
			waiter.setPassword(userInput.getPassword());
			waiterService.save(waiter);
		}
	}
}