package app.manager.restaurant;

import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.ws.rs.BadRequestException;

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

import app.bidder.Bidder;
import app.bidder.BidderService;
import app.bill.Bill;
import app.dish.Dish;
import app.dish.DishService;
import app.drink.Drink;
import app.drink.DrinkService;
import app.employed.bartender.Bartender;
import app.employed.cook.Cook;
import app.employed.cook.CookOrder;
import app.employed.cook.CookService;
import app.employed.cook.TypeOfCooker;
import app.employed.waiter.Waiter;
import app.employed.waiter.WaiterService;
import app.manager.changedShiftBartender.ChangedShiftBartender;
import app.manager.changedShiftBartender.ChangedShiftBartenderService;
import app.manager.changedShiftCook.ChangedShiftCook;
import app.manager.changedShiftCook.ChangedShiftCookService;
import app.manager.changedShiftWaiter.ChangedShiftWaiter;
import app.manager.changedShiftWaiter.ChangedShiftWaiterService;
import app.offer.Offer;
import app.offer.OfferService;
import app.order.Orderr;
import app.restaurant.Description;
import app.restaurant.Restaurant;
import app.restaurant.RestaurantService;
import app.restaurant.Segment;
import app.restaurant.SegmentService;
import app.restaurant.TableService;
import app.restaurant.restaurantOrder.RestaurantOrderService;
import app.restaurant.restaurantOrder.RestaurantOrderr;

@RestController
@RequestMapping("/restaurantManager")
public class RestaurantManagerController {

	private HttpSession httpSession;
	private RestaurantService restaurantService;
	private BidderService bidderService;
	private WaiterService waiterService;
	private CookService cookService;
	private RestaurantOrderService restaurantOrderService;
	private RestaurantManagerService restaurantManagerService;
	private OfferService offerService;
	private SegmentService segmentService;
	private ChangedShiftCookService changedShiftCookService;
	private ChangedShiftBartenderService changedShiftBartenderService;
	private ChangedShiftWaiterService changedShiftWaiterService;
	private DrinkService drinkService;
	private DishService dishService;
	private TableService tableService;

	@Autowired
	public RestaurantManagerController(final HttpSession httpSession, final RestaurantService restaurantService,
			final RestaurantManagerService restaurantManagerService, final BidderService bidderService, WaiterService waiterService, CookService cookService,
			final RestaurantOrderService restaurantOrderService, final OfferService offerService,
			final SegmentService segmentService, final TableService tableService,
			final ChangedShiftCookService changedShiftCookService,
			final ChangedShiftWaiterService changedShiftWaiterService, final DishService dishService,
			final DrinkService drinkService, final ChangedShiftBartenderService changedShiftBartenderService) {
		this.httpSession = httpSession;
		this.restaurantService = restaurantService;
		this.bidderService = bidderService;
		this.waiterService = waiterService;
		this.cookService = cookService;
		this.restaurantOrderService = restaurantOrderService;
		this.restaurantManagerService = restaurantManagerService;
		this.offerService = offerService;
		this.segmentService = segmentService;
		this.changedShiftCookService = changedShiftCookService;
		this.changedShiftBartenderService = changedShiftBartenderService;
		this.changedShiftWaiterService = changedShiftWaiterService;
		this.dishService = dishService;
		this.drinkService = drinkService;
		this.tableService = tableService;
	}

	@GetMapping("/checkRights")
	@ResponseStatus(HttpStatus.OK)
	public RestaurantManager checkRights() {
		try {
			return ((RestaurantManager) httpSession.getAttribute("user"));
		} catch (Exception e) {
			return null;
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
			for (int j = 0; j < restaurant.getRestaurantManagers().size(); j++)
				if (restaurant.getRestaurantManagers().get(j).getId() == userId) {
					// sluzi za inicijalizaciju posto preko data u konsturktoru
					// nzm kako da dam default values
					if (restaurant.getSummRate() == 0) {
						// restaurant.setNumRate(0);
						restaurant.setSummRate(0.0);
						restaurantService.save(restaurant);
					}
					return restaurant;
				}
		}
		return null;
	}

