package ru.swingy.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.swingy.utils.Util;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.swing.*;
import java.util.Random;

@Data
@Entity

@NoArgsConstructor
public class Artifact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ToString.Exclude
    private Long id;
    private Integer attack;
    private Integer defence;
    private Integer hp;
    private String type;

    public Artifact(Integer level) {
        Random random = new Random();
        attack = 0;
        defence = 0;
        hp = 0;
        int i = random.nextInt(3);
        if (i == 0) {
            type = "Weapon";
            attack = 1 + random.nextInt(level + 1);
        } else if (i == 1) {
            type = "Helmet";
            hp = 5 + random.nextInt((level + 1) * (level + 1));
        } else {
            type = "Armor";
            defence = 1 + random.nextInt(level + 1);
        }
    }

    public Icon getIcon() {
        if (type.equals("Weapon")) {
            return Util.weapon;
        } else if (type.equals("Armor")) {
            return Util.armor;
        } else if (type.equals("Helmet")) {
            return Util.helmet;
        }
        return null;
    }

    public String toHtml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>")
                .append("<h1>Artifact</h1>")
                .append("<ul>")
                .append("<li>")
                .append("Type: ").append(type)
                .append("</li>")
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
}
