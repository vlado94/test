package app.guest;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.OptimisticLockException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
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

import app.bill.Bill;
import app.bill.BillService;
import app.dish.Dish;
import app.dish.DishService;
import app.drink.Drink;
import app.drink.DrinkService;
import app.employed.waiter.Waiter;
import app.employed.waiter.WaiterService;
import app.friends.FriendsService;
import app.manager.changedShiftWaiter.ChangedShiftWaiter;
import app.manager.changedShiftWaiter.ChangedShiftWaiterService;
import app.order.OrderService;
import app.order.Orderr;
import app.order.RateOrder;
import app.order.RateOrderService;
import app.order.RateService;
import app.order.RateServiceService;
import app.reservation.Reservation;
import app.reservation.ReservationService;
import app.restaurant.RateRestaurant;
import app.restaurant.RateRestaurantService;
import app.restaurant.Restaurant;
import app.restaurant.RestaurantService;
import app.restaurant.Segment;
import app.restaurant.SegmentService;
import app.restaurant.Table;
import app.restaurant.TableService;
import net.minidev.asm.ex.ConvertException;

@RestController
@RequestMapping("/guest")
public class GuestController {

	private final GuestService guestService;
	private final RestaurantService restaurantService;
	private final WaiterService waiterService;

	private final DishService dishService;
	private final DrinkService drinkService;
	private final OrderService orderService;	
	private final TableService tableService;
	private final ReservationService reservationService;
	private final RateRestaurantService rateRestaurantService;
	private final RateOrderService rateOrderService;
	private final RateServiceService rateServiceService;
	private final BillService billService;
	private final FriendsService friendsService;
	private final ChangedShiftWaiterService changedShiftWaiterService;
	private HttpSession httpSession;
	private JavaMailSender javaMailSender;
	

	@Autowired
	public GuestController(final HttpSession httpSession, final GuestService service, final RestaurantService restaurantService,
			DishService dishService,final DrinkService drinkService,
			final OrderService orderService, final TableService tableService, final ReservationService reservationService,
			final WaiterService waiterService,final SegmentService segmentService,
			final RateRestaurantService rateRestaurantService, final RateOrderService rateOrderService,
			final RateServiceService rateServiceService, final BillService billService,
			final ChangedShiftWaiterService changedShiftWaiterService,
			final JavaMailSender javaMailSender,final FriendsService friendsService) {
		this.guestService = service;
		this.httpSession = httpSession;
		this.restaurantService = restaurantService;
		this.dishService = dishService;
		this.drinkService = drinkService;
		this.orderService = orderService;
		this.tableService = tableService;
		this.reservationService = reservationService;
		this.waiterService = waiterService;
		this.rateRestaurantService = rateRestaurantService;
		this.rateOrderService = rateOrderService;
		this.rateServiceService = rateServiceService;
		this.billService = billService;
		this.changedShiftWaiterService = changedShiftWaiterService;
		this.javaMailSender = javaMailSender;
		this.friendsService = friendsService;
	}

