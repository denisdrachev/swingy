package ru.swingy.service;

import org.springframework.stereotype.Service;
import ru.swingy.model.Enemy;

@Service
public class EnemyService {

    public Enemy createEnemy(Integer heroLevel) {
        return new Enemy(heroLevel);
    }
}
