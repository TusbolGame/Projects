/**
 * 
 */
package com.ticketadvantage.services.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ticketadvantage.services.dao.UserDAO;
import com.ticketadvantage.services.errorhandling.AppException;
import com.ticketadvantage.services.model.User;

/**
 * @author jmiller
 *
 */
public class TicketAdvantageAuthenticationProvider implements AuthenticationProvider {
	private static final Logger LOGGER = Logger.getLogger(TicketAdvantageAuthenticationProvider.class);

	@Autowired
	private UserDAO userDAO;

	/**
	 * 
	 */
	public TicketAdvantageAuthenticationProvider() {
		super();
		LOGGER.info("Entering TicketAdvantageAuthenticationProvider()");
		LOGGER.info("Exiting TicketAdvantageAuthenticationProvider()");
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.authentication.AuthenticationProvider#authenticate(org.springframework.security.core.Authentication)
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		LOGGER.info("Entering authenticate()");
		LOGGER.debug("authenticate: " + authentication);
		List<SimpleGrantedAuthority> authorities = null;

		User user = null;
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        LOGGER.debug("username: " + username);
        LOGGER.debug("password: " + password);

        try {
	        // Log the user in
	        user = userDAO.login(username, password);
	        if (user == null) {
	        	LOGGER.error("Username not found");
	            throw new BadCredentialsException("Username not found.");
	        }
	        if (!password.equals(user.getPassword())) {
	        	LOGGER.error("Wrong password");
	            throw new BadCredentialsException("Wrong password.");
	        }
	 
	        //Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
	        final SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_REST");
	        authorities = new ArrayList<SimpleGrantedAuthority>();
	        authorities.add(authority);
        } catch (AppException ae) {
        	LOGGER.error("Login failure", ae);
        	// throw new AuthenticationException("");
        	throw new UsernameNotFoundException("Error in retrieving user", ae);
        }
 
        LOGGER.info("Exiting authenticate()");
        return new UsernamePasswordAuthenticationToken(user, password, authorities);
    }

	/* (non-Javadoc)
	 * @see org.springframework.security.authentication.AuthenticationProvider#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}
}