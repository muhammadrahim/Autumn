package com.morahim.spring.web;

import com.morahim.spring.Taco;
import lombok.Getter;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Date;

@Relation(value = "taco", collectionRelation = "tacos")
public class TacoResource extends RepresentationModel<TacoResource> {

    private static final IngredientResourceAssembler ingredientResourceAssembler =
            new IngredientResourceAssembler();

    @Getter
    private Date createdAt;

    @Getter
    private String name;

    @Getter
    private CollectionModel<IngredientResource> ingredientResources;

    public TacoResource(Taco taco) {
        this.createdAt = taco.getCreatedAt();
        this.name = taco.getName();
        this.ingredientResources = ingredientResourceAssembler.toCollectionModel(taco.getIngredients());
    }
}
