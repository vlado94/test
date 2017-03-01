package app.bidder;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import app.restaurant.Restaurant;
import app.restaurant.RestaurantRepository;
import app.restaurant.restaurantOrder.RestaurantOrderRepository;
import app.restaurant.restaurantOrder.RestaurantOrderr;

@Service
@Transactional
public class BidderServiceImpl implements BidderService {
	private final BidderRepository repository;
	private final RestaurantRepository repositoryRestaurant;
	private final RestaurantOrderRepository restaurantOrderRepository;
	
	@Autowired
	public BidderServiceImpl(final BidderRepository repository,final RestaurantRepository repositoryRestaurant,final RestaurantOrderRepository restaurantOrderRepository) {
		this.repository = repository;
		this.repositoryRestaurant = repositoryRestaurant;
		this.restaurantOrderRepository = restaurantOrderRepository;
	}

	@Override
	public List<Bidder> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public Bidder save(Bidder bidder) {
		return repository.save(bidder);
	}

	@Override
	public Bidder findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public void delete(Long id) {
		repository.delete(id);
	}

	@Override
	public Bidder findOneMailAndPass(String mail, String password) {
		return repository.findByMailAndPassword(mail,password);
	}

	@Override
	public Bidder findOneWithMail(String mail) {
		return repository.findByMail(mail);
	}

	//ovo moze da se smisli i upit na bazu da se uprosti skroz
	@Override
	public ArrayList<RestaurantOrderr> selectAllOffersWhereBidderCompeted(Bidder bidder) {
		ArrayList<RestaurantOrderr> restaurantOrders = new ArrayList<>();
		List<Restaurant> restaurants = Lists.newArrayList(repositoryRestaurant.findAll());
		for (int i = 0; i < restaurants.size(); i++) {
			Restaurant restaurant = restaurants.get(i);
			for (int j = 0; j < restaurant.getBidders().size(); j++) {
				if (restaurant.getBidders().get(j).getId() == bidder.getId()) {
					for (int q = 0; q < restaurant.getRestaurantOrders().size(); q++)
						if (restaurant.getRestaurantOrders().get(q).getStartDate()
								.before(restaurant.getRestaurantOrders().get(q).getEndDate()) && restaurant.getRestaurantOrders().get(q).getOrderActive().equals("open"))
							restaurantOrders.add(restaurant.getRestaurantOrders().get(q));
				}
			}
		}
		return restaurantOrders;
	}

	@Override
	public boolean tryToChangeValueOfOffer(RestaurantOrderr restaurantOrderr,Long bidderId) {
		List<RestaurantOrderr> restaurantOrderrs = Lists.newArrayList(restaurantOrderRepository.findAll());
		for (int i = 0; i < restaurantOrderrs.size(); i++) {
			if (restaurantOrderrs.get(i).getId() == restaurantOrderr.getId()) {
				// provera da li je ponudjac vec dao ponudu za tu ponudu
				// restorana
				RestaurantOrderr restaurantOrder = restaurantOrderrs.get(i);
				if (!checkIfOfferExistInOrder(restaurantOrder, bidderId)) {
					if (restaurantOrder.getEndDate().getTime() > restaurantOrder.getStartDate().getTime()
							&& restaurantOrderrs.get(i).getOrderActive().equals("open")) {
						return true;
					}
				}
			}
		}
		return false;
	}

	// provera da li je ponudjac vec dao ponudu za tu ponudu
	private boolean checkIfOfferExistInOrder(RestaurantOrderr restaurantOrderr, Long bidderId) {
		for (int q = 0; q < restaurantOrderr.getOffers().size(); q++)
			if (restaurantOrderr.getOffers().get(q).getBidder().getId() == bidderId)
				return true;
		return false;
	}
}