	@PostMapping(path = "/restaurant/saveDrink")
	@ResponseStatus(HttpStatus.CREATED)
	public void saveDrink(@Valid @RequestBody Drink drink) {
		drink = setDrink(drink);
		Restaurant restaurant = findRestaurantForRestaurantManager();
		drinkService.save(drink);
		restaurant.getDrinks().add(drink);
		restaurantService.save(restaurant);
	}
	
	private Drink setDrink(Drink drink) {
		drink.setNumRate(0);
		drink.setSummRate(0);
		return drink;
	}

	@PostMapping(path = "/restaurant/saveDish")
	@ResponseStatus(HttpStatus.CREATED)
	public void saveDish(@RequestBody Dish dish) {
		dish = setDish(dish);
		Restaurant restaurant = findRestaurantForRestaurantManager();
		dishService.save(dish);
		restaurant.getFood().add(dish);
		restaurantService.save(restaurant);
	}
	
	private Dish setDish(Dish dish) {
		dish.setNumRate(new ArrayList<Integer>());
		dish.setRate(0.0);
		return dish;
	}

	@PostMapping(path = "/restaurant/saveWaiter")
	@ResponseStatus(HttpStatus.CREATED)
	public void saveWaiter(@RequestBody Waiter waiter) {
		waiter = setObjectWaiter(waiter);
		Restaurant restaurant = findRestaurantForRestaurantManager();
		waiterService.save(waiter);
		restaurant.getWaiters().add(waiter);
		restaurantService.save(restaurant);
	}

	private Waiter setObjectWaiter(Waiter waiter) {
		waiter.setRestaurant(findRestaurantForRestaurantManager());
		waiter.setNumRate(new ArrayList<Integer>());
		waiter.setRegistrated("0");
		waiter.setOrders(new ArrayList<Orderr>());
		waiter.setBills(new ArrayList<Bill>());
		return waiter;
	}

	@PostMapping(path = "/restaurant/saveCook/{cookType}")
	@ResponseStatus(HttpStatus.CREATED)
	public void saveCook(@PathVariable String cookType,@RequestBody Cook cook) {
		cook = setCook(cook,cookType);
		Restaurant restaurant = findRestaurantForRestaurantManager();
		restaurant.getCooks().add(cook);
		restaurantService.save(restaurant);
	}
	
	private Cook setCook(Cook cook,String cookType) {
		cook.setRegistrated("0");
		if(cookType.equals("salad"))
			cook.setTypeOfCooker(TypeOfCooker.salad);
		else if(cookType.equals("cooked"))
			cook.setTypeOfCooker(TypeOfCooker.cooked);
		else
			cook.setTypeOfCooker(TypeOfCooker.baked);
		cook.setRestaurant(findRestaurantForRestaurantManager());
		cook.setOrders(new ArrayList<CookOrder>());
		return cook;
	}

