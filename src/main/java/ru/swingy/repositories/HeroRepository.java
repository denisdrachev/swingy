package ru.swingy.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.swingy.model.Hero;

import java.util.List;

public interface HeroRepository extends CrudRepository<Hero, Long> {
//    List<Hero> findAllByName(String color);

//    @Override
    List<Hero> findAll();

//    @Query("TRUNCATE tbl_name")
//    @Query(value = "DROP table tbl_name")
//    void dropTable();
}
