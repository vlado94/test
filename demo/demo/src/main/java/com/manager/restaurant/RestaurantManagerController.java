package com.manager.restaurant;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bidder.Bidder;
import com.dish.Dish;
import com.drink.Drink;
import com.employed.bartender.Bartender;
import com.employed.cook.Cook;
import com.employed.waiter.Waiter;
import com.restaurant.Restaurant;
import com.restaurant.RestaurantService;

@RestController
@RequestMapping("/restaurantManager")
public class RestaurantManagerController {

	private HttpSession httpSession;
	private RestaurantService restaurantService;
	private RestaurantManagerService restaurantManagerService;

	@Autowired
	public RestaurantManagerController(final HttpSession httpSession, final RestaurantService restaurantService,
			final RestaurantManagerService restaurantManagerService) {
		this.httpSession = httpSession;
		this.restaurantService = restaurantService;
		this.restaurantManagerService = restaurantManagerService;
	}

	@SuppressWarnings("unused")
	@GetMapping("/checkRights")
	public boolean checkRights() {
		try {
			RestaurantManager restaurantManager = ((RestaurantManager) httpSession.getAttribute("user"));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// spusta se pretraga na repo,menja se kasnije kad se uvede da restoran
	// sadrzi vise menadzera
	@GetMapping("/restaurant")
	@ResponseStatus(HttpStatus.OK)
	public Restaurant findRestaurantForRestaurantManager() {
		Long userId = ((RestaurantManager) httpSession.getAttribute("user")).getId();
		List<Restaurant> restaurants = restaurantService.findAll();
		for (int i = 0; i < restaurants.size(); i++) {
			Restaurant restaurant = restaurants.get(i);
			if (restaurant.getRestaurantManager().getId() == userId)
				return restaurant;
		}
		return null;
	}

	@PostMapping(path = "/restaurant/saveDrink")
	@ResponseStatus(HttpStatus.CREATED)
	public void saveDrink(@Valid @RequestBody Drink drink) {
		Restaurant restaurant = findRestaurantForRestaurantManager();
		restaurant.getDrinks().add(drink);
		restaurantService.save(restaurant);
	}

	@PostMapping(path = "/restaurant/saveDish")
	@ResponseStatus(HttpStatus.CREATED)
	public void saveDish(@Valid @RequestBody Dish dish) {
		Restaurant restaurant = findRestaurantForRestaurantManager();
		restaurant.getFood().add(dish);
		restaurantService.save(restaurant);
	}

	@PostMapping(path = "/restaurant/saveWaiter")
	@ResponseStatus(HttpStatus.CREATED)
	public void saveWaiter(@Valid @RequestBody Waiter waiter) {
		waiter.setRegistrated("0");
		Restaurant restaurant = findRestaurantForRestaurantManager();
		restaurant.getWaiters().add(waiter);
		restaurantService.save(restaurant);
	}

	@PostMapping(path = "/restaurant/saveCook")
	@ResponseStatus(HttpStatus.CREATED)
	public void saveCook(@Valid @RequestBody Cook cook) {
		cook.setRegistrated("0");
		Restaurant restaurant = findRestaurantForRestaurantManager();
		restaurant.getCooks().add(cook);
		restaurantService.save(restaurant);
	}

	@PostMapping(path = "/restaurant/saveBartender")
	@ResponseStatus(HttpStatus.CREATED)
	public void saveCook(@Valid @RequestBody Bartender bartender) {
		bartender.setRegistrated("0");
		Restaurant restaurant = findRestaurantForRestaurantManager();
		restaurant.getBartenders().add(bartender);
		restaurantService.save(restaurant);
	}

	@PostMapping(path = "/restaurant/saveBidder")
	@ResponseStatus(HttpStatus.CREATED)
	public void saveBidder(@Valid @RequestBody Bidder bidder) {
		bidder.setRegistrated("0");
		Restaurant restaurant = findRestaurantForRestaurantManager();
		restaurant.getBidders().add(bidder);
		restaurantService.save(restaurant);
	}

	@PutMapping(path = "/{id}")
	public RestaurantManager updateRestaurantManager(@PathVariable Long id,
			@Valid @RequestBody RestaurantManager restaurantManager) {
		Optional.ofNullable(restaurantManagerService.findOne(id))
				.orElseThrow(() -> new ResourceNotFoundException("resourceNotFound!"));
		restaurantManager.setId(id);
		return restaurantManagerService.save(restaurantManager);
	}
}