package ru.swingy.model;

import lombok.Data;
import ru.swingy.interfaces.MapObject;

@Data
public class GameMap {

    private Integer dimension;
    private MapObject[][] map;

    public GameMap(Integer heroLevel) {
        this.dimension = (heroLevel - 1) * 5 + 10 - (heroLevel % 2);
        this.map = new MapObject[dimension][dimension];
    }
}
