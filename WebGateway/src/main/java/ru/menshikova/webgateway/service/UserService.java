package ru.menshikova.webgateway.service;

import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.menshikova.webgateway.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
    @Autowired private UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
    }
}