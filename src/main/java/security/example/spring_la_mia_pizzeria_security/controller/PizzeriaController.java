package security.example.spring_la_mia_pizzeria_security.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import security.example.spring_la_mia_pizzeria_security.model.Ingredienti;
import security.example.spring_la_mia_pizzeria_security.model.OffertePizza;
import security.example.spring_la_mia_pizzeria_security.model.Pizzeria;
import security.example.spring_la_mia_pizzeria_security.repository.IngredientiPizzaRepository;
import security.example.spring_la_mia_pizzeria_security.repository.OffertaPizzaRepository;
import security.example.spring_la_mia_pizzeria_security.repository.PizzeriaRepository;


@Controller
@RequestMapping("/pizzeria")
public class PizzeriaController {

    private PizzeriaRepository pizzeriaRepository;
    private OffertaPizzaRepository repositoryOfferta;
    private IngredientiPizzaRepository ingredientiRepository;

    @Autowired
    public PizzeriaController(PizzeriaRepository pizzeriaRepository, OffertaPizzaRepository repositoryOfferta, IngredientiPizzaRepository ingredientiRepository ){
        this.pizzeriaRepository = pizzeriaRepository;
        this.repositoryOfferta = repositoryOfferta;
        this.ingredientiRepository = ingredientiRepository;
    }
    
    @GetMapping
    public String index(Authentication authentication, Model model, @RequestParam(value="keyword", required=false) String nome){
        List<Pizzeria> result;
        if(nome != null  && !nome.isBlank()){
            //result = pizzeriaRepository.findByNome(nome);
            result = pizzeriaRepository.findByNomeContainingIgnoreCase(nome);
        }else{
            result = pizzeriaRepository.findAll();
        }
        model.addAttribute("list", result);
        model.addAttribute("username", authentication.getName());
        return "pizzeria/index";
    }

    @GetMapping("/show_id/{id}")
    public String show_id(@PathVariable("id") Integer id, Model model) {
        Optional<Pizzeria> optPizzeria = pizzeriaRepository.findById(id);
        if(optPizzeria.isPresent()){
            model.addAttribute("pizza", optPizzeria.get());
            return "pizzeria/show_id";
        }
        model.addAttribute("errorMsg", "Pagina non trovata");
        return "pizzeria/error_page";
    }

   @GetMapping("/showdescrizione/{descrizione}")
      public String showdescrizione(@PathVariable("descrizione") String descrizione, Model model){
        List<Pizzeria> resultDescrizione = pizzeriaRepository.findDescrizioneContainingIgnoreCase(descrizione);
        model.addAttribute("pizza", resultDescrizione);
        return "pizzeria/showdescrizione";
   }

   @GetMapping("/create")
      public String createPizza(Model model){
        model.addAttribute("nuovapizza", new Pizzeria());
        model.addAttribute("ingredientiList", ingredientiRepository.findAll());
        model.addAttribute("ingredientiObj", new Ingredienti()); // non serve
        return "pizzeria/create";
    }
    
   @PostMapping("/create")
   public String storePizza(@Valid @ModelAttribute("nuovapizza") Pizzeria formPizza, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes){
    if(bindingResult.hasErrors()) {
        return "pizzeria/create";
        }
        model.addAttribute("ingredientiList", ingredientiRepository.findAll());
        pizzeriaRepository.save(formPizza);
        redirectAttributes.addFlashAttribute("successMessage", "Pizza creata con successo!");
        return "redirect:/pizzeria";
   }

   @GetMapping("/edit/{id}")
   public String edit(@PathVariable("id") Integer id, Model model){
    model.addAttribute("modificapizza", pizzeriaRepository.findById(id).get());
    model.addAttribute("ingredientiList", ingredientiRepository.findAll());
    return "/pizzeria/edit";
   }

   @PostMapping("edit/{id}")
   public String update(@Valid @ModelAttribute("modificapizza") Pizzeria formPizza, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes){
    if(bindingResult.hasErrors()) {
        return "pizzeria/edit";
        }
        model.addAttribute("ingredientiList", ingredientiRepository.findAll());
        pizzeriaRepository.save(formPizza);
        redirectAttributes.addFlashAttribute("successMessage", "Pizza modificata con successo!");
        return "redirect:/pizzeria";
   }

   @PostMapping("delete/{id}")
   public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes ){
    Pizzeria pizzeria = pizzeriaRepository.findById(id).get();
    if(!pizzeria.getOffertePizza().isEmpty()){
        redirectAttributes.addFlashAttribute("errorMessage", "Ci sono offerte collegate a questa pizza, non si può cancellare!");
        return "redirect:/pizzeria";
    }
    pizzeriaRepository.deleteById(id);
    return "redirect:/pizzeria";
   }

   @GetMapping("/{id}/offerta")
    public String offerta(@PathVariable Integer id, Model model) {
        OffertePizza offertaPizza = new OffertePizza();
        offertaPizza.setPizzeria(pizzeriaRepository.findById(id).get());
        model.addAttribute("offertaPizza", offertaPizza);
        model.addAttribute("editMode", false);
        return "offerte/edit";
    }

}
