package com.digicore.challenge.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public interface IAuthenticationFacade {
	Authentication getAuthentication();

	String getName();

	@Component
	class AuthenticationFacade implements IAuthenticationFacade {

		@Override
		public Authentication getAuthentication() {
			return SecurityContextHolder.getContext().getAuthentication();
		}

		@Override
		public String getName() {
			return this.getAuthentication().getName();
		}

	}

}
