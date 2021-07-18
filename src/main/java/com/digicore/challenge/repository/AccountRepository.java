package com.digicore.challenge.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import com.digicore.challenge.models.UserAccount;

@Component
@ApplicationScope
public class AccountRepository {

	private Map<String, UserAccount> systemStorageMap = new HashMap<>();

	public UserAccount save(UserAccount user) {
		this.systemStorageMap.put(user.getAccountNumber(), user);
		return user;
	}

	public Boolean existsByAccountNumber(String accountNumber) {
		return this.systemStorageMap.containsKey(accountNumber);
	}

	public Optional<UserAccount> findByAccountNumber(String accountNumber) {
		return Optional.ofNullable(this.systemStorageMap.get(accountNumber));
	}

	public UserAccount update(UserAccount user) {
		this.systemStorageMap.computeIfPresent(user.getAccountNumber(), (key, oldValue) -> user);
		return user;
	}

	public Boolean checkIfAccountNameExists(String accountName) {
		return this.systemStorageMap.entrySet().parallelStream()
				.filter(user -> user.getValue().getAccountName().equalsIgnoreCase(accountName))
				.collect(Collectors.toList()).size() > 0;
	}

	public void removeAll() {
		this.systemStorageMap.clear();
	}

}
