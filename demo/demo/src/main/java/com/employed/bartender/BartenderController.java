package com.employed.bartender;

import java.util.ArrayList;
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

import com.drink.Drink;
import com.order.DrinkStatus;
import com.order.OrderService;
import com.order.Orderr;

@RestController
@RequestMapping("/bartender")
public class BartenderController {

	private HttpSession httpSession;

	private final BartenderService bartenderService;
	private final OrderService orderService;

	@Autowired
	public BartenderController(final HttpSession httpSession, final BartenderService bartenderService,
			final OrderService orderService) {
		this.httpSession = httpSession;
		this.bartenderService = bartenderService;
		this.orderService = orderService;
	}

	@SuppressWarnings("unused")
	@GetMapping("/checkRights")
	public boolean checkRights() {
		try {
			Bartender bartender = ((Bartender) httpSession.getAttribute("user"));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@GetMapping
	public ResponseEntity<Bartender> findBartender() {
		Long id = ((Bartender) httpSession.getAttribute("user")).getId();
		Bartender bartender = bartenderService.findOne(id);
		return new ResponseEntity<Bartender>(bartender, HttpStatus.OK);
	}

	/*
	 * @GetMapping public ResponseEntity<List<Bartender>> findAll() { return new
	 * ResponseEntity<>(bartenderService.findAll(), HttpStatus.OK); }
	 */

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void save(@Valid @RequestBody Bartender bartender) {
		bartender.setId(null);
		bartender.setRegistrated("0");
		bartenderService.save(bartender);
	}

	@GetMapping(path = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Bartender findOne(@PathVariable Long id) {
		Bartender bartender = bartenderService.findOne(id);
		Optional.ofNullable(bartender).orElseThrow(() -> new ResourceNotFoundException("resourceNotFound!"));
		return bartender;
	}

	// 2.4. sanker ima mogucnost da azurira podatke
	@PutMapping(path = "/profile{id}")
	@ResponseStatus(HttpStatus.OK)
	public Bartender update(@PathVariable Long id, @Valid @RequestBody Bartender bartender) {
		Optional.ofNullable(bartenderService.findOne(id))
				.orElseThrow(() -> new ResourceNotFoundException("Resource Not Found!"));
		bartender.setId(id);
		return bartenderService.save(bartender);
	}

	// 2.4 prikaz porudzbina za sankera
	@GetMapping(path = "/orders")
	public ResponseEntity<List<Drink>> findAllOrdrers() {
		Long id = ((Bartender) httpSession.getAttribute("user")).getId();
		// Bartender bartender = ((Bartender) httpSession.getAttribute("user"));
		List<Orderr> orders = bartenderService.findOne(id).getOrders();
		Optional.ofNullable(orders).orElseThrow(() -> new ResourceNotFoundException("Resource Not Found!"));

		List<Drink> drinks = new ArrayList<Drink>();

		for (int i = 0; i < orders.size(); i++) {
			if (orders.get(i).getDrinks().size() != 0 && orders.get(i).getDrinkStatus() == null) {
				for (int j = 0; j < orders.get(i).getDrinks().size(); j++) {
					drinks.add(orders.get(i).getDrinks().get(j));
				}
			}
		}

		return new ResponseEntity<>(drinks, HttpStatus.OK);
	}

	// spisak pica koja su spremna
	@GetMapping(path = "/readyDrinks")
	public ResponseEntity<List<Drink>> readyDrinks() {
		Long id = ((Bartender) httpSession.getAttribute("user")).getId();
		// Bartender bartender = ((Bartender) httpSession.getAttribute("user"));
		Bartender bartender = bartenderService.findOne(id);
		List<Orderr> orders = bartender.getOrders();

		Optional.ofNullable(orders).orElseThrow(() -> new ResourceNotFoundException("Resource Not Found!"));

		List<Drink> drinks = new ArrayList<Drink>();

		for (int i = 0; i < orders.size(); i++) {
			if (orders.get(i).getDrinks().size() != 0 && orders.get(i).getDrinkStatus() != null
					&& orders.get(i).getDrinkStatus().compareTo(DrinkStatus.finished) == 0) {
				for (int j = 0; j < orders.get(i).getDrinks().size(); j++) {
					drinks.add(orders.get(i).getDrinks().get(j));
				}
			}
		}

		return new ResponseEntity<>(drinks, HttpStatus.OK);
	}

	// 2.4 sanker signalizir da je odgovarajuce pice spremno
	@GetMapping(path = "/drinkReady/{orderId}")
	@ResponseStatus(HttpStatus.OK)
	public Orderr drinkReady(@PathVariable Long orderId) {
		Optional.ofNullable(orderService.findOne(orderId))
				.orElseThrow(() -> new ResourceNotFoundException("Resource Not Found!"));
		Orderr order = orderService.findOne(orderId);
		order.setDrinkStatus(DrinkStatus.finished);
		order.setId(orderId);
		return orderService.save(order);
	}

}
