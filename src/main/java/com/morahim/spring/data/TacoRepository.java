package com.morahim.spring.data;

import com.morahim.spring.Ingredient;
import com.morahim.spring.Taco;
import org.springframework.data.repository.CrudRepository;

public interface TacoRepository extends CrudRepository<Taco, Long>  {

    Taco save(Taco design);

}
