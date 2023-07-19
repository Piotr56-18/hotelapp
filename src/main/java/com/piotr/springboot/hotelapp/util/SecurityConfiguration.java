package com.piotr.springboot.hotelapp.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    @Bean
    public InMemoryUserDetailsManager userDetailsManager(){
        UserDetails admin = User.builder()
                .username("admin")
                .password("{noop}admin")
                .roles("ADMIN", "USER")
                .build();
        UserDetails user = User.builder()
                .username("user")
                .password("{noop}user")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(admin, user);
    }
    @Bean
    public SecurityFilterChain filterChain (HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers("/").hasRole("USER")
                                .requestMatchers("/reservationsWizard/**").hasRole("USER")
                                .requestMatchers("/rooms/**").hasRole("ADMIN")
                                .requestMatchers("/guests/**").hasRole("ADMIN")
                                .requestMatchers("/reservations/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                                .loginPage("/showLoginPage")
                                .loginProcessingUrl("/authenticateTheUser")
                                .permitAll()
                )
                .logout(logout->logout.permitAll()
                )
                .exceptionHandling(configurer->configurer.accessDeniedPage("/access-denied"));
        return httpSecurity.build();
    }
    /*
    @Bean
    public SecurityFilterChain filterChain (HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(configurer->
                configurer
                        .requestMatchers(HttpMethod.GET, "/guests/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/guests/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/rooms/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/rooms/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/reservations/list/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/reservations/addReservationForm").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/reservations/updateReservationForm").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/reservations/delete").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/reservations/save").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/reservations/createReservationDate").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/reservations/createReservationRoomChoosing").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/reservations/createReservationGuestDetails").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/reservations/createReservationSuccess").hasRole("USER"));
        httpSecurity.httpBasic();
        httpSecurity.csrf().disable();
        return httpSecurity.build();
        }
*/
}
