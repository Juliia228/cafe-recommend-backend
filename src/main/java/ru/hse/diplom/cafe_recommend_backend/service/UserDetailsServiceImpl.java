package ru.hse.diplom.cafe_recommend_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.hse.diplom.cafe_recommend_backend.model.entity.User;
import ru.hse.diplom.cafe_recommend_backend.repository.UserRepository;
import ru.hse.diplom.cafe_recommend_backend.model.UserDetailsImpl;

//@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new UsernameNotFoundException("No user with phone=" + phone + " was found"));
        return new UserDetailsImpl(user);
    }
}
