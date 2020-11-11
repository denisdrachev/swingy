package ru.swingy.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.swingy.model.Artifact;

public interface ArtifactRepository extends CrudRepository<Artifact, Long> {
//    List<Hero> findAllByName(String color);
}
