package com.manager.boss;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
@Transactional
public class BossManagerServiceImpl implements BossManagerService {
	private final BossManagerRepository repository;

	@Autowired
	public BossManagerServiceImpl(final BossManagerRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<BossManager> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public BossManager save(BossManager boss) {
		return repository.save(boss);
	}

	@Override
	public BossManager findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public BossManager findByMailAndPassword(String mail, String password) {
		return repository.findByMailAndPassword(mail, password);
	}

	@Override
	public BossManager findByMail(String mail) {
		return repository.findByMail(mail);
	}
}