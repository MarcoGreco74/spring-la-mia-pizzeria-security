package security.example.spring_la_mia_pizzeria_security.controller.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import security.example.spring_la_mia_pizzeria_security.model.Pizzeria;
import security.example.spring_la_mia_pizzeria_security.repository.IngredientiPizzaRepository;
import security.example.spring_la_mia_pizzeria_security.repository.OffertaPizzaRepository;
import security.example.spring_la_mia_pizzeria_security.repository.PizzeriaRepository;

@RestController
@RequestMapping("/api/pizzeria/v2")
public class PizzeriaAdvancedRestApi {

    private PizzeriaRepository pizzeriaRepository;
    private OffertaPizzaRepository repositoryOfferta;
    private IngredientiPizzaRepository ingredientiRepository;

    @Autowired
    public PizzeriaAdvancedRestApi(PizzeriaRepository pizzeriaRepository, OffertaPizzaRepository repositoryOfferta, IngredientiPizzaRepository ingredientiRepository ){
        this.pizzeriaRepository = pizzeriaRepository;
        this.repositoryOfferta = repositoryOfferta;
        this.ingredientiRepository = ingredientiRepository;
    }

    @GetMapping
    public ResponseEntity<List<Pizzeria>> index(@RequestParam(name="keyword", required=false) String param){
        List<Pizzeria> pizze;
        if(param != null && !param.isBlank()){
            pizze = pizzeriaRepository.findByNomeContainingIgnoreCase(param);
        }else{
             pizze = pizzeriaRepository.findAll();
        }
        //return new ResponseEntity<>(pizze, HttpStatus.OK);
        return ResponseEntity.ok().body(pizze);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> trovaxId(@PathVariable Integer id) {
        Optional<Pizzeria> optPizza = pizzeriaRepository.findById(id);
        if(!optPizza.isPresent()) {
            Map<String, String> msg = new HashMap<>();
            msg.put("errore", "Utente con ID " + id + " non trovato");
            return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(msg);
        }else{
            return new ResponseEntity<>(optPizza.get(), HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletexId(@PathVariable Integer id){
        pizzeriaRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Pizzeria> create(@RequestBody Pizzeria entity) {
        Pizzeria pizza = pizzeriaRepository.save(entity);
        return new ResponseEntity<>(pizza, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Pizzeria> update(@PathVariable Integer id, @RequestBody Pizzeria entity) {
        try {
            Pizzeria pizzaUpdated = pizzeriaRepository.save(entity);
            return new ResponseEntity<>(pizzaUpdated, HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
