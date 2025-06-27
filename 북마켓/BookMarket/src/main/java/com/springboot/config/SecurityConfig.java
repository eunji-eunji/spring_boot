package com.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

	@Bean
	protected PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

//	@Bean
//	protected UserDetailsService users() {
//		UserDetails admin = User.builder()
//				.username("Admin")
//				.password(passwordEncoder().encode("Admin1234"))
//				.roles("ADMIN")
//				.build();
//		return new InMemoryUserDetailsManager(admin);
//	}

	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
				.csrf(AbstractHttpConfigurer::disable)

				// 특정 URL에 대한 권한 설정.
				.authorizeHttpRequests(
						authorizeRequests -> authorizeRequests
								.requestMatchers("/books/add").hasRole("ADMIN" )
								.anyRequest().permitAll()
				)
				//.formLogin(Customizer.withDefaults());
				.formLogin(
						formLogin->formLogin

								.loginPage("/login") // 사용자 정의 로그인 페이지
								.loginProcessingUrl("/login")
								.defaultSuccessUrl("/")// 로그인 성공 후 이동 페이지
								.failureUrl("/loginfailed") // 로그인 실패 후 이동 페이지
								.usernameParameter("username")
								.passwordParameter("password")
				)

				.logout(
						logout -> logout
								.logoutUrl("/logout")
								.logoutSuccessUrl("/login")
				);

		return http.build();
	}

   /* @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
       return(web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
    */
    /*
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user1 = User.withUsername("user1")
                   .password(passwordEncoder().encode("user1Pass"))
                   .roles("USER")
                   .build();
               UserDetails user2 = User.withUsername("user2")
                   .password(passwordEncoder().encode("user2Pass"))
                   .roles("USER")
                   .build();

               UserDetails admin = User.withUsername("admin")
                   .password(passwordEncoder().encode("adminPass"))
                   .roles("ADMIN")
                   .build();
              // return new InMemoryUserDetailsManager(user1, user2, admin);
               return new InMemoryUserDetailsManager( admin);
    }
    */

}
