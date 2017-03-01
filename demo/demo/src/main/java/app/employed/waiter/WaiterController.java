package app.employed.waiter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

import app.bill.Bill;
import app.bill.BillService;
import app.dish.Dish;
import app.dish.DishService;
import app.drink.Drink;
import app.drink.DrinkService;
import app.employed.DefaultShift;
import app.manager.changedShiftWaiter.ChangedShiftWaiter;
import app.manager.changedShiftWaiter.ChangedShiftWaiterService;
import app.order.ChangeStatus;
import app.order.DrinkStatus;
import app.order.FoodStatus;
import app.order.OrderService;
import app.order.Orderr;
import app.reservation.Reservation;
import app.reservation.ReservationService;
import app.restaurant.Restaurant;
import app.restaurant.RestaurantService;
import app.restaurant.Table;
import app.restaurant.TableService;

@RestController
@RequestMapping("/waiter")
public class WaiterController {

	private HttpSession httpSession;

	private final WaiterService waiterService;
	private final OrderService orderService;

	private final TableService tableService;
	private final ReservationService reservationService;
	private final BillService billService;
	private final ChangedShiftWaiterService changedShiftService;
	private final RestaurantService restaurantService;
	private final DrinkService drinkService;
	private final DishService dishService;

	@Autowired
	public WaiterController(final HttpSession httpSession, final WaiterService service, final OrderService orderService,
			final DrinkService drinkService,final TableService tableService, final ReservationService reservationService, 
			final BillService billService, final ChangedShiftWaiterService changedShiftService, 
			final RestaurantService restaurantService, final DishService dishService) {
		this.httpSession = httpSession;
		this.waiterService = service;
		this.orderService = orderService;
		this.tableService = tableService;
		this.reservationService = reservationService;
		this.billService = billService;
		this.changedShiftService = changedShiftService;
		this.restaurantService = restaurantService;
		this.drinkService = drinkService;
		this.dishService = dishService;
	}

