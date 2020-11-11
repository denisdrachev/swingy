package ru.swingy.service;

import org.springframework.stereotype.Service;
import ru.swingy.model.Artifact;

import java.util.Random;

@Service
public class ArtifactService {

    private Random random = new Random();

    public Artifact tryGetArtifact(Integer heroLevel) {
        if (random.nextInt(4) == 0) {
            return new Artifact(heroLevel);
        }
        return null;
    }
}
