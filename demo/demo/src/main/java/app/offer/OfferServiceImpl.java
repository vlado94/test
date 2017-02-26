package app.offer;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
@Transactional
public class OfferServiceImpl implements OfferService {
	private final OfferRepository repository;

	@Autowired
	public OfferServiceImpl(final OfferRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<Offer> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public Offer save(Offer offer) {
		return repository.save(offer);
	}

	@Override
	public Offer findOne(Long id) {
		return repository.findOne(id);
	}
}