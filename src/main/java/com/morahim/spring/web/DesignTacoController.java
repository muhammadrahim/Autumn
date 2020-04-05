package com.morahim.spring.web;

import com.morahim.spring.Ingredient;
import com.morahim.spring.Order;
import com.morahim.spring.Taco;
import com.morahim.spring.WebConfig;
import com.morahim.spring.data.IngredientRepository;
import com.morahim.spring.data.TacoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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

    @GetMapping("/all")
    public CollectionModel<TacoResource> getAllTacos() {
        Iterable<Taco> tacos = tacoRepository.findAll();
        return new TacoResourceAssembler()
                .toCollectionModel(tacos)
                .add(WebMvcLinkBuilder.linkTo(DesignTacoController.class)
                        .slash("all")
                        .withRel("all"));
    }

    @GetMapping("/recent")
    public Flux<Taco> getRecentTacos() {
        return Flux.fromIterable(tacoRepository.findAll()).take(10);
    }

    @GetMapping("/{id}")
    public Mono<Taco> tacoById(@PathVariable("id") UUID id) {
        return Mono.justOrEmpty(tacoRepository.findById(id));
    }

    @GetMapping("/ingredients/{id}")
    public Mono<Ingredient> ingredientById(@PathVariable("id") UUID ingredientId) {
        return WebClient.create()
                .put().uri("http://localhost:8080/ingredients/{id}", ingredientId)
                .retrieve()
                .bodyToMono(Ingredient.class);
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
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable("orderId") UUID orderId) {
        try {
            tacoRepository.deleteById(orderId);
        } catch (EmptyResultDataAccessException ignored) {
        }
    }

    private List<Ingredient> filterByType(
            List<Ingredient> ingredients, Type type) {
        return ingredients
                .stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }
}
