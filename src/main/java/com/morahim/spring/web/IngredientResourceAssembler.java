package com.morahim.spring.web;

import com.morahim.spring.Ingredient;
import com.morahim.spring.Taco;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public class IngredientResourceAssembler extends RepresentationModelAssemblerSupport<Ingredient, IngredientResource> {

    public IngredientResourceAssembler() {
        super(DesignTacoController.class, IngredientResource.class);
    }

    @Override
    protected IngredientResource instantiateModel(Ingredient ingredient) {
        return new IngredientResource(ingredient);
    }

    @Override
    public IngredientResource toModel(Ingredient ingredient) {
        return createModelWithId(ingredient.getId(), ingredient);
    }
}
