package com.morahim.spring.data;

import com.morahim.spring.Taco;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TacoRepository extends CrudRepository<Taco, UUID>  {

    Taco save(Taco design);

}
