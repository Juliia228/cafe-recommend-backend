package ru.hse.diplom.cafe_recommend_backend.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.linear.RealVector;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.hse.diplom.cafe_recommend_backend.exception.UserAlreadyExistsException;
import ru.hse.diplom.cafe_recommend_backend.model.dto.UserDto;
import ru.hse.diplom.cafe_recommend_backend.model.Role;
import ru.hse.diplom.cafe_recommend_backend.model.entity.User;
import ru.hse.diplom.cafe_recommend_backend.model.dto.AuthResponseDto;
import ru.hse.diplom.cafe_recommend_backend.model.dto.NewUserRequestDto;
import ru.hse.diplom.cafe_recommend_backend.repository.UserRepository;

import java.time.OffsetDateTime;
import java.util.*;

import static ru.hse.diplom.cafe_recommend_backend.model.Constants.USER_DOES_NOT_EXIST;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final IngredientService ingredientService;
    private final TokenGenerationService tokenGenerationService;

    public User getCurrentUser() {
        String phone = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByPhone(phone);
    }

    public User get(UUID id) {
        return userRepository.findById(id).orElseThrow();
    }

    public User getByPhone(String phone) {
        return userRepository.findByPhone(phone).orElseThrow();
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Transactional
    public RealVector getUserPreferences(UUID userId) {
        // Предпочтениями пользователя считаются оценки ингредиентов пользователя (0 - не пробовал, 1 - пробовал)
//        List<UUID> orderedDishes = orderInfoRepository.findOrderedDishIdByUserId(userId);
        List<UUID> orderedIngredients = ingredientService.getDistinctOrderedIngredientIds();
        return ingredientService.getRatedIngredientsVector(orderedIngredients);
    }

    public UserDto add(NewUserRequestDto userDto) {
        User user = User.builder()
                .id(UUID.randomUUID())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .phone(userDto.getPhone())
                .password(userDto.getPassword())
                .loyaltyDiscount(3)
                .createdAt(OffsetDateTime.now())
                .build();
        if (userDto.getRoles() == null || userDto.getRoles().length == 0) {
//            user.setRoles(new String[]{Role.USER.name()});
        } else {
            List<String> roles = Arrays.asList(userDto.getRoles());
            if (!roles.contains(Role.USER.name())) {
                roles.add(Role.USER.name());
            }
//            user.setRoles(roles.toArray());
        }

        User newUser;
        try {
            newUser = userRepository.save(user);
        } catch (DuplicateKeyException e) {
            user.setId(UUID.randomUUID());
            newUser = userRepository.save(user);
        }

        return map(newUser);
    }

    @Transactional
    public AuthResponseDto authenticate(String phone, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(phone, password));
        if (authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return tokenGenerationService.createTokens(phone, userDetails);
        } else {
            throw new UsernameNotFoundException("User is not authorized");
        }
    }

    @Transactional
    public AuthResponseDto authenticateAdmin(String phone, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(phone, password));
        if (authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()))) {
                return tokenGenerationService.createTokens(phone, userDetails);
            } else {
                throw new RuntimeException(String.format("User with phone = %s does not have admin rights", phone));
            }
        } else {
            throw new UsernameNotFoundException("User is not authorized");
        }
    }

    @Transactional
    public UserDto register(NewUserRequestDto new_user) {
        if (userRepository.existsByPhone(new_user.getPhone())) {
            throw new UserAlreadyExistsException(new_user.getPhone());
        }
        new_user.setPassword(passwordEncoder.encode(new_user.getPassword()));
        return add(new_user);
    }

    @Transactional
    public User setRoleAdmin(UUID id) {
        User user = get(id);
        List<String> roles = new ArrayList<>();
//                Arrays.asList(user.getRoles());
        if (!roles.contains(Role.ADMIN.name())) {
            roles.add(Role.ADMIN.name());
        }
//        user.setRoles(roles.toArray());
        return userRepository.save(user);
    }

    public User edit(User new_user) {
        UUID id = new_user.getId();
        if (userRepository.existsById(id)) {
            return userRepository.save(new_user);
        }
        throw new RuntimeException(String.format(USER_DOES_NOT_EXIST, id));
    }

    @Transactional
    public void delete(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException(String.format(USER_DOES_NOT_EXIST, id));
        }
        userRepository.deleteById(id);
    }

    public static UserDto map(User user) {
        return UserDto.builder()
                .id(user.getId())
                .phone(user.getPhone())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .loyaltyDiscount(user.getLoyaltyDiscount())
                .createdAt(user.getCreatedAt())
                .build();
    }

}
