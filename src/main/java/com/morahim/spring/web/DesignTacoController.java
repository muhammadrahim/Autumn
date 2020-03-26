package com.morahim.spring.web;

import com.morahim.spring.Ingredient;
import com.morahim.spring.Order;
import com.morahim.spring.Taco;
import com.morahim.spring.data.IngredientRepository;
import com.morahim.spring.data.TacoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.morahim.spring.Ingredient.Type;

@Slf4j
@RestController
@RequestMapping(path = "/design",
        produces = "application/json")
@CrossOrigin(origins = "*")
public class DesignTacoController {

    private final IngredientRepository ingredientRepository;

    private TacoRepository tacoRepository;


    EntityLinks entityLinks;

    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepository, TacoRepository tacoRepository) {
        this.ingredientRepository = ingredientRepository;
        this.tacoRepository = tacoRepository;
    }

    @ModelAttribute(name = "order")
    public Order order() {
        return new Order();
    }

    @ModelAttribute(name = "design")
    public Taco design() {
        return new Taco();
    }

    @GetMapping
    public String showDesignForm(Model model) {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepository.findAll().forEach(ingredients::add);
        Type[] types = Ingredient.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(ingredients, type));
        }
        return "design";
    }

    @GetMapping("/recent")
    public CollectionModel<TacoResource> getAllTacos() {
        Iterable<Taco> tacos = tacoRepository.findAll();
        return new TacoResourceAssembler()
                .toCollectionModel(tacos)
                .add(WebMvcLinkBuilder.linkTo(DesignTacoController.class)
                        .slash("recent")
                        .withRel("all"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Taco> tacoById(@PathVariable("id") Long id) {
        Optional<Taco> optTaco = tacoRepository.findById(id);
        return optTaco.map(DesignTacoController::okHttp).orElseGet(
                DesignTacoController::notFoundHttp
        );
    }


    private static ResponseEntity<Taco> notFoundHttp() {
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    private static ResponseEntity<Taco> okHttp(Taco taco) {
        return new ResponseEntity<>(taco, HttpStatus.OK);
    }

    @PostMapping
    public String processDesign(
            @Valid @ModelAttribute("design") Taco design, Errors errors, @ModelAttribute Order order) {
        if (errors.hasErrors()) {
            return "design";
        }
        Taco saved = tacoRepository.save(design);
        order.addDesign(saved);
        return "redirect:/orders/current";
    }


    @DeleteMapping("/{orderId}")
    @ResponseStatus(code=HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable("orderId") Long orderId) {
        try {
            tacoRepository.deleteById(orderId);
        } catch (EmptyResultDataAccessException ignored) { }
    }

    private List<Ingredient> filterByType(
            List<Ingredient> ingredients, Type type) {
        return ingredients
                .stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }
}
