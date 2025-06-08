package ru.hse.diplom.cafe_recommend_backend.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.linear.RealVector;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.hse.diplom.cafe_recommend_backend.model.dto.*;
import ru.hse.diplom.cafe_recommend_backend.model.Role;
import ru.hse.diplom.cafe_recommend_backend.model.entity.User;
import ru.hse.diplom.cafe_recommend_backend.repository.UserRepository;

import java.time.OffsetDateTime;
import java.util.*;

import static ru.hse.diplom.cafe_recommend_backend.model.Constants.USER_DOES_NOT_EXIST;
import static ru.hse.diplom.cafe_recommend_backend.service.Utils.createIndexMap;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IngredientService ingredientService;
    private final OrderService orderService;
    private final LoyaltyProgramService loyaltyProgramService;

    public UserDto getCurrentUser() {
        String phone = SecurityContextHolder.getContext().getAuthentication().getName();
        return map(getByPhone(phone));
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

    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    @Transactional
    public GetUserDiscountDto countUserDiscount(String phone) {
        User user = getByPhone(phone);
        UserDto updatedUser = map(user);
        int userOrdersCount = orderService.getOrderCountByUser(user.getId());
        double userLoyaltyDiscount = user.getLoyaltyDiscount();

        LoyaltyProgramSettingsDto loyaltyProgramSettingsDto = loyaltyProgramService.getLoyaltySettings();
        double maxDiscount = loyaltyProgramSettingsDto.getMaxDiscount();

        if (userLoyaltyDiscount != maxDiscount
                // и если количество заказов, деленное на количество повышений скидки + 1 больше или равно требуемого значения
                && (userOrdersCount / (user.getLoyaltyDiscountIncrementCount() + 1)) >= loyaltyProgramSettingsDto.getOrdersThreshold()) {
            double newUserDiscount = userLoyaltyDiscount + loyaltyProgramSettingsDto.getDiscountIncrement();
            if (newUserDiscount > maxDiscount) {
                newUserDiscount = maxDiscount;
            }
            user.setLoyaltyDiscount(newUserDiscount);
            user.setLoyaltyDiscountIncrementCount(user.getLoyaltyDiscountIncrementCount() + 1);

            updatedUser = save(user);
        }
        return GetUserDiscountDto.of(updatedUser.getId(), updatedUser.getLoyaltyDiscount());
    }

    @Transactional
    public RealVector getUserPreferences(UUID userId) {
        // Предпочтениями пользователя считаются оценки ингредиентов пользователя (0 - не пробовал, 1 - пробовал)
//        List<UUID> orderedDishes = orderInfoRepository.findOrderedDishIdByUserId(userId);
        List<UUID> orderedIngredients = ingredientService.getDistinctOrderedIngredientIdsByUserId(userId);
        return ingredientService.getRatedIngredientsVector(orderedIngredients);
    }

    public Map<UUID, Integer> createUserIndexMap() {
        List<UUID> allUsers = getAll().stream()
                .map(User::getId)
                .toList();
        return createIndexMap(allUsers);
    }

    @Transactional
    public UserDto add(NewUserRequestDto userDto) {
        double baseDiscount = loyaltyProgramService.getLoyaltySettings().getBaseDiscount();
        User user = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .phone(userDto.getPhone())
                .password(userDto.getPassword())
                .keyWord(userDto.getKeyWord())
                .loyaltyDiscount(baseDiscount)
                .loyaltyDiscountIncrementCount(0)
                .roles(new String[]{Role.USER.name()})
                .createdAt(OffsetDateTime.now())
                .build();
        return map(userRepository.save(user));
    }

    public UserDto save(User user) {
        return map(userRepository.save(user));
    }

    public List<UserDto> saveAll(List<User> users) {
        return map(userRepository.saveAll(users));
    }

    @Transactional
    public UserDto setRoleAdmin(UUID id) {
        User user = get(id);
        List<String> roles = Arrays.asList(user.getRoles());
        if (!roles.contains(Role.ADMIN.name())) {
            roles.add(Role.ADMIN.name());
        }
        user.setRoles(roles.toArray(new String[0]));
        return map(userRepository.save(user));
    }

    @Transactional
    public void resetPassword(ResetPasswordRequestDto request) {
        User user = getByPhone(request.getPhone());
        if (!passwordEncoder.matches(user.getKeyWord(), request.getKeyWord())) {
            throw new RuntimeException("Wrong key word!");
        }
        request.setNewPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setPassword(request.getNewPassword());
        userRepository.save(user);
    }

    public UserDto edit(UserDto new_user) {
        UUID id = new_user.getId();
        if (userRepository.existsById(id)) {
            return map(userRepository.save(map(new_user)));
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

    public static UserWithRolesDto mapUserWithRolesDto(User user) {
        return UserWithRolesDto.builder()
                .id(user.getId())
                .phone(user.getPhone())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .loyaltyDiscount(user.getLoyaltyDiscount())
                .createdAt(user.getCreatedAt())
                .roles(user.getRoles())
                .build();
    }

    public static User map(UserDto user) {
        return User.builder()
                .id(user.getId())
                .phone(user.getPhone())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .loyaltyDiscount(user.getLoyaltyDiscount())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public static List<UserDto> map(List<User> users) {
        return users.stream()
                .map(UserService::map)
                .toList();
    }

}
