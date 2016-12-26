package com.bidder;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bidder")
public class BidderController {
	private final BidderService bidderService;
	private HttpSession httpSession;

	@Autowired
	public BidderController(final HttpSession httpSession, final BidderService bidderService) {
		this.bidderService = bidderService;
		this.httpSession = httpSession;
	}

	@SuppressWarnings("unused")
	@GetMapping("/checkRights")
	public boolean checkRights() {
		try {
			Bidder bidder = ((Bidder) httpSession.getAttribute("user"));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public Bidder findAllLastOrders() {
		Long bidderId = ((Bidder) httpSession.getAttribute("user")).getId();
		Bidder bidder = bidderService.findOne(bidderId);
		return bidder;
	}
}