	@SuppressWarnings("unused")
	@GetMapping("/checkRights")
	public boolean checkRights() {
		try {
			Guest guest = ((Guest) httpSession.getAttribute("user"));
			if(!((httpSession.getAttribute("user")) instanceof Guest) )
			{
				return false;
			}
				
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// izlistavanje svih gostiju
	@GetMapping
	public ResponseEntity<List<Guest>> findAll() {
		return new ResponseEntity<>(guestService.findAll(), HttpStatus.OK);
	}
	
	@GetMapping(path = "/restaurants")
	public ResponseEntity<List<Restaurant>> findAllRestaurants() {
		return new ResponseEntity<>(restaurantService.findAll(), HttpStatus.OK);
	}
	
	@GetMapping(path = "/restaurant/{id}")
	public Restaurant findRestaurant(@PathVariable Long id){
		Optional.ofNullable(restaurantService.findOne(id))
					.orElseThrow(() -> new ResourceNotFoundException("Resource Not Found!"));
		Restaurant r = restaurantService.findOne(id);
		return r;
		
	}

	// 2.2
	// izmena informacija o gostu
	@PutMapping(path = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Guest update(@PathVariable Long id, @Valid @RequestBody Guest guest) {
		Optional.ofNullable(guestService.findOne(id))
				.orElseThrow(() -> new ResourceNotFoundException("Resource Not Found!"));
		guest.setId(id);
		return guestService.save(guest);
	}

	// kada se klikne na link iz maila
	@PutMapping(path = "/activate/{acNum}")
	@ResponseStatus(HttpStatus.OK)
	public void activateGuest(@PathVariable String acNum) {
		guestService.activate(acNum);
	}

	@GetMapping(path = "/findByFirstAndLastName/{inputStr}")
	public List<Guest> findByFirstAndLastName(@PathVariable String inputStr) {
		List<Guest> result = guestService.findByFirstAndLastName(inputStr);
		//izbacivanje njega samog iz liste
		result.remove((Guest) httpSession.getAttribute("user"));
		// treba izbaciti i kad postoji vec prijateljstvo...
		
		return result;
	}
	
	@GetMapping(path = "/findRestaurant/{inputStr}")
	public List<Restaurant> findRestaurant(@PathVariable String inputStr) {
		List<Restaurant> result = restaurantService.findByNameAndType(inputStr);
		return result;
	}
	

	@GetMapping(path = "/order")
	public Orderr guestOrder(){
		Orderr order = new Orderr();
		return order;
	}
	
	@PutMapping(path = "/addDish/{id}")
	public Orderr guestAddDish(@PathVariable Long id,  @RequestBody Orderr order){
		Dish dish = dishService.findOne(id);
		order.getFood().add(dish);
		return order;
	}
	
	@PutMapping(path = "/removeDish/{id}")
	public Orderr removeDish(@PathVariable Long id, @RequestBody Orderr order){
		//da li treba zastita ovde sa ifom za size hrane?
		for(int i = 0 ; i < order.getFood().size(); i++){
			if(order.getFood().get(i).getId() == id){
				order.getFood().remove(i);
				break;
			}
		}
		
		return order;
	}
	
	@PutMapping(path = "/removeDrink/{id}")
	public Orderr removeDrink(@PathVariable Long id, @RequestBody Orderr order){
		//da li treba zastita ovde sa ifom za size hrane?
		for(int i = 0 ; i < order.getDrinks().size(); i++){
			if(order.getDrinks().get(i).getId() == id){
				order.getDrinks().remove(i);
				break;
			}
		}
		
		return order;
	}
	
	@PutMapping(path = "/addDrink/{id}")
	public Orderr guestAddDrink(@PathVariable Long id, @RequestBody Orderr order){
		Drink drink = drinkService.findOne(id);
		order.getDrinks().add(drink);
		return order;
	}
	
	@PostMapping(path = "/makeOrder/{id}/{resId}")
	@ResponseStatus(HttpStatus.CREATED)
	public void makeOrder(@PathVariable Long id, @PathVariable Long resId, @Valid @RequestBody Orderr order){
		if(order.getDrinks().size() > 0 || order.getFood().size() > 0){
			Table table = tableService.findOne(id);
			Reservation reservation = reservationService.findOne(resId);
			table.getReservations();
			order.setId(null);
			order.setTable(tableService.findOne(id));
			order.setTotal(order.getTotal());
			order = orderService.save(order);
			Restaurant restaurant = restaurantService.findOne(reservation.getRestaurant().getId());
			
			restaurant.getOrder().add(order);
			List<Waiter> waiters = restaurant.getWaiters();
			List<Waiter> waiter = new ArrayList<Waiter>();
			List<ChangedShiftWaiter> changedShifts = restaurant.getChangedShiftsForWaiters();
			
			
			String shift = "";
			if(reservation.getHours() > 8 && reservation.getHours() < 16){
				shift = "First";
			} else if (reservation.getHours() >= 16 && reservation.getHours() < 22){
				shift = "Second";
			}
			
			int endTime = reservation.getHours() + reservation.getDuration();
			
			if(endTime > 16 && shift == "Second"){
				for(int i = 0 ; i < waiters.size(); i++){
					for(int j = 0 ; j < waiters.get(i).getTablesForHandling().size(); j++){
						if(waiters.get(i).getTablesForHandling().get(j).getId() == table.getId() &&
								waiters.get(i).getDefaultShift().toString().equals(shift)){
							waiter.add(waiterService.findOne(waiters.get(i).getId()));
						}
					}
				}
			} else if (endTime > 16 && shift == "First"){
				for(int i = 0 ; i < waiters.size(); i++){
					for(int j = 0 ; j < waiters.get(i).getTablesForHandling().size(); j++){
						if(waiters.get(i).getTablesForHandling().get(j).getId() == table.getId()){
							waiter.add(waiterService.findOne(waiters.get(i).getId()));
						}
					}
				}
			} else {
				for(int i = 0 ; i < waiters.size(); i++){
					for(int j = 0 ; j < waiters.get(i).getTablesForHandling().size(); j++){
						if(waiters.get(i).getTablesForHandling().get(j).getId() == table.getId() &&
								waiters.get(i).getDefaultShift().toString().equals(shift)){
							waiter.add(waiterService.findOne(waiters.get(i).getId()));
						}
					}
				}
			}
			
			
			for(int j = 0 ; j < waiter.size(); j++){
				for(int i = 0 ; i < changedShifts.size(); i++){
					if((changedShifts.get(i).getWaiter1().getId() == waiter.get(j).getId() &&
							changedShifts.get(i).getDate().compareTo(reservation.getDate()) == 0)){
						waiter.set(j, changedShifts.get(i).getWaiter2());
					} else if ((changedShifts.get(i).getWaiter2().getId() == waiter.get(j).getId() &&
							changedShifts.get(i).getDate().compareTo(reservation.getDate()) == 0)){
						waiter.set(j, changedShifts.get(i).getWaiter1());
					}
				}
			}
			for(int i = 0 ; i < waiter.size(); i++){
				waiter.get(i).getOrders().add(order);
				Waiter w = waiterService.findOne(waiter.get(i).getId());
				waiterService.save(w);
			}
			
			reservation.getOrders().add(order);
			reservationService.save(reservation);
			restaurantService.save(restaurant);
			
			
			
		}
	}
	
	@GetMapping(path="/restaurant/getTables/{id}")
	public List<app.restaurant.Table> getTables(@PathVariable Long id){
		Restaurant restaurant = restaurantService.findOne(id);
		ArrayList<app.restaurant.Table> outTables = new ArrayList<app.restaurant.Table>();
		for(int i=0; i<restaurant.getSegments().size(); i++){
			outTables.addAll(restaurant.getSegments().get(i).getTables());
		}
		return outTables;
	}
	
	
	@PostMapping(path="/makeReservation")
	public Reservation makeReservation(@RequestBody Reservation reservation){
		List<Long> tables = reservation.getTables();
		
		// provera u bazi da li moze za taj termin
		for(int i=0; i<tables.size(); i++){
			Table table = tableService.findOne(tables.get(i));
			for(int j=0; j<table.getReservations().size(); j++){
				Reservation res = table.getReservations().get(j);
				if(res.getDate().toString().equals(reservation.getDate().toString()) ){
					if(((res.getHours() + res.getMinutes()/60+res.getDuration()) >= (reservation.getHours()+reservation.getMinutes()/60)) &&
						((reservation.getHours()+reservation.getMinutes()/60+reservation.getDuration())>=(res.getHours() + res.getMinutes()/60) )){
						System.out.println("Kolizija");
						throw new OptimisticLockException("Kolizija");
					}
				}
			}	
		}
		
		
		if(reservation.getHours()>22 || reservation.getMinutes()>60 || reservation.getHours()<0 || reservation.getMinutes()<0 || tables.size()<1 ){
			throw new NullPointerException();
		}
		
		for(int i=0; i<tables.size(); i++){
			tableService.findOne(tables.get(i)).getReservations().add(reservation);
		}
		System.out.println("tables size:::: "+tables.size());
		
		Long guestId = ((Guest) httpSession.getAttribute("user")).getId();
		Guest guest = guestService.findOne(guestId);
		
		
		reservation.getGuests().add(guest);
		
		List<Restaurant> restaurants = restaurantService.findAll();
		Restaurant restaurant = new Restaurant();
		label :
		for(int i = 0 ; i < restaurants.size(); i++){
			for(int j = 0 ; j < restaurants.get(i).getSegments().size(); j++){
				if(restaurants.get(i).getSegments().get(j).getName().equals(tableService.findOne(tables.get(0)).getSegmentName())){
					restaurant = restaurants.get(i);
					break label;
				}
			}
		}
		for(int i=0; i<reservation.getInvitedGuests().size(); i++){
		// ----Salje mejl sam sebi, zbog testiranja...
				try {
					SimpleMailMessage mail = new SimpleMailMessage();
					mail.setTo("isarestorani2@gmail.com");// umesto ovoga guest.mail..ako neces da testiras
					mail.setFrom("isarestorani2@gmail.com");
					mail.setSubject("Activation link");
					mail.setText("http://localhost:8080/#/loggedIn/guest/home");

					javaMailSender.send(mail);
				} catch (Exception m) {
					m.printStackTrace();
				}
		}
		reservation.setRestaurant(restaurant);
		return reservationService.save(reservation);
	}
	
	
	@SuppressWarnings("unused")
	@PostMapping(path="/checkTables")
	public boolean checkTables(@RequestBody ArrayList<Table> tables){
		
		for(int i=0; i<tables.size(); i++){
			Table t = tableService.findOne(tables.get(i).getId());
			if(t.getVersion() == tables.get(i).getVersion()){}
			else
				throw new OptimisticLockException();
		}
		return true;
	}
	
	
	
	
	
	
	
	
	@GetMapping(path="/reservations")
	@ResponseStatus(HttpStatus.OK)
	public List<Reservation> getReservations(){
		return reservationService.findAll();		
	}
	
	@GetMapping(path="/currentReservations")
	@ResponseStatus(HttpStatus.OK)
	public List<Reservation> getcurrentReservations(){
		Long guestId = ((Guest) httpSession.getAttribute("user")).getId();
		List<Reservation> reservations = reservationService.findAll();
		List<Reservation> out = new ArrayList<Reservation>();
		for(int i=0; i<reservations.size(); i++){
			List<Guest> guests = reservations.get(i).getGuests();
			for(int j=0; j<guests.size(); j++){
				if(guests.get(j).getId()== guestId ){
					Date today = new Date(Calendar.getInstance().getTime().getTime());
					Date resDate = reservations.get(i).getDate();
					if(today.before(resDate))
						out.add(reservations.get(i));
				}
			}
			
		}
		
		return out;		
	}
	
	@GetMapping(path="/cancelReservation/{id}")
	public void CancelReservation(@PathVariable Long id){
		Reservation res = reservationService.findOne(id);
		res.getGuests().clear();
		reservationService.save(res);
	}
	
	
	public List<Reservation> getReservationsRestaurant(){
		Long guestId = ((Guest) httpSession.getAttribute("user")).getId();
		Guest guest = guestService.findOne(guestId);
		
		List<Reservation> allReservations = reservationService.findAll();
		List<Reservation> resertavationsTemp = new ArrayList<Reservation>();
		for(int i = 0 ; i < allReservations.size(); i++){
			for(int j = 0 ; j < allReservations.get(i).getGuests().size(); j++){
				if(allReservations.get(i).getGuests().get(j).getId() == guest.getId()){
					resertavationsTemp.add(allReservations.get(i));
				}
			}
		}
		
		List<Bill> bills = billService.findAll();
		List<Reservation> guestReservations = new ArrayList<Reservation>();
		
		for(int i = 0 ; i < resertavationsTemp.size(); i++){
			for(int j = 0 ; j < bills.size(); j++){
				if(bills.get(j).getReservation().getId() == resertavationsTemp.get(i).getId()){
					guestReservations.add(resertavationsTemp.get(i));
				}
			}
		}
		
		return guestReservations;
		
	}
	@GetMapping(path="/visitedRestaurants")
	@ResponseStatus(HttpStatus.OK)
	public Set<Restaurant> getVisitedRestaurants(){
		List<Reservation> reservations = getReservationsRestaurant();
		Set<Restaurant> restaurants = new HashSet<Restaurant>();
		//List<Restaurant> rest = new ArrayList<Restaurant>();
		//rest.addAll(restaurants);
		for(int i = 0 ; i < reservations.size(); i++){
			//restaurants.add(reservations.get(i).getRestaurant());
			restaurants.add(reservations.get(i).getRestaurant());
		}
		
		return restaurants;
		
	}
	
	@GetMapping(path = "/restaurantOrders/{id}")
	public List<Orderr> getRestaurantOrders(@PathVariable Long id){
		
		List<Reservation> reservations = getReservationsRestaurant();
		
		Restaurant restaurant = restaurantService.findOne(id);
		
		
		List<Segment> segments = restaurant.getSegments();
		List<Table> tables = new ArrayList<Table>();
		for(int i = 0 ; i < segments.size(); i++){
			tables.addAll(segments.get(i).getTables());
		}
		
		//List<Orderr> orders = orderService.findAll();
		List<Table> tableReservations = new ArrayList<Table>();
		for(int i = 0 ; i < tables.size(); i++){
			if(tables.get(i).getReservations() != null)
				for(int j = 0 ; j < tables.get(i).getReservations().size(); j++){
					tableReservations.add(tables.get(i));
				}
		}
		Set<Table> guestTables = new HashSet<Table>();
		
		for(int j = 0 ; j < tableReservations.size(); j++){
			for(int k = 0 ; k < tableReservations.get(j).getReservations().size(); k++){
				for(int i = 0 ; i < reservations.size(); i++){
					if(tableReservations.get(j).getReservations().get(k).getId() == reservations.get(i).getId()){
						guestTables.add(tableReservations.get(j));
					}
				}
			}
		}
		List<Table> tableTemp = new ArrayList<Table>(guestTables);
		List<Reservation> resTemp = new ArrayList<Reservation>();
		
		for(int i = 0 ; i < tableTemp.size(); i++){
			resTemp.addAll(tableTemp.get(i).getReservations());
		}
		Set<Orderr> orderTemp = new HashSet<Orderr>();
		for(int i = 0 ; i < reservations.size();i++){
			for(int j = 0 ; j < resTemp.size();j++){
				if(reservations.get(i).getOrders() == resTemp.get(j).getOrders()){
					orderTemp.addAll(reservations.get(i).getOrders());
				}
			}
		}
		
		List<Orderr> guestOrders = new ArrayList<Orderr>(orderTemp);
		return guestOrders;
			
	}
	
	@GetMapping(path = "/avgRateFriends/{restaurantId}")
	public double avgRateFriend(@PathVariable Long restaurantId){
		Restaurant restaurant = restaurantService.findOne(restaurantId);
		Long id = ((Guest) httpSession.getAttribute("user")).getId();
		
		Guest guest = guestService.findOne(id);
		List<Guest> guests = new ArrayList<Guest>();
		guests.add(guest);
		for(int i = 0 ; i < friendsService.findAll().size(); i++){
			if(friendsService.findAll().get(i).getFriendReciveRequest().getId() == guest.getId() && 
					friendsService.findAll().get(i).getStatus().equals("accepted")){
				guests.add(friendsService.findAll().get(i).getFriendSendRequest());
			} else if (friendsService.findAll().get(i).getFriendSendRequest().getId() == guest.getId() && 
					friendsService.findAll().get(i).getStatus().equals("accepted")){
				guests.add(friendsService.findAll().get(i).getFriendReciveRequest());
			}
		}
		List<Integer> marks = new ArrayList<Integer>();
		
		for(int i = 0 ; i < restaurant.getRateRestaurant().size(); i++){
			for(int j = 0 ; j < guests.size(); j++){
				if(restaurant.getRateRestaurant().get(i).getGuest().getId() == guests.get(j).getId()){
					marks.add(restaurant.getRateRestaurant().get(i).getRate());
				}
			}
		}
		
		double sum = 0;
		double average = 0;
		for(int i = 0 ; i < marks.size(); i++){
			sum += marks.get(i);
			
		}
		average = sum/marks.size();
		
		return average;
		
	}
	
	@PutMapping(path = "/rateRestaurant/{rate}/{id}")
	public Restaurant rateRestaurant(@PathVariable int rate, @PathVariable Long id){
		Restaurant restaurant = restaurantService.findOne(id);
		//restaurant.setNumRate(rate);
		Long guestId = ((Guest) httpSession.getAttribute("user")).getId();
		Guest guest = guestService.findOne(guestId);
		RateRestaurant r = new RateRestaurant();
		r.setGuest(guest);
		r.setRate(rate);
		for(int i = 0 ; i < restaurant.getRateRestaurant().size(); i++){
			if(restaurant.getRateRestaurant().get(i).getGuest() == null){
				continue;
			}
			if(restaurant.getRateRestaurant().get(i).getGuest().getId() == guestId){
				return null;
			}
		}
		
		rateRestaurantService.save(r);
		
		restaurant.getRateRestaurant().add(r);
		
		restaurantService.save(restaurant);
		return restaurant;
	}
	
	@PutMapping(path = "/rateOrder/{rate}/{id}")
	public Orderr rateOrder(@PathVariable int rate, @PathVariable Long id){
		Orderr order = orderService.findOne(id);

		Long guestId = ((Guest) httpSession.getAttribute("user")).getId();
		Guest guest = guestService.findOne(guestId);
		
		RateOrder rateOrder = new RateOrder();
		rateOrder.setGuest(guest);
		rateOrder.setRate(rate);
		for(int i = 0 ; i < order.getRateOrders().size(); i++){
			if(order.getRateOrders().get(i).getGuest().getId() == guestId){
				return null;
			}
		}
		
			
		Set<Dish> setFood = new HashSet<Dish>(order.getFood());
		ArrayList<Dish> food = new ArrayList<Dish>(setFood);
		for(int i = 0 ; i < food.size(); i++){
			Dish dish = dishService.findOne(food.get(i).getId());
			dish.getNumRate().add(rate);
			
			double average = dish.getRate();
			average = Math.round(average*100.0)/100.0;
			dish.setRate(average);
			dishService.save(dish);
		}
		
		rateOrderService.save(rateOrder);
		order.getRateOrders().add(rateOrder);
		orderService.save(order);
		return order;
	}
	
	@PostMapping(path = "/acceptInvite/{id}")
	public Restaurant acceptInvite(@PathVariable Long id){
		Reservation r = reservationService.findOne(id);
		Long idR = r.getRestaurant().getId();
		Restaurant out = restaurantService.findOne(idR);
		
		Long guestId = ((Guest) httpSession.getAttribute("user")).getId();
		Guest guest = guestService.findOne(guestId);
		r.getInvitedGuests().remove(guestId);
		r.getGuests().add(guest);
		reservationService.save(r);
		return out;
	}
	
	@PostMapping(path = "/rejectInvite/{id}")
	public void rejectInvite(@PathVariable Long id){
		Reservation r = reservationService.findOne(id);
		Long guestId = ((Guest) httpSession.getAttribute("user")).getId();
		Guest guest = guestService.findOne(guestId);
		r.getInvitedGuests().remove(guestId);
		reservationService.save(r);
	}
	
	@PutMapping(path = "/rateService/{rate}/{id}")
	public Orderr rateService(@PathVariable int rate, @PathVariable Long id){
		Orderr order = orderService.findOne(id);

		Long guestId = ((Guest) httpSession.getAttribute("user")).getId();
		Guest guest = guestService.findOne(guestId);
		
		RateService rateService = new RateService();
		rateService.setGuest(guest);
		rateService.setRate(rate);
		
		for(int i = 0 ; i < order.getRateServices().size(); i++){
			if(order.getRateServices().get(i).getGuest().getId() == guest.getId()){
				return null;
			}
		}
		
		List<Waiter> waiters = waiterService.findAll();
		Waiter waiter = new Waiter();
		for(int i = 0 ; i < waiters.size(); i++){
			for(int j = 0 ; j < waiters.get(i).getOrders().size(); j++){
				if(waiters.get(i).getOrders().get(j).getId() == order.getId()){
					waiter = waiters.get(i);
				}
			}
		}
		
		waiter.getNumRate().add(rate);
		double average = waiter.getRate();
		average = (Math.round(average*100.0)/100.0);
		waiter.setRate(average);
		waiterService.save(waiter);
		
		rateServiceService.save(rateService);
		order.getRateServices().add(rateService);
		orderService.save(order);
		
		
		return order;
	}
	
	
	
	
	
}