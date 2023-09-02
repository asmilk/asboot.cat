/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.config.Customizer;
import org.springframework.security.web.session.HttpSessionEventPublisher;

/**
 * An example of explicitly configuring Spring Security with the defaults.
 *
 * @author Rob Winch
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		// @formatter:off
		http.authorizeHttpRequests(authorize -> 
				authorize
					.requestMatchers("/favicon.ico", "/webjars/**", "/login**").permitAll()
					.anyRequest().authenticated())
			.formLogin(formLogin ->
				formLogin.loginPage("/login"))
			.rememberMe(rememberMe -> 
				rememberMe.tokenValiditySeconds(60))
			.exceptionHandling(exceptionHandling ->
				exceptionHandling.accessDeniedPage("/error/access-denied"))
			.sessionManagement(sessionManagement ->
				sessionManagement.sessionConcurrency(concurrency -> 
					concurrency
						.maximumSessions(1)
						.maxSessionsPreventsLogin(true)
						.expiredUrl("/login?expired"))
					)
//			.csrf(csrf -> 
//				csrf.disable())
//			.logout(logout->
//				logout.logoutSuccessUrl("/index"))
		;
		// @formatter:on

		return http.build();
	}

	@Bean
	InMemoryUserDetailsManager userDetailsService() {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(User.withUsername("admin").password("{noop}123456").roles("ADMIN").build());
		manager.createUser(User.withUsername("staff").password("{noop}123456").roles("STAFF").build());
		manager.createUser(User.withUsername("user").password("{noop}123456").roles("USER").build());
		manager.createUser(User.withUsername("guest").password("{noop}123456").roles("GUEST").build());
		manager.createUser(User.withUsername("hacker").password("{noop}123456").roles("HACKER").disabled(true).build());
		manager.createUser(
				User.withUsername("graduate").password("{noop}123456").roles("GRADUATE").accountLocked(true).build());
		manager.createUser(
				User.withUsername("former").password("{noop}123456").roles("FORMER").accountExpired(true).build());
		manager.createUser(User.withUsername("repeater").password("{noop}123456").roles("REPEATER")
				.credentialsExpired(true).build());
		return manager;
	}

	@Bean
	RoleHierarchy roleHierarchy() {
		RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
		roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_STAFF > ROLE_USER" + System.getProperty("line.separator")
				+ "ROLE_A > ROLE_B > ROLE_C");
		return roleHierarchy;

	}

	@Bean
	MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
		DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
		expressionHandler.setRoleHierarchy(roleHierarchy);
		return expressionHandler;
	}

	@Bean
	HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}

}
