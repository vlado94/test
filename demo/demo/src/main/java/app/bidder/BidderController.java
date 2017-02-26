package app.bidder;

import java.util.ArrayList;
import java.util.List;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.ws.rs.BadRequestException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import app.offer.Offer;
import app.offer.OfferService;
import app.restaurant.Restaurant;
import app.restaurant.RestaurantService;
import app.restaurant.restaurantOrder.RestaurantOrderService;
import app.restaurant.restaurantOrder.RestaurantOrderr;

@RestController
@RequestMapping("/bidder")
public class BidderController {
	private final BidderService bidderService;
	private final RestaurantOrderService restaurantOrderrService;
	private final RestaurantService restaurantService;
	private final OfferService offerService;
	private HttpSession httpSession;

	@Autowired
	public BidderController(final HttpSession httpSession, final BidderService bidderService,
			final RestaurantService restaurantService, final RestaurantOrderService restaurantOrderrService,
			final OfferService offerService) {
		this.bidderService = bidderService;
		this.restaurantService = restaurantService;
		this.restaurantOrderrService = restaurantOrderrService;
		this.offerService = offerService;
		this.httpSession = httpSession;
	}

	
	@GetMapping("/updateBidderProfile/{firstName}/{lastName}/{password}")
	@ResponseStatus(HttpStatus.OK)
	public Bidder updateBidderProfile(@PathVariable String firstName,@PathVariable String lastName,@PathVariable String password) {
		Long bidderId = ((Bidder) httpSession.getAttribute("user")).getId();
		Bidder bidder = bidderService.findOne(bidderId);
		bidder.setPassword(password);
		bidder.setFirstname(firstName);
		bidder.setLastname(lastName);
		return bidderService.save(bidder);
	}
	
	@GetMapping("/checkRights")
	@ResponseStatus(HttpStatus.OK)
	public Bidder checkRights() throws AuthenticationException {
		try {
			return ((Bidder) httpSession.getAttribute("user"));
		} catch (Exception e) {
			throw new AuthenticationException("Forbidden.");
		}
	}

	// izlistavanje svih ponuda na koje je do sada konkurisao logovani ponudjac
	@GetMapping("/getOffers")
	@ResponseStatus(HttpStatus.OK)
	public ArrayList<RestaurantOrderr> getOffers() {
		Bidder bidder = ((Bidder) httpSession.getAttribute("user"));
		ArrayList<RestaurantOrderr> restaurantOrderrs = new ArrayList<>();
		List<Restaurant> restaurants = restaurantService.findAll();
		for (int i = 0; i < restaurants.size(); i++) {
			Restaurant restaurant = restaurants.get(i);
			for (int j = 0; j < restaurant.getRestaurantOrders().size(); j++) {
				RestaurantOrderr restaurantOrderr = restaurant.getRestaurantOrders().get(j);
				for (int q = 0; q < restaurantOrderr.getOffers().size(); q++) {
					Offer offer = restaurantOrderr.getOffers().get(q);
					if (offer.getBidder().getId() == bidder.getId()) {
						restaurantOrderrs.add((restaurants.get(i).getRestaurantOrders().get(j)));
					}
				}
			}
		}
		return restaurantOrderrs;
	}

	// izlistavanje svih ponuda za logovanog ponudjaca od svih restorana gde
	// moze da konkurise
	@GetMapping("/getActiveOffers")
	@ResponseStatus(HttpStatus.OK)
	public List<RestaurantOrderr> getActiveOffers() {
		Bidder bidder = ((Bidder) httpSession.getAttribute("user"));
		ArrayList<RestaurantOrderr> restaurantOrderrs = bidderService.selectAllOffersWhereBidderCompeted(bidder);
		return restaurantOrderrs;
	}

	// izmena vrednosti aktivne ponude
	@PostMapping("/changeOffer/{id}")
	@ResponseStatus(HttpStatus.OK)
	public String changeOffer(@PathVariable Long id,@Valid @RequestBody Offer offer) {
		RestaurantOrderr restaurantOrder = restaurantOrderrService.findOne(id);
		List<Offer> listOfOffers = restaurantOrder.getOffers();
		for (int j = 0; j < listOfOffers.size(); j++) {
			if (listOfOffers.get(j).getId() == offer.getId()) {
				if (restaurantOrder.getEndDate().getTime() > offer.getPosibleDelivery().getTime() && offer.getPosibleDelivery().getTime() > restaurantOrder.getStartDate().getTime()
					&& restaurantOrder.getOrderActive().equals("open")) {
					//glupost al da ne pravim bzv drugi objekat
					listOfOffers.get(j).setPrice(offer.getPrice());
					listOfOffers.get(j).setGaranty(offer.getGaranty());
					listOfOffers.get(j).setPosibleDelivery(offer.getPosibleDelivery());
					restaurantOrderrService.save(restaurantOrder);
					return "ok";
				}
			}
		}

		return "no";
	}

	@PostMapping("/competeWithInsertedValue/{id}")
	@ResponseStatus(HttpStatus.OK)
	public String competeWithInsertedValue(@PathVariable Long id,@Valid  @RequestBody Offer offer) {
		RestaurantOrderr restaurantOrderr = restaurantOrderrService.findOne(id);
		Long bidderId = ((Bidder) httpSession.getAttribute("user")).getId();
		Bidder bidder = bidderService.findOne(bidderId);
		if(!checkIfMakedOfferEarlier(restaurantOrderr,bidder) && offer.getPosibleDelivery().before(restaurantOrderr.getEndDate()) && offer.getPosibleDelivery().after(restaurantOrderr.getStartDate()))
			if (bidderService.tryToChangeValueOfOffer(restaurantOrderr, bidderId, bidder)) {
				offer.setAccepted("in progress");
				offer.setBidder(bidder);
				offerService.save(offer);
				restaurantOrderr.getOffers().add(offer);
				restaurantOrderrService.save(restaurantOrderr);
				return "ok";
			}
		return "no";
	}
	
	private boolean checkIfMakedOfferEarlier(RestaurantOrderr restaurantOrderr,Bidder bidder) {
		for(int i=0;i<restaurantOrderr.getOffers().size();i++) {
			if(restaurantOrderr.getOffers().get(i).getBidder().getId() == bidder.getId())
				return true;
		}
		return false;
	}
	
}