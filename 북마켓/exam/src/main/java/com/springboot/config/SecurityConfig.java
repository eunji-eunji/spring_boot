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
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public UserDetailsService userDetailsService(){
//        UserDetails admin = User.builder().username("admin").password(passwordEncoder().encode("a1234"))
//                .roles("ADMIN").build();
//        return new InMemoryUserDetailsManager(admin);
//    }

    @Bean
    public UserDetailsService users() {
        UserDetails user = User.builder()
                .username("guest")
                .password(passwordEncoder().encode("g1234"))
                .roles("USER")
                .build();
        UserDetails manager = User.builder()
                .username("manager")
                .password(passwordEncoder().encode("m1234"))
                .roles("MANAGER")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("a1234"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, manager, admin);

    }

//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(
//                        authorizeHttpRequests -> authorizeHttpRequests
//                                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
//                                .requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN")
//                                .requestMatchers("/member/**").authenticated()
//                                .anyRequest().permitAll()
//                )
//                .formLogin(Customizer.withDefaults());
//        // 기본 로그인 폼 사용(/login URL 자동 생성)
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain examMethod02(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                // 특정 URL에 대한 권한 설정.


                .authorizeHttpRequests(
                        authorizeRequests -> authorizeRequests
                                .requestMatchers("/admin").hasRole("ADMIN" )
                                .anyRequest().permitAll()
                )
                .formLogin(
                        formLogin->formLogin

                                .loginPage("/exam05") // 사용자 정의 로그인 페이지
                                .loginProcessingUrl("/exam05")
                                .defaultSuccessUrl("/admin") // 로그인 성공 후 이동 페이지
                                .usernameParameter("username")
                                .passwordParameter("password")
                                .failureUrl("/loginfailed") // 로그인 실패 후 이동 페이지

                )


                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/exam05")
                );
        return http.build();
    }
}
