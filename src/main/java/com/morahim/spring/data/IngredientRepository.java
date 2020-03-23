package com.morahim.spring.data;

import com.morahim.spring.Ingredient;
import org.springframework.data.repository.CrudRepository;

public interface IngredientRepository extends CrudRepository<Ingredient, String> {

    Iterable<Ingredient> findAll();

    Ingredient save(Ingredient ingredient);

}
