package com.morahim.spring.data;

import com.morahim.spring.Ingredient;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface IngredientRepository extends CrudRepository<Ingredient, UUID> {

    Iterable<Ingredient> findAll();

    Ingredient save(Ingredient ingredient);

}
