package com.morahim.spring.web;

import com.morahim.spring.Ingredient;
import com.morahim.spring.Taco;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;
import java.util.List;

public class IngredientResource extends RepresentationModel<IngredientResource> {

    @Getter
    private final String name;

    @Getter
    private Ingredient.Type type;

    public IngredientResource(Ingredient ingredient) {
        this.name = ingredient.getName();
        this.type = ingredient.getType();
    }
}
