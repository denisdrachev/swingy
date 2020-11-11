package ru.swingy.model;

import lombok.Data;
import ru.swingy.interfaces.MapObject;
import ru.swingy.utils.Util;

@Data
public class Enemy implements MapObject {

    private Integer attack;
    private Integer defence;
    private Integer hp;

    public Enemy(Integer heroLevel) {
        attack = 1 + (heroLevel * 7) / 100 + Util.random.nextInt((heroLevel + 1));
        defence = 1 + (heroLevel * 7) / 100 + Util.random.nextInt((heroLevel + 1));
        hp = 10 + (heroLevel * 10) / 100 + Util.random.nextInt(heroLevel + 1);
    }

    public Integer setDamage(Integer damage) {
        Integer readDamage;
        if (damage > defence) {
            readDamage = damage - defence;
        } else {
            readDamage = 1;
        }
        hp -= readDamage;
        if (hp < 0)
            hp = 0;
        return readDamage;
    }

    public String getHtml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>")
                .append("<h1>Enemy</h1>")
                .append("<ul>")
                .append("<li>")
                .append("Attack: ").append(attack)
                .append("</li>")
                .append("<li>")
                .append("Defence: ").append(defence)
                .append("</li>")
                .append("<li>")
                .append("HP: ").append(hp)
                .append("</li>")
                .append("</ul>")
                .append("</html>");
        return sb.toString();
    }

    public String getDiv() {
        StringBuilder sb = new StringBuilder();
//        sb.append("<div>")

        return sb.toString();
    }
}
