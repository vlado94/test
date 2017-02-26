package app.bidder;

import java.util.ArrayList;
import java.util.List;

import app.restaurant.restaurantOrder.RestaurantOrderr;

public interface BidderService {
	List<Bidder> findAll();

	Bidder save(Bidder bidder);

	Bidder findOne(Long id);

	Bidder findOne(String mail, String password);

	Bidder findOneWithMail(String mail);

	void delete(Long id);

	ArrayList<RestaurantOrderr> selectAllOffersWhereBidderCompeted(Bidder bidder);

	boolean tryToChangeValueOfOffer(RestaurantOrderr restaurantOrderr, Long bidderId, Bidder bidder);
}