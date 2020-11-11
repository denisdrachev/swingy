package ru.swingy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.swingy.interfaces.MapObject;
import ru.swingy.model.GameMap;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class GameMapService {

    private Random random = new Random();
    private final EnemyService enemyService;

    public GameMap createGameMap(Integer levelHero) {
        GameMap gameMap = new GameMap(levelHero);
        initGameMap(gameMap, levelHero);
        return gameMap;
    }
    
    private void initGameMap(GameMap gameMap, Integer levelHero) {
        MapObject[][] mapObjects = gameMap.getMap();
        for (int i = 0; i < mapObjects.length; i++) {
            for (int j = 0; j < mapObjects[i].length; j++) {
                if (i != gameMap.getDimension() / 2 && j != gameMap.getDimension() / 2 && random.nextInt(5) == 0) {
                    mapObjects[i][j] = enemyService.createEnemy(levelHero);
                } else {
                    mapObjects[i][j] = null;
                }
            }
        }
    }

    public void clearMapField(GameMap gameMap, Integer positionX, Integer positionY) {
        gameMap.getMap()[positionX][positionY] = null;
    }
}