	@PostMapping(path = "/restaurant/saveBartender")
	@ResponseStatus(HttpStatus.CREATED)
	public void saveBartender(@Valid @RequestBody Bartender bartender) {
		bartender.setRegistrated("0");
		bartender.setOrders(new ArrayList<Orderr>());
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

	// brisanje sankera
	@PostMapping("/restaurant/deleteBartender")
	@ResponseStatus(HttpStatus.OK)
	public void deleteBartender(@Valid @RequestBody Bartender bartender) {
		Restaurant restaurant = findRestaurantForRestaurantManager();
		restaurant.getBartenders().remove(bartender);
		restaurantService.save(restaurant);
	}

	// brisanje konobara
	@PostMapping("/restaurant/deleteWaiter")
	@ResponseStatus(HttpStatus.OK)
	public void deleteWaiter(@Valid @RequestBody Waiter waiter) {
		Restaurant restaurant = findRestaurantForRestaurantManager();
		restaurant.getWaiters().remove(waiter);
		restaurantService.save(restaurant);
	}

	// brisanje kuvara
	@PostMapping("/restaurant/deleteCook")
	@ResponseStatus(HttpStatus.OK)
	public void deleteCook(@Valid @RequestBody Cook cook) {
		Restaurant restaurant = findRestaurantForRestaurantManager();
		restaurant.getCooks().remove(cook);
		cookService.delete(cook.getId());
		restaurantService.save(restaurant);
	}

	@GetMapping("/showFreeBidders")
	public ArrayList<Bidder> showFreeBidders() {
		ArrayList<Bidder> bidders = new ArrayList<Bidder>();
		List<Bidder> allBidders = bidderService.findAll();
		Restaurant restaurant = findRestaurantForRestaurantManager();
		for (int i = 0; i < allBidders.size(); i++) {
			if (!contains(restaurant, allBidders.get(i)))
				bidders.add(allBidders.get(i));
		}
		return bidders;
	}

	private boolean contains(Restaurant restaurant, Bidder bidder) {
		for (int j = 0; j < restaurant.getBidders().size(); j++) {
			if (bidder.getId() == restaurant.getBidders().get(j).getId())
				return true;
		}
		return false;
	}

	@PostMapping(path = "/restaurant/connectBidder")
	@ResponseStatus(HttpStatus.CREATED)
	public void connectBidder(@Valid @RequestBody Bidder bidder) {
		Bidder bidderOld = bidderService.findOne(bidder.getId());
		Restaurant restaurant = findRestaurantForRestaurantManager();
		restaurant.getBidders().add(bidderOld);
		restaurantService.save(restaurant);
	}

	@PostMapping(path = "/restaurant/createNewOffer")
	@ResponseStatus(HttpStatus.CREATED)
	public void createNewOffer(@Valid @RequestBody RestaurantOrderr restaurantOrderr) {
		Dish dish = restaurantOrderr.getDish();
		Drink drink = restaurantOrderr.getDrink();
		Restaurant restaurant = findRestaurantForRestaurantManager();
		setObject(dish, drink, restaurant, restaurantOrderr);
		restaurant.getRestaurantOrders().add(restaurantOrderr);
		restaurantOrderService.save(restaurantOrderr);
		restaurantService.save(restaurant);
	}

	@PostMapping(path = "/restaurant/acceptRestaurantOrder")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void acceptRestaurantOrder(@Valid @RequestBody RestaurantOrderr restaurantOrderr) {
		if (restaurantOrderr.getOrderActive().equals("open")) {
			restaurantOrderr.setOrderActive("closed");
			for (int i = 0; i < restaurantOrderr.getOffers().size(); i++) {
				if (restaurantOrderr.getOffers().get(i).getBidder().getId() == restaurantOrderr
						.getIdFromChoosenBidder()) {
					restaurantOrderr.getOffers().get(i).setAccepted("accepted");
				} else
					restaurantOrderr.getOffers().get(i).setAccepted("rejected");
				offerService.save(restaurantOrderr.getOffers().get(i));
			}
			Restaurant restaurant = findRestaurantForRestaurantManager();
			restaurantOrderService.save(restaurantOrderr);
			restaurantService.save(restaurant);
		} else {
			try {
				throw new Exception("Not legal offer.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void setObject(Dish dish, Drink drink, Restaurant restaurant, RestaurantOrderr restaurantOrderr) {
		restaurantOrderr.setOrderActive("0");
		if (restaurantOrderr.getStartDate().before(restaurantOrderr.getEndDate())) {
			if (dish != null) {
				for (int i = 0; i < restaurant.getFood().size(); i++)
					if (restaurant.getFood().get(i).getId() == dish.getId()) {
						restaurantOrderr.setDish(restaurant.getFood().get(i));
						break;
					}
			} else if (drink != null) {
				for (int i = 0; i < restaurant.getDrinks().size(); i++)
					if (restaurant.getDrinks().get(i).getId() == drink.getId()) {
						restaurantOrderr.setDrink(restaurant.getDrinks().get(i));
						break;
					}
			}
			restaurantOrderr.setOffers(new ArrayList<Offer>());
			restaurantOrderr.setOrderActive("open");
		} else
			throw new DateTimeException("Wrong date.");
	}

	@PutMapping(path = "/{id}")
	public RestaurantManager updateRestaurantManager(@PathVariable Long id,
			@Valid @RequestBody RestaurantManager restaurantManager) {
		Optional.ofNullable(restaurantManagerService.findOne(id))
				.orElseThrow(() -> new ResourceNotFoundException("resourceNotFound!"));
		restaurantManager.setId(id);
		return restaurantManagerService.save(restaurantManager);
	}

	@PutMapping(path = "/restaurant/makeConfig/{xaxis}/{yaxis}")
	public void makeConfig(@PathVariable("xaxis") Long xaxis, @PathVariable("yaxis") Long yaxis) {

		Restaurant restaurant = findRestaurantForRestaurantManager();
		// RESITI OVO KAKO TREBA
		restaurant.getSegments().get(0).getTables().clear();

		for (int x = 0; x < xaxis; x++) {
			for (int y = 0; y < yaxis; y++) {
				app.restaurant.Table t = new app.restaurant.Table("Table", x, y, app.restaurant.Table.NOT_EXISTS);
				restaurant.getSegments().get(0).getTables().add(t);
				tableService.save(t);
			}
		}
	}

	@GetMapping(path = "/restaurant/getTables")
	public List<app.restaurant.Table> getTables() {

		Restaurant restaurant = findRestaurantForRestaurantManager();
		// prvi put napravi default segment
		if (restaurant.getSegments().size() < 1) {
			Segment seg = new Segment("default");
			restaurant.getSegments().add(seg);
			segmentService.save(seg);
		}
		ArrayList<app.restaurant.Table> outTables = new ArrayList<app.restaurant.Table>();
		for (int i = 0; i < restaurant.getSegments().size(); i++) {
			outTables.addAll(restaurant.getSegments().get(i).getTables());
		}
		return outTables;
	}

	@PostMapping(path = "/restaurant/addSegment")
	public void addSegment(@RequestBody Segment segment) {
		Restaurant restaurant = findRestaurantForRestaurantManager();
		restaurant.getSegments().add(segment);
		segmentService.save(segment);
	}

	@GetMapping(path = "/restaurant/getSegments")
	public List<Segment> getSegments() {
		Restaurant rest = findRestaurantForRestaurantManager();
		return rest.getSegments();
	}

	@PutMapping(path = "/restaurant/table/{id}")
	public app.restaurant.Table updateTable(@PathVariable Long id, @RequestBody app.restaurant.Table table) {
		System.out.println(
				"idemo cuvati tabelu: " + table.getName() + " " + table.getSegmentName() + " " + table.getStatus());
		app.restaurant.Table pomtab = tableService.findOne(id);
		table.setXPos(pomtab.getXPos());
		table.setYPos(pomtab.getYPos());
		table.setId(id);

		return tableService.save(table);
	}

	// fali da se ubaci provera da se ne poklapaju kuvari
	@PostMapping(path = "/restaurant/changeShiftCookAction")
	public void changeShiftCookAction(@Valid @RequestBody ChangedShiftCook changedShiftCook) {
		// onemoguceno da sam sa sobom menja smenu
		if (changedShiftCook.getCook1().getId() != changedShiftCook.getCook2().getId()) {
			Restaurant restaurant = findRestaurantForRestaurantManager();
			changedShiftCookService.save(changedShiftCook);
			restaurant.getChangedShiftsForCooks().add(changedShiftCook);
			restaurantService.save(restaurant);
		} else
			throw new BadRequestException();
	}

	// fali da se ubaci provera da se ne poklapaju kuvari
	@PostMapping(path = "/restaurant/changeShiftBartenderAction")
	public void changeShiftBartenderAction(@Valid @RequestBody ChangedShiftBartender changedShiftBartender) {
		// onemoguceno da sam sa sobom menja smenu
		if (changedShiftBartender.getBartender1().getId() != changedShiftBartender.getBartender2().getId()) {
			Restaurant restaurant = findRestaurantForRestaurantManager();
			changedShiftBartenderService.save(changedShiftBartender);
			restaurant.getChangedShiftsForBartenders().add(changedShiftBartender);
			restaurantService.save(restaurant);
		} else
			throw new BadRequestException();
	}

	// fali da se ubaci provera da se ne poklapaju kuvari
	@PostMapping(path = "/restaurant/changeShiftWaiterAction")
	public void changeShiftWaiterAction(@RequestBody ChangedShiftWaiter changedShiftWaiter) {
		// onemoguceno da sam sa sobom menja smenu
		
		setObjects(changedShiftWaiter);
		if (changedShiftWaiter.getWaiter1().getId() != changedShiftWaiter.getWaiter2().getId()) {
			Restaurant restaurant = findRestaurantForRestaurantManager();
			changedShiftWaiterService.save(changedShiftWaiter);
			restaurant.getChangedShiftsForWaiters().add(changedShiftWaiter);
			restaurantService.save(restaurant);
		} else
			throw new BadRequestException();
	}

	private ChangedShiftWaiter setObjects(ChangedShiftWaiter changedShiftWaiter) {
		for(int i=0;i<waiterService.findAll().size();i++) {
			if(changedShiftWaiter.getWaiter1().getId() == waiterService.findAll().get(i).getId())
				changedShiftWaiter.setWaiter1(waiterService.findAll().get(i));
			if(changedShiftWaiter.getWaiter2().getId() == waiterService.findAll().get(i).getId())
				changedShiftWaiter.setWaiter2(waiterService.findAll().get(i));
		}
		return changedShiftWaiter;
	}
	@GetMapping(path = "/restaurant/getWaiterWithInputName/{waiterName}")
	@ResponseStatus(HttpStatus.OK)
	public Waiter getWaiterWithInputName(@PathVariable String waiterName) {
		Restaurant rest = findRestaurantForRestaurantManager();
		for (int i = 0; i < rest.getWaiters().size(); i++) {
			if (rest.getWaiters().get(i).getFirstname().equals(waiterName))
				return rest.getWaiters().get(i);
		}
		return null;
	}

	@GetMapping(path = "/restaurant/geDishWithInputName/{dishName}")
	@ResponseStatus(HttpStatus.OK)
	public double geDishWithInputName(@PathVariable String dishName) {
		Restaurant rest = findRestaurantForRestaurantManager();
		for (int i = 0; i < rest.getFood().size(); i++) {
			if (rest.getFood().get(i).getName().equals(dishName))
				return rest.getFood().get(i).getRate();
		}
		return -1;
	}

	@GetMapping(path = "/restaurant/updateRestaurant/{restaurantName}/{description}/{city}/{street}/{number}")
	@ResponseStatus(HttpStatus.OK)
	public void updateRestaurant(@PathVariable String restaurantName, @PathVariable String description,
			@PathVariable String city, @PathVariable String street, @PathVariable String number) {
		Restaurant rest = findRestaurantForRestaurantManager();
		rest.setName(restaurantName);
		rest.setDescription(description);
		rest.setCity(city);
		rest.setNumber(number);
		rest.setStreet(street);
		restaurantService.save(rest);
	}

	// izmena vrednosti aktivne ponude
	@PostMapping("/restaurant/tryToChangeDish/{id}/{name}/{count}")
	@ResponseStatus(HttpStatus.OK)
	public String tryToChangeDish(@PathVariable Long id, @PathVariable String name, @PathVariable Integer count) {
		Restaurant rest = findRestaurantForRestaurantManager();
		Dish dishh = null;
		for (int i = 0; i < rest.getFood().size(); i++) {
			if (rest.getFood().get(i).getId() == id) {
				dishh = rest.getFood().get(i);
				break;
			}
		}
		dishh.setCount(count);
		dishh.setName(name);
		for (int j = 0; j < rest.getFood().size(); j++) {
			if (rest.getFood().get(j).getId() == id) {
				dishService.save(dishh);
				rest.getFood().set(j, dishh);
				return "ok";
			}
		}
		return "no";
	}

	// izmena vrednosti aktivne ponude
	@PostMapping("/restaurant/tryToChangeDrink/{id}/{name}/{count}")
	@ResponseStatus(HttpStatus.OK)
	public String tryToChangeDrink(@PathVariable Long id, @PathVariable String name, @PathVariable Integer count) {
		Restaurant rest = findRestaurantForRestaurantManager();
		Drink drinkk = null;
		for (int i = 0; i < rest.getDrinks().size(); i++) {
			if (rest.getDrinks().get(i).getId() == id) {
				drinkk = rest.getDrinks().get(i);
				break;
			}
		}
		drinkk.setCount(count);
		drinkk.setName(name);
		for (int j = 0; j < rest.getDrinks().size(); j++) {
			if (rest.getDrinks().get(j).getId() == id) {
				drinkService.save(drinkk);
				rest.getDrinks().set(j, drinkk);
				return "ok";
			}
		}
		return "no";
	}
}