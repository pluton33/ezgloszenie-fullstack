package io.github.pluton33.ezgloszenie.service;

import io.github.pluton33.ezgloszenie.data.UserEntity;
import io.github.pluton33.ezgloszenie.repository.UsersRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements  UserService, UserDetailsService {
    private final UsersRepository repository;
    public UserServiceImpl(UsersRepository repository) {
        this.repository = repository;
    }
    @Override
    public UserEntity getUserByLogin(String login) {
        return repository.findByEmail(login)
                .orElseThrow(RuntimeException::new);
    }

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        UserEntity user = repository.findByEmail(username)
                .orElseThrow(()->new UsernameNotFoundException("user not found"));
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); //szybkie wygenerowanie hashu hasła
//        String freshHash = encoder.encode("pass");
//        System.out.println("NOWY, PEWNY HASH: " + freshHash);
        return User.builder()
                .username(user.getEmail())
                .password(user.getPasswordHash())
                .roles(user.getRole().name())
                .build();
    }
}
