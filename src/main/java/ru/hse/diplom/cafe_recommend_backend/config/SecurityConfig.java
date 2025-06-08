package ru.hse.diplom.cafe_recommend_backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.hse.diplom.cafe_recommend_backend.model.Role;
import ru.hse.diplom.cafe_recommend_backend.service.UserDetailsServiceImpl;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static ru.hse.diplom.cafe_recommend_backend.controller.AuthController.*;
import static ru.hse.diplom.cafe_recommend_backend.controller.DishController.*;
import static ru.hse.diplom.cafe_recommend_backend.controller.IngredientController.*;
import static ru.hse.diplom.cafe_recommend_backend.controller.UserController.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    @Lazy
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AUTH_POINT + REGISTER_POINT, AUTH_POINT + LOG_IN_POINT,
                                AUTH_POINT + LOG_IN_ADMIN_POINT, AUTH_POINT + RESET_PASSWORD_POINT,
                                AUTH_POINT + REFRESH_TOKEN_POINT, DISH_POINT + GET_ALL_DISHES_POINT)
                        .permitAll()
                        .requestMatchers(USER_REST_POINT, USER_REST_POINT + GET_RECOMMENDATIONS_POINT,
                                USER_REST_POINT + EDIT_USER_POINT)
                        .hasRole(Role.USER.name())
                        .requestMatchers(USER_REST_POINT + DELETE_USER_POINT, DISH_POINT + GET_DISH_POINT,
                                DISH_POINT + GET_DISH_INGREDIENTS_POINT, DISH_POINT + GET_DISH_WITH_INGREDIENTS_POINT,
                                INGREDIENT_REST_POINT + INGREDIENT_BY_ID_POINT, INGREDIENT_REST_POINT + ALL_INGREDIENTS_POINT,
                                DISH_POINT + GET_ALL_CATEGORIES_POINT)
                        .hasAnyRole(Role.USER.name(), Role.ADMIN.name())
                        .requestMatchers(AUTH_POINT + SET_ADMIN_POINT, DISH_POINT + NEW_DISH_POINT,
                                DISH_POINT + EDIT_DISH_POINT, DISH_POINT + DELETE_DISH_POINT, INGREDIENT_REST_POINT + NEW_INGREDIENT_POINT,
                                INGREDIENT_REST_POINT + EDIT_INGREDIENT_POINT, INGREDIENT_REST_POINT + DELETE_INGREDIENT_POINT)
                        .hasRole(Role.ADMIN.name())
                        .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