	@SuppressWarnings("unused")
	@GetMapping("/checkRights")
	public boolean checkRights() {
		try {
			Waiter waiter = ((Waiter) httpSession.getAttribute("user"));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public Waiter findWaiter() {
		Long id = ((Waiter) httpSession.getAttribute("user")).getId();
		Waiter waiter = waiterService.findOne(id);
		return waiter;
	}



	@GetMapping(path = "/orders")
	public ResponseEntity<List<Orderr>> orders() {
		Long id = ((Waiter) httpSession.getAttribute("user")).getId();
		Waiter waiter = waiterService.findOne(id);
		List<Reservation> reservationsTemp = new ArrayList<Reservation>();
		Date date = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		List<ChangedShiftWaiter> changedShifts = changedShiftService.findAll();
		for (int i = 0; i < changedShifts.size(); i++) {
			if ((changedShifts.get(i).getWaiter1().getId() == waiter.getId()
					&& changedShifts.get(i).getDate().toString().equals(ft.format(date)))) {
				waiter.setTablesForHandling(changedShifts.get(i).getWaiter2().getTablesForHandling());
			} else if ((changedShifts.get(i).getWaiter2().getId() == waiter.getId()
					&& changedShifts.get(i).getDate().toString().equals(ft.format(date)))) {
				waiter.setTablesForHandling(changedShifts.get(i).getWaiter1().getTablesForHandling());
			}
		}
		// preuzeti sve rezervacije konobara
		for (int i = 0; i < waiter.getTablesForHandling().size(); i++) {
			for (int j = 0; j < waiter.getTablesForHandling().get(i).getReservations().size(); j++) {
				reservationsTemp.add(waiter.getTablesForHandling().get(i).getReservations().get(j));
			}
		}

		List<Reservation> reservations = new ArrayList<Reservation>();
		// rezervacije koje su za danasnji datum
		for (int i = 0; i < reservationsTemp.size(); i++) {
			if (reservationsTemp.get(i).getDate().toString().equals(ft.format(date))) {
				reservations.add(reservationsTemp.get(i));
			}
		}
		
		List<Reservation> returnReservation = getActiveReservations(reservations, waiter);

		List<Orderr> returnOrders = new ArrayList<Orderr>();
		for (int i = 0; i < returnReservation.size(); i++) {
			for (int j = 0; j < returnReservation.get(i).getOrders().size(); j++) {
				if (returnReservation.get(i).getOrders().get(j).getDrinkStatus() == null
						&& returnReservation.get(i).getOrders().get(j).getFoodStatus() == null) {
					returnOrders.add(returnReservation.get(i).getOrders().get(j));
				}
			}
		}

		return new ResponseEntity<>(returnOrders, HttpStatus.OK);
	}
	

	@GetMapping(path = "/readyOrders")
	public ResponseEntity<List<Orderr>> readyOrder() {
		Long id = ((Waiter) httpSession.getAttribute("user")).getId();
		Waiter waiter = waiterService.findOne(id);
		List<Orderr> orders = waiter.getOrders();

		Optional.ofNullable(orders).orElseThrow(() -> new ResourceNotFoundException("Resource Not Found!"));

		List<Orderr> finishedOrders = new ArrayList<Orderr>();
		for (int i = 0; i < orders.size(); i++) {
			if (orders.get(i).getDrinkStatus() != null
					&& orders.get(i).getDrinkStatus().compareTo(DrinkStatus.finished) == 0
					&& orders.get(i).getFoodStatus() != null
					&& orders.get(i).getFoodStatus().compareTo(FoodStatus.finished) == 0) {

				finishedOrders.add(orders.get(i));

			}
		}

		return new ResponseEntity<>(finishedOrders, HttpStatus.OK);
	}
	
	@GetMapping(path = "/employedWaiters")
	public List<Waiter> employedWaiters(){
		Long id = ((Waiter) httpSession.getAttribute("user")).getId();
		Waiter waiter = waiterService.findOne(id);
		Restaurant restaurant = new Restaurant();
		for(int i = 0 ; i < restaurantService.findAll().size(); i++){
			for(int j = 0 ; j < restaurantService.findAll().get(i).getWaiters().size(); j++){
				if(restaurantService.findAll().get(i).getWaiters().get(j).getId() == waiter.getId()){
					restaurant = restaurantService.findAll().get(i);
				}
			}
		}
		
		return restaurant.getWaiters();
	}

	@PutMapping(path = "/sendToEmployed/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Orderr sendToEmployed(@PathVariable Long id) {

		Optional.ofNullable(orderService.findOne(id))
				.orElseThrow(() -> new ResourceNotFoundException("Resource Not Found!"));

		Orderr order = orderService.findOne(id);

		if (order.getDrinks().size() > 0 && order.getDrinkStatus() == null) {
			order.setDrinkStatus(DrinkStatus.inPrepared);
		} else if (order.getDrinks().size() == 0) {
			order.setDrinkStatus(DrinkStatus.finished);
		}

		if (order.getFood().size() > 0 && order.getFoodStatus() == null) {
			order.setFoodStatus(FoodStatus.inPrepared);
		} else if (order.getFood().size() == 0) {
			order.setFoodStatus(FoodStatus.finished);
		}

		return orderService.save(order);

	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void save(@Valid @RequestBody Waiter waiter) {
		waiter.setId(null);
		waiter.setRegistrated("0");
		waiterService.save(waiter);
	}

	@GetMapping(path = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Waiter findOne(@PathVariable Long id) {
		Optional.ofNullable(waiterService.findOne(id))
				.orElseThrow(() -> new ResourceNotFoundException("resourceNotFound!"));

		Waiter waiter = waiterService.findOne(id);
		return waiter;
	}

	@PutMapping(path = "/profile/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Waiter update(@PathVariable Long id, @Valid @RequestBody Waiter waiter) {
		Optional.ofNullable(waiterService.findOne(id))
				.orElseThrow(() -> new ResourceNotFoundException("Resource Not Found!"));
		waiter.setId(id);
		return waiterService.save(waiter);
	}
	
	@PutMapping(path = "/changePassword/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Waiter changePassword(@PathVariable Long id, @Valid @RequestBody Waiter waiter) {
		Optional.ofNullable(waiterService.findOne(id))
				.orElseThrow(() -> new ResourceNotFoundException("Resource Not Found!"));
		waiter.setId(id);
		return waiterService.save(waiter);
	}

	@PostMapping(path = "/makeBill")
	@ResponseStatus(HttpStatus.CREATED)
	public void makeBill(@Valid @RequestBody Orderr order) {
		Long id = ((Waiter) httpSession.getAttribute("user")).getId();
		
		Waiter waiterTemp = waiterService.findOne(id);
		Table table = tableService.findOne(order.getTable().getId());
		Orderr saveOrder = orderService.findOne(order.getId());
		List<Reservation> reservations = table.getReservations();
		Reservation reservation = new Reservation();
		for (int i = 0; i < reservations.size(); i++) {
			for (int j = 0; j < reservations.get(i).getOrders().size(); j++) {
				if (reservations.get(i).getOrders().get(j).getId() == order.getId()) {
					reservation = reservations.get(i);
					saveOrder.setDrinkStatus(DrinkStatus.madeBill);
					saveOrder.setFoodStatus(FoodStatus.madeBill);
					orderService.save(saveOrder);
					break;
				}
			}
		}
		Waiter waiter = billForWaiter(reservation, saveOrder, waiterTemp, table);
		Bill bill = new Bill();
		bill.setDate(reservation.getDate());
		bill.setTotal(order.getTotal());
		bill.setId(null);
		bill.setReservation(reservation);
		billService.save(bill);
		waiter.getBills().add(bill);
		waiterService.save(waiter);

	}
	
	public Waiter billForWaiter(Reservation reservation, Orderr order, Waiter waiter, Table table){
		
		for(int i = 0 ; i < reservation.getOrders().size(); i++){
			if(reservation.getOrders().get(i).getId() == order.getId()){
				Date startReservation = new Date();
				Date endReservation = new Date();
				SimpleDateFormat date24Format = new SimpleDateFormat("HH:mm");
				String time = "", timeEnd = "";
				time += "" + reservation.getHours() + ":" + reservation.getMinutes();
				timeEnd += "" + (reservation.getHours() + reservation.getDuration()) + ":"
						+ reservation.getMinutes();
				
				Date shiftTime = new Date();
				try {
					startReservation = date24Format.parse(time);
					endReservation = date24Format.parse(timeEnd);
					shiftTime = date24Format.parse("16:00");
					long differenceShiftOne = shiftTime.getTime() - startReservation.getTime();
					long differenceShiftTwo = endReservation.getTime() - shiftTime.getTime();
					

					if(differenceShiftTwo > differenceShiftOne){
						if(waiter.getDefaultShift().compareTo(DefaultShift.Second) == 0){
							return waiter;
						} else {
							return secondWaiter(table, waiter);							
						}
					} else {
						if(waiter.getDefaultShift().compareTo(DefaultShift.First) == 0){
							return waiter;
						} else {
							return secondWaiter(table, waiter);
						}
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		}
		return waiter;
		
	}
	
	public Waiter secondWaiter(Table table, Waiter waiter){
		for(int j = 0 ; j < waiterService.findAll().size(); j++){
			for(int k = 0 ; k < waiterService.findAll().get(j).getTablesForHandling().size(); k++){
				if(waiterService.findAll().get(j).getTablesForHandling().get(k).getId() == table.getId() && 
						waiterService.findAll().get(j).getId() != waiter.getId()){
					return waiterService.findOne(waiterService.findAll().get(j).getId());
				}
			}
		}
		return waiter;
	}
	
	@GetMapping("/changeOrder/{orderId}/{versionId}")
	public Orderr changeOrder(@PathVariable Long orderId, @PathVariable Integer versionId){
		Orderr order = orderService.findOne(orderId);
		if(order.getVersion() == versionId){
			order.setChangeStatus(ChangeStatus.change);
			//order.setChangeVersion(order.getChangeVersion()+1);
			orderService.save(order);
			return order;
		} else {
			return null;
		}
	}
	
	@GetMapping("/getRestaurant")
	public Restaurant getRestaurant(){
		Long id = ((Waiter) httpSession.getAttribute("user")).getId();
		Waiter waiter = waiterService.findOne(id);
		
		for(int i = 0 ; i < restaurantService.findAll().size(); i++){
			for(int j = 0 ; j < restaurantService.findAll().get(i).getWaiters().size(); j++){
				if(restaurantService.findAll().get(i).getWaiters().get(j).getId() == waiter.getId()){
					return restaurantService.findAll().get(i);
				}
			}
		}
		
		return null;
	}
	
	@PutMapping(path = "/addDrink/{id}")
	public Orderr guestAddDrink(@PathVariable Long id, @RequestBody Orderr order){
		Drink drink = drinkService.findOne(id);
		order.getDrinks().add(drink);
		return orderService.save(order);
	}
	
	@PutMapping(path = "/addDish/{id}")
	public Orderr guestAddDish(@PathVariable Long id,  @RequestBody Orderr order){
		Dish dish = dishService.findOne(id);
		order.getFood().add(dish);
		
		return orderService.save(order);
	}
	
	@GetMapping(path = "/newOrder")
	public Orderr newOrder(){
		Orderr order = new Orderr();
		return order;
	}
	
	
	@PutMapping(path = "/newOrderDish/{id}/{reservationId}")
	public Orderr newOrderDish(@PathVariable Long id, @PathVariable Long reservationId, @RequestBody Orderr order){
		Dish dish = dishService.findOne(id);
		order.getFood().add(dish);
		return order;
	}
	
	@PutMapping(path = "/newOrderDrink/{id}/{reservationId}")
	public Orderr newOrderDrink(@PathVariable Long id, @PathVariable Long reservationId, @RequestBody Orderr order){
		Drink drink = drinkService.findOne(id);
		order.getDrinks().add(drink);
		return order;
	}
	
	@PutMapping(path = "/removeNewDish/{dishId}")
	public Orderr removeNewDish(@PathVariable Long dishId, @RequestBody Orderr order){
		Dish dish = dishService.findOne(dishId);
		
		for(int i = 0 ; i < order.getFood().size(); i++){
			if(order.getFood().get(i).getId() == dish.getId()){
				order.getFood().remove(i);
				break;
			}
		}
		
		return order;
	}
	
	@PutMapping(path = "/removeNewDrink/{dishId}")
	public Orderr removeNewDrink(@PathVariable Long dishId, @RequestBody Orderr order){
		Drink drink = drinkService.findOne(dishId);
		
		for(int i = 0 ; i < order.getDrinks().size(); i++){
			if(order.getDrinks().get(i).getId() == drink.getId()){
				order.getDrinks().remove(i);
				break;
			}
		}
		
		return order;
	}
	
	@PutMapping(path = "/makeNewOrder/{reservationId}")
	public Orderr makeNewOrder(@PathVariable Long reservationId, @RequestBody Orderr order){
		
		Long id = ((Waiter) httpSession.getAttribute("user")).getId();
		
		Waiter waiter = waiterService.findOne(id);
		
		Reservation reservation = reservationService.findOne(reservationId);
		Restaurant restaurant = reservation.getRestaurant();
		label:
		for(int i = 0 ; i < tableService.findAll().size(); i++){
			for(int j = 0 ; j < tableService.findAll().get(i).getReservations().size(); j++){
				if(tableService.findAll().get(i).getReservations().get(j).getId() == reservation.getId()){
					order.setTable(tableService.findAll().get(i));
					break label;
				}
			}
		}
		
		
		
		int total = order.getTotal();
		order.setTotal(total);
		if(order.getFood().size() > 0){
			order.setFoodStatus(FoodStatus.inPrepared);
		} else {
			order.setFoodStatus(FoodStatus.finished);
		}
		
		if(order.getDrinks().size() > 0){
			order.setDrinkStatus(DrinkStatus.inPrepared);
		} else {
			order.setDrinkStatus(DrinkStatus.finished);
		}
		order = orderService.save(order);
		restaurant.getOrder().add(order);
		reservation.getOrders().add(order);
		waiter.getOrders().add(order);
		reservationService.save(reservation);
		restaurantService.save(restaurant);
		waiterService.save(waiter);
		
		return order;
	}
	
	@PutMapping(path = "/changeOrder/{orderId}")
	public Orderr changeOrder(@PathVariable Long orderId){
		Orderr order = orderService.findOne(orderId);
		
		order.setChangeStatus(null);
		order.setChangeVersion(order.getChangeVersion()+1);
		
		return orderService.save(order);
	}
	
	@GetMapping(path = "/removeDish/{id}/{orderId}")
	public Orderr removeDish(@PathVariable Long id, @PathVariable Long orderId){
		//da li treba zastita ovde sa ifom za size hrane?
		Dish dish = dishService.findOne(id);
		Orderr order = orderService.findOne(orderId);
		for(int i = 0 ; i < order.getFood().size(); i++){
			if(order.getFood().get(i).getId() == dish.getId()){
				order.getFood().remove(i);
				break;
			}
		}
		
		return orderService.save(order);
	}
	
	@GetMapping(path = "/removeDrink/{id}/{orderId}")
	public Orderr removeDrink(@PathVariable Long id, @PathVariable Long orderId){
		//da li treba zastita ovde sa ifom za size hrane?
		Drink drink = drinkService.findOne(id);
		Orderr order = orderService.findOne(orderId);
		for(int i = 0 ; i < order.getDrinks().size(); i++){
			if(order.getDrinks().get(i).getId() == drink.getId()){
				order.getDrinks().remove(i);
				break;
			}
		}
		
		return orderService.save(order);
	}
	
	@GetMapping(path = "/getReservations")
	public List<Reservation> getReservations(){
		Long id = ((Waiter) httpSession.getAttribute("user")).getId();
		
		Waiter waiter = waiterService.findOne(id);
		List<Reservation> reservations = reservationService.findAll();
		Set<Reservation> returnReservationsSet = new HashSet<Reservation>();
		for(int i = 0 ; i < reservations.size(); i++){
			for(int j = 0 ; j < reservations.get(i).getOrders().size(); j++){
				for(int k = 0 ; k < waiter.getOrders().size(); k++){
					if(reservations.get(i).getOrders().get(j).getId() == waiter.getOrders().get(k).getId()){
						returnReservationsSet.add(reservations.get(i));
					}
				}
			}
		}
		
		List<Reservation> returnReservations = new ArrayList<>(returnReservationsSet);
		
		List<Reservation> activeReservations = getActiveReservations(returnReservations, waiter);
		
		return activeReservations;
	}
	
	@GetMapping("/waiterTables")
	public List<Table> waiterTables(){
		Long id = ((Waiter) httpSession.getAttribute("user")).getId();
		Waiter waiter = waiterService.findOne(id);
		
		Date date = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		List<ChangedShiftWaiter> changedShifts = changedShiftService.findAll();
		for (int i = 0; i < changedShifts.size(); i++) {
			if ((changedShifts.get(i).getWaiter1().getId() == waiter.getId()
					&& changedShifts.get(i).getDate().toString().equals(ft.format(date)))) {
				return changedShifts.get(i).getWaiter2().getTablesForHandling();
			} else if ((changedShifts.get(i).getWaiter2().getId() == waiter.getId()
					&& changedShifts.get(i).getDate().toString().equals(ft.format(date)))) {
				return changedShifts.get(i).getWaiter1().getTablesForHandling();
			}
		}
		
		return waiter.getTablesForHandling();
	}
	
	@GetMapping(path="/getTables/{id}")
	public List<app.restaurant.Table> getTables(@PathVariable Long id){
		Restaurant restaurant = restaurantService.findOne(id);
		ArrayList<app.restaurant.Table> outTables = new ArrayList<app.restaurant.Table>();
		for(int i=0; i<restaurant.getSegments().size(); i++){
			outTables.addAll(restaurant.getSegments().get(i).getTables());
		}
		return outTables;
	}
	
	@GetMapping(path = "/changeOrdersList")
	public List<Orderr> changeOrdersList(){
		List<Reservation> reservations = getReservations();
		//List<Reservation> reservations = reservationService.findAll();
		List<Orderr> orders = new ArrayList<Orderr>();
		Long id = ((Waiter) httpSession.getAttribute("user")).getId();
		Waiter waiter = waiterService.findOne(id);
		
		for(int i = 0 ; i < reservations.size(); i++){
			for(int j = 0 ; j < reservations.get(i).getOrders().size(); j++){
				for(int k = 0 ; k < waiter.getOrders().size(); k++){
					if(reservations.get(i).getOrders().get(j).getId() == waiter.getOrders().get(k).getId()){
						Orderr order = orderService.findOne(reservations.get(i).getOrders().get(j).getId());
						if(order.getCheckVersion() == 0){
							orders.add(order);
						}
					}
				}
			}
		}
		
		
		return orders;
	}
	
	
	public List<Reservation> getActiveReservations(List<Reservation> reservations, Waiter waiter){
		
		List<Reservation> returnReservation = new ArrayList<Reservation>();
		Date date = new Date();
		SimpleDateFormat date24Format = new SimpleDateFormat("HH:mm");
		String timeString = date24Format.format(date);
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		try {
			
			for (int i = 0; i < reservations.size(); i++) {
					if(reservations.get(i).getDate().toString().equals(ft.format(date))){
						
					String time = "", timeEnd = "";
					time += "" + reservations.get(i).getHours() + ":" + reservations.get(i).getMinutes();
					timeEnd += "" + (reservations.get(i).getHours() + reservations.get(i).getDuration()) + ":"
							+ reservations.get(i).getMinutes();
					Date currentDate = new Date();
					Date timeDate = new Date();
					Date timeDateEnd = new Date();
					Date shiftTime = new Date();
					timeDate = date24Format.parse(time);
					timeDateEnd = date24Format.parse(timeEnd);
					shiftTime = date24Format.parse("16:00");
					currentDate = date24Format.parse(timeString);
					if (currentDate.after(timeDate) && currentDate.before(timeDateEnd)) {
						if(waiter.getDefaultShift().compareTo(DefaultShift.First) == 0){
							if(currentDate.before(shiftTime)){
								returnReservation.add(reservations.get(i));
							}
						} else {
							if(currentDate.after(shiftTime)){
								returnReservation.add(reservations.get(i));
							}
						}
					}
				}
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return returnReservation;
	}

}
