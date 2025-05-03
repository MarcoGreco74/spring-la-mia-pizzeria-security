package security.example.spring_la_mia_pizzeria_security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import security.example.spring_la_mia_pizzeria_security.model.OffertePizza;

public interface OffertaPizzaRepository extends JpaRepository<OffertePizza, Integer>{

}
