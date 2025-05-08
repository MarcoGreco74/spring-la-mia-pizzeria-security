package security.example.spring_la_mia_pizzeria_security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import security.example.spring_la_mia_pizzeria_security.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    
    Optional<Role> findByNome(String nome);
    
}

