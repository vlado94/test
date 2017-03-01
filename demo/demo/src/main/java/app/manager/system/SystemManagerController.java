package app.manager.system;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.ws.rs.BadRequestException;

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

import app.bidder.Bidder;
import app.dish.Dish;
import app.drink.Drink;
import app.employed.bartender.Bartender;
import app.employed.cook.Cook;
import app.employed.waiter.Waiter;
import app.manager.changedShiftBartender.ChangedShiftBartender;
import app.manager.changedShiftCook.ChangedShiftCook;
import app.manager.changedShiftWaiter.ChangedShiftWaiter;
import app.manager.restaurant.RestaurantManager;
import app.manager.restaurant.RestaurantManagerService;
import app.order.Orderr;
import app.restaurant.RateRestaurant;
import app.restaurant.Restaurant;
import app.restaurant.RestaurantService;
import app.restaurant.Segment;
import app.restaurant.restaurantOrder.RestaurantOrderr;

@RestController
@RequestMapping("/systemManager")
public class SystemManagerController {

	private HttpSession httpSession;
	private final RestaurantManagerService restaurantManagerService;
	private final RestaurantService restaurantService;
	private final SystemManagerService systemManagerService;

	@Autowired
	public SystemManagerController(final RestaurantManagerService restaurantManagerService,
			final RestaurantService restaurantService, final HttpSession httpSession,
			SystemManagerService systemManagerService) {
		this.restaurantManagerService = restaurantManagerService;
		this.httpSession = httpSession;
		this.restaurantService = restaurantService;
		this.systemManagerService = systemManagerService;
	}

	@GetMapping("/checkRights")
	@ResponseStatus(HttpStatus.OK)
	public boolean checkRights() {
		try {
			SystemManager systemManager = ((SystemManager) httpSession.getAttribute("user"));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// izlistavanje svih menadzera restorana
	@GetMapping("/restaurantManager")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<List<RestaurantManager>> findAllRestaurantManagers() {
		return new ResponseEntity<>(restaurantManagerService.findAll(), HttpStatus.OK);
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public SystemManager findSystemManager() {
		return ((SystemManager) httpSession.getAttribute("user"));
	}

	// izlistavanje svih menadzera restorana koji nemaju radno mesto
	@GetMapping(path = "/freeRestaurantManager")
	@ResponseStatus(HttpStatus.OK)
	public List<RestaurantManager> findAllFreeRestaurantManagers() {
		// ovo ce se kasnije promeniti da ide odmah na bazu, sa posebnim upitom
		List<RestaurantManager> managers = restaurantManagerService.findAll();
		List<RestaurantManager> result = new ArrayList<RestaurantManager>();
		for (int i = 0; i < managers.size(); i++) {
			if(!checkIfWorkInSomeRestaurant(managers.get(i)))
				result.add(managers.get(i));
		}

		return result;
	}
	
	private boolean checkIfWorkInSomeRestaurant(RestaurantManager restaurantManager) {
		List<Restaurant> restaurants = restaurantService.findAll();
		for (int j = 0; j < restaurants.size(); j++) {
			for(int k =0;k < restaurants.get(j).getRestaurantManagers().size();k++)
				if (restaurantManager.getId() == restaurants.get(j).getRestaurantManagers().get(k).getId())
					return true;
		}
		
		return false;
	}

	// dodavanje novog menadzera restorana
	@PostMapping("/restaurantManager")
	@ResponseStatus(HttpStatus.CREATED)
	public void saveRestaurantManager(@Valid @RequestBody RestaurantManager restaurantManager) {
		restaurantManager.setId(null);
		restaurantManager.setRegistrated("0");
		restaurantManagerService.save(restaurantManager);
	}

	// izlistavanje svih restorana
	@GetMapping(path = "/restaurant")
	@ResponseStatus(HttpStatus.OK)
	public List<Restaurant> findAllRestaurant() {
		return restaurantService.findAll();
	}

	// 2.3
	// registrovanje novih restorana, sa vec postojecim menadzerima
	// isti,mora postojati menadzer da bi
	// bio postavljen za menadzer datog restoran
	@PostMapping(path = "/restaurant")
	@ResponseStatus(HttpStatus.CREATED)
	public void saveRestaurant(@RequestBody Restaurant restaurant) {
		try {
			restaurant.setId(null);
			restaurant = initializeRestaurant(restaurant);
			restaurantService.save(restaurant);
		}
		catch (Exception e) {
			throw new BadRequestException();
		}
	}
	
	private Restaurant initializeRestaurant(Restaurant restaurant) {
		restaurant.setFood(new ArrayList<Dish>());
		restaurant.setDrinks(new ArrayList<Drink>());
		restaurant.setSegments(new ArrayList<Segment>());
		restaurant.setWaiters(new ArrayList<Waiter>());
		restaurant.setCooks(new ArrayList<Cook>());
		restaurant.setBartenders(new ArrayList<Bartender>());
		restaurant.setBidders(new ArrayList<Bidder>());
		restaurant.setRateRestaurant(new ArrayList<RateRestaurant>());
		restaurant.setOrder(new ArrayList<Orderr>());
		restaurant.setRestaurantOrders(new ArrayList<RestaurantOrderr>());
		restaurant.setChangedShiftsForCooks(new ArrayList<ChangedShiftCook>());
		restaurant.setChangedShiftsForBartenders(new ArrayList<ChangedShiftBartender>());
		restaurant.setChangedShiftsForWaiters(new ArrayList<ChangedShiftWaiter>());
		restaurant.setSummRate(0.0);
		return restaurant;		
	}

	@PutMapping(path = "/{id}")
	public SystemManager updateSystemManager(@PathVariable Long id, @Valid @RequestBody SystemManager systemManager) {
		Optional.ofNullable(restaurantManagerService.findOne(id))
				.orElseThrow(() -> new ResourceNotFoundException("resourceNotFound!"));
		systemManager.setId(id);
		return systemManagerService.save(systemManager);
	}
}