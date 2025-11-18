package com.synerge.order101.auth.model.service;

import com.synerge.order101.user.model.entity.User;
import com.synerge.order101.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try{
            Long userId = Long.parseLong(username);
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                throw new UsernameNotFoundException("User not found with ID: " + username);
            }
            return new CustomUserDetails(user);
        }
        catch (NumberFormatException e){
            throw new UsernameNotFoundException("올바른 형식이 아닙니다.");
        }

    }
}
