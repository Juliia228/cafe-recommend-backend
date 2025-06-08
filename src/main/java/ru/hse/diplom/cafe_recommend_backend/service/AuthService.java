package ru.hse.diplom.cafe_recommend_backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.hse.diplom.cafe_recommend_backend.exception.UserAlreadyExistsException;
import ru.hse.diplom.cafe_recommend_backend.model.Role;
import ru.hse.diplom.cafe_recommend_backend.model.UserDetailsImpl;
import ru.hse.diplom.cafe_recommend_backend.model.dto.AuthResponseDto;
import ru.hse.diplom.cafe_recommend_backend.model.dto.NewUserRequestDto;
import ru.hse.diplom.cafe_recommend_backend.model.dto.TokenResponseDto;
import ru.hse.diplom.cafe_recommend_backend.model.dto.UserDto;
import ru.hse.diplom.cafe_recommend_backend.model.entity.RefreshToken;
import ru.hse.diplom.cafe_recommend_backend.model.entity.User;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenGenerationService tokenGenerationService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponseDto authenticate(String phone, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(phone, password));
        if (authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if (!Objects.equals(phone, userDetails.getUsername())) {
                throw new RuntimeException(String.format("Ошибка аутентификации для пользователя с phone = %s", phone));
            }
            User user = userService.getByPhone(phone);
            return tokenGenerationService.createTokens(user, userDetails);
        } else {
            throw new UsernameNotFoundException("User is not authorized");
        }
    }

    @Transactional
    public AuthResponseDto authenticateAdmin(String phone, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(phone, password));
        if (authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if (!Objects.equals(phone, userDetails.getUsername())) {
                throw new RuntimeException(String.format("Ошибка аутентификации для пользователя с phone = %s", phone));
            }
            if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()))) {
                User user = userService.getByPhone(phone);
                return tokenGenerationService.createTokens(user, userDetails);
            } else {
                throw new RuntimeException(String.format("User with phone = %s does not have admin rights", phone));
            }
        } else {
            throw new UsernameNotFoundException("User is not authorized");
        }
    }

    @Transactional
    public UserDto register(NewUserRequestDto new_user) {
        if (userService.existsByPhone(new_user.getPhone())) {
            throw new UserAlreadyExistsException(new_user.getPhone());
        }
        new_user.setPassword(passwordEncoder.encode(new_user.getPassword()));
        new_user.setKeyWord(passwordEncoder.encode(new_user.getKeyWord()));
        return userService.add(new_user);
    }

    @Transactional
    public TokenResponseDto generateAccessToken(String requestRefreshToken) {
        RefreshToken refreshToken = tokenGenerationService.getRefreshTokenEntity(requestRefreshToken);
        User user = userService.get(refreshToken.getUserId());
        String new_token = tokenGenerationService.generateToken(new UserDetailsImpl(user), user.getId());
        return TokenResponseDto.builder()
                .access_token(new_token)
                .refresh_token(requestRefreshToken)
                .build();
    }
}
