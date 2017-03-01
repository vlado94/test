package app.employed.bartender;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import app.employed.DefaultShift;
import app.employed.waiter.Waiter;
import app.order.DrinkStatus;
import app.order.OrderService;
import app.order.Orderr;
import app.reservation.Reservation;
import app.reservation.ReservationService;
import app.restaurant.Restaurant;
import app.restaurant.RestaurantService;

@RestController
@RequestMapping("/bartender")
public class BartenderController {

	private HttpSession httpSession;

	private final BartenderService bartenderService;
	private final OrderService orderService;
	private final ReservationService reservationService;
	private final RestaurantService restaurantService;

	@Autowired
	public BartenderController(final HttpSession httpSession, final BartenderService bartenderService,
			final OrderService orderService, final ReservationService reservationService, final RestaurantService restaurantService) {
		this.httpSession = httpSession;
		this.bartenderService = bartenderService;
		this.orderService = orderService;
		this.reservationService = reservationService;
		this.restaurantService = restaurantService;
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
	
	@GetMapping(path = "/employedBartenders")
	@ResponseStatus(HttpStatus.OK)
	public List<Bartender> employedBartenders(){
		Long id = ((Bartender) httpSession.getAttribute("user")).getId();
		Bartender bartender = bartenderService.findOne(id);
		Restaurant restaurant = new Restaurant();
		for(int i = 0 ; i < restaurantService.findAll().size(); i++){
			for(int j = 0 ; j < restaurantService.findAll().get(i).getBartenders().size(); j++){
				if(restaurantService.findAll().get(i).getBartenders().get(j).getId() == bartender.getId()){
					restaurant = restaurantService.findAll().get(i);
				}
			}
		}
		
		return restaurant.getBartenders();
	}

	// 2.4. sanker ima mogucnost da azurira podatke
	@PutMapping(path = "/profile")
	@ResponseStatus(HttpStatus.OK)
	public Bartender update(@Valid @RequestBody Bartender bartender) {
		Optional.ofNullable(bartenderService.findOne(bartender.getId()))
					.orElseThrow(() -> new ResourceNotFoundException("Resource Not Found!"));
		return bartenderService.save(bartender);
	}
	
	
	@PutMapping(path = "/changePassword/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Bartender changePassword(@PathVariable Long id, @Valid @RequestBody Bartender bartender) {
		Optional.ofNullable(bartenderService.findOne(id))
				.orElseThrow(() -> new ResourceNotFoundException("Resource Not Found!"));
		bartender.setId(id);
		return bartenderService.save(bartender);
	}

	// 2.4 prikaz porudzbina za sankera
	@GetMapping(path = "/orders")
	public ResponseEntity<List<Orderr>> findAllOrdrers() {
		Long id = ((Bartender) httpSession.getAttribute("user")).getId();
	    Bartender bartender = bartenderService.findOne(id);
		//List<Orderr> orders = bartenderService.findOne(id).getOrders();
	    List<Reservation> reservationsTemp = new ArrayList<Reservation>();
	    Date date = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		
	    for(int i = 0 ; i < reservationService.findAll().size(); i++){
			if(reservationService.findAll().get(i).getDate().toString().equals(ft.format(date))){
				reservationsTemp.add(reservationService.findAll().get(i));
			}
		}
		
		
		List<Reservation> reservations = getActiveReservations(reservationsTemp, bartender);
		List<Orderr> allOrders = orderService.findAll();
		//Optional.ofNullable(orders).orElseThrow(() -> new ResourceNotFoundException("Resource Not Found!"));
		
		List<Orderr> orders = new ArrayList<Orderr>();

		for (int i = 0; i < allOrders.size(); i++) {
			if (allOrders.get(i).getDrinks().size() > 0 && allOrders.get(i).getDrinkStatus() != null
					&& allOrders.get(i).getDrinkStatus().compareTo(DrinkStatus.inPrepared) == 0) {
				
				orders.add(allOrders.get(i));
				
			}
		}
		
		
		
		//danasnje rezervacije
		
		List<Orderr> returnOrders = new ArrayList<Orderr>();
		
		for(int i = 0 ; i < reservations.size(); i++){
			for(int k = 0 ; k < reservations.get(i).getOrders().size(); k++){
				for(int j = 0 ; j < orders.size(); j++){
					if(reservations.get(i).getOrders().get(k).getId() == orders.get(j).getId()){
						returnOrders.add(orders.get(j));
					}
				}
			}
		}
		//bartender.getOrders().addAll(returnOrders);
		//bartenderService.save(bartender);
		

		return new ResponseEntity<>(returnOrders, HttpStatus.OK);
	}


	// 2.4 sanker signalizir da je odgovarajuce pice spremno
	@GetMapping(path = "/drinkReady/{orderId}")
	@ResponseStatus(HttpStatus.OK)
	public Orderr drinkReady(@PathVariable Long orderId) {
		Optional.ofNullable(orderService.findOne(orderId))
				.orElseThrow(() -> new ResourceNotFoundException("Resource Not Found!"));
		Long id = ((Bartender) httpSession.getAttribute("user")).getId();
		Bartender bartender = bartenderService.findOne(id);
		Orderr order = orderService.findOne(orderId);
		if(order.getChangeStatus() != null){
			return null;
		}
		order.setDrinkStatus(DrinkStatus.finished);
		order.setCheckVersion(order.getCheckVersion()+1);
		for(int i = 0 ; i < bartender.getOrders().size(); i++){
			if(bartender.getOrders().get(i).getId() == orderId){
				bartender.getOrders().get(i).setDrinkStatus(DrinkStatus.finished);
				
				break;
			}
		}
		bartenderService.save(bartender);
		order.setId(orderId);
		return orderService.save(order);
	}

	
	public List<Reservation> getActiveReservations(List<Reservation> reservations, Bartender bartender){
		
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
						if(bartender.getDefaultShift().compareTo(DefaultShift.First) == 0){
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
			//return returnReservation;

			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return returnReservation;
	}
}
