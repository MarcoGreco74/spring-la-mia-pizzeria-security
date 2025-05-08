package security.example.spring_la_mia_pizzeria_security.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import security.example.spring_la_mia_pizzeria_security.model.Role;
import security.example.spring_la_mia_pizzeria_security.model.User;
import security.example.spring_la_mia_pizzeria_security.model.UserDto;
import security.example.spring_la_mia_pizzeria_security.repository.RoleRepository;
import security.example.spring_la_mia_pizzeria_security.repository.UserRepository;


@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public void registerNewUserAccount(UserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new RuntimeException("There is an account with that email address: "+ userDto.getUsername());
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role userRole = roleRepository.findByNome(userDto.getRole())
                     .orElseThrow(() -> new RuntimeException("Default role not found"));
        //user.setRoles(List.of(userRole));
        user.getRoles().add(userRole);

        userRepository.saveAndFlush(user);
    }

}