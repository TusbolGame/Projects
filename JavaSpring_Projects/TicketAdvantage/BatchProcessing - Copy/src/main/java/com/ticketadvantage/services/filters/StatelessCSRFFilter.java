/**
 * 
 */
package com.ticketadvantage.services.filters;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author jmiller
 *
 */
public class StatelessCSRFFilter extends OncePerRequestFilter {
	private final Logger log = Logger.getLogger(StatelessCSRFFilter.class);
	private static final String CSRF_TOKEN = "CSRF-TOKEN";
	private static final String X_CSRF_TOKEN = "X-CSRF-TOKEN";
	private final RequestMatcher requireCsrfProtectionMatcher = new DefaultRequiresCsrfMatcher();
	private final AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandlerImpl();

	/**
	 * 
	 */
	public StatelessCSRFFilter() {
		super();
		log.info("Entering StatelessCSRFFilter()");
		log.info("Exiting StatelessCSRFFilter()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.info("Entering doFilterInternal()");
		if (requireCsrfProtectionMatcher.matches(request)) {
			final String csrfTokenValue = request.getHeader(X_CSRF_TOKEN);
			final Cookie[] cookies = request.getCookies();

			String csrfCookieValue = null;
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					log.debug("Cookie Name: " + cookie.getName());
					if (cookie.getName().equals(CSRF_TOKEN)) {
						csrfCookieValue = cookie.getValue();
					}
				}
			}

			if (csrfTokenValue == null || !csrfTokenValue.equals(csrfCookieValue)) {
				log.debug("Header Value: " + csrfTokenValue);
				log.debug("Cookie Value: " + csrfCookieValue);
				log.info("Missing or non-matching CSRF-token");
				accessDeniedHandler.handle(request, response, new AccessDeniedException(
						"Missing or non-matching CSRF-token"));
				return;
			}
		}
		log.info("Exiting doFilterInternal()");
		filterChain.doFilter(request, response);
	}

	/**
	 * 
	 * @author jmiller
	 *
	 */
	public static final class DefaultRequiresCsrfMatcher implements RequestMatcher {
		private final Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");

		@Override
		public boolean matches(HttpServletRequest request) {
			return !allowedMethods.matcher(request.getMethod()).matches();
		}
	}
}
