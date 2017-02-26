package app.friends;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.ws.rs.BadRequestException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import app.guest.Guest;
import app.guest.GuestService;

@RestController
@RequestMapping("/friends")
public class FriendsController {
	private final FriendsService friendService;
	private final GuestService guestService;
	private HttpSession httpSession;

	@Autowired
	public FriendsController(final HttpSession httpSession, final FriendsService friendService, final GuestService guestService) {
		this.httpSession = httpSession;
		this.friendService = friendService;
		this.guestService = guestService;
	}

	// izlistavanje svih prijatelja
	@GetMapping
	public ResponseEntity<List<Friends>> findAll() {
		return new ResponseEntity<>(friendService.findAll(), HttpStatus.OK);
	}

	// izlistavanje svih prijatelja gosta koji je logovan
	@GetMapping(path = "/list")
	public ResponseEntity<List<Friends>> findAllFriends() {
		Guest logovani = (Guest) httpSession.getAttribute("user");
		Long idFriend = logovani.getId();
		List<Friends> friends = new ArrayList<>();
		List<Friends> allFriendShips = friendService.findAll();
		for (int i = 0; i < allFriendShips.size(); i++)
			if (allFriendShips.get(i).getFriendSendRequest().getId() == idFriend
				|| allFriendShips.get(i).getFriendReciveRequest().getId() == idFriend)
			friends.add(allFriendShips.get(i));
		return new ResponseEntity<>(friends, HttpStatus.OK);
	}
	
	// izlistavanje svih primljenih zahteva gosta koji je logovan
	@GetMapping(path = "/recivedRequests")
	public ResponseEntity<List<Friends>> findAllRecivedRequests() {
		Guest logovani = (Guest) httpSession.getAttribute("user");
		List<Friends> friends = new ArrayList<>();
		List<Friends> allFriendShips = friendService.findAll();
		for (int i = 0; i < allFriendShips.size(); i++)					// ako logovani user prima zahtev i ako je status pending
			if(allFriendShips.get(i).getStatus() == Friends.PENDING &&
			   allFriendShips.get(i).getFriendReciveRequest().equals(logovani))
			friends.add(allFriendShips.get(i));
		return new ResponseEntity<>(friends, HttpStatus.OK);
	}
	
	
	// 2.2
	// dodavanje prijatelja u listu prijatelja
	@GetMapping(path = "/addFriend/{idFriend}")
	@ResponseStatus(HttpStatus.OK)
	public void addFriend(@PathVariable Long idFriend) {
		Guest guest = Optional.ofNullable(guestService.findOne(idFriend))
				.orElseThrow(() -> new ResourceNotFoundException("Resource Not Found!"));
		addGuestAsFriend(guest);
	}
	
	
	

	public void addGuestAsFriend(Guest guest) {
		Guest home = (Guest) httpSession.getAttribute("user");
		List<Friends> friends = friendService.findAll();

		for (int i = 0; i < friends.size(); i++)
			if ((friends.get(i).getFriendReciveRequest().getId() == home.getId() 
					&& friends.get(i).getFriendSendRequest().getId() == guest.getId()) ||
					(friends.get(i).getFriendReciveRequest().getId() == home.getId() 
					&& friends.get(i).getFriendSendRequest().getId() == guest.getId()))
				// ovo se menja, znak je da ovaj prijetelj vec postoji tu
				throw new BadRequestException();
		friendService.save(new Friends(home, guest,Friends.PENDING));
	}
	
	
	
	
	//na bolji nacin preko upita....
	
	@GetMapping(path = "/acceptRequest/{idFriend}")
	@ResponseStatus(HttpStatus.OK)
	public void acceptRequest(@PathVariable Long idFriend){
		Guest logovani = (Guest) httpSession.getAttribute("user");
		Guest senderGuest = Optional.ofNullable(guestService.findOne(idFriend))
				.orElseThrow(() -> new ResourceNotFoundException("Resource Not Found!"));
		//---pronaci friend par gde je friendSendRequest==senderGuest, a friendReciveRequest==logovani
		List<Friends> friends = friendService.findAll();
		for (int i = 0; i < friends.size(); i++){
			if(friends.get(i).getFriendReciveRequest().equals(logovani) && 
			   friends.get(i).getFriendSendRequest().equals(senderGuest) ){
				friendService.remove(friends.get(i)); //nzm kako drugacije da promenim vrednost kolone status.
				friendService.save(new Friends(senderGuest,logovani,Friends.ACCEPTED));
				System.out.println("accepted "+friends.get(i).getStatus());
			}
		}
	}
	
	
	
	//na bolji nacin preko upita....
	@GetMapping(path = "/rejectRequest/{idFriend}")
	@ResponseStatus(HttpStatus.OK)
	public void rejectRequest(@PathVariable Long idFriend){
		Guest logovani = (Guest) httpSession.getAttribute("user");
		Guest senderGuest = Optional.ofNullable(guestService.findOne(idFriend))
				.orElseThrow(() -> new ResourceNotFoundException("Resource Not Found!"));
		//---pronaci friend par gde je friendSendRequest==senderGuest, a friendReciveRequest==logovani
		List<Friends> friends = friendService.findAll();
		for (int i = 0; i < friends.size(); i++){
			if(friends.get(i).getFriendReciveRequest().equals(logovani) && 
			   friends.get(i).getFriendSendRequest().equals(senderGuest) ){
				friendService.remove(friends.get(i));	//nzm kako drugacije da promenim vrednost kolone status. Promeniti OBAVEZNO!
				friendService.save(new Friends(senderGuest,logovani,Friends.REJECTED));
				System.out.println("rejected");
			}
		}

		
	}
	
	
	//Brisanje prijateljstva
	@GetMapping(path = "/unfriend/{idFriend}")
	@ResponseStatus(HttpStatus.OK)
	public void unfriend(@PathVariable Long idFriend) {
		friendService.remove(idFriend);
	}
	
}