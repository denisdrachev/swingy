package ru.swingy.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.swingy.utils.Util;

import javax.persistence.*;
import javax.swing.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Hero {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull(message = "Hero name cannot be null")
    @Size(min = 1, max = 50, message = "Min length hero name value is 1. Max length is 50")
    private String name;
    @Pattern(regexp = "(WARRIOR|ARCHER|WIZARD)", message = "Allowed hero class values: WARRIOR, ARCHER, WIZARD")
    @NotNull(message = "Field 'classType' cannot be empty")
//    @Size(min = 1, max = 50, message = "Min length 'classType' value is 1. Max length 'classType' value is 50")
    private String classType;
    @Min(value = 0, message = "Min value 'level' is 0")
    private Integer level;
    @Min(value = 0, message = "Min value 'experience' is 0")
    private Integer experience;
    @Min(value = 0, message = "Min value 'attack' is 0")
    private Integer attack;
    @Min(value = 0, message = "Min value 'defence' is 0")
    private Integer defence;
    @Min(value = 0, message = "Min value 'hp' is 1")
    private Integer hp;
    @ToString.Exclude
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Artifact> artifacts;

    @Transient
    private Integer currentHp;
    @Transient
    private Integer positionX;
    @Transient
    private Integer positionY;

    public Hero(String name, String classType) {
        this.name = name;
        this.classType = classType;
        this.level = 0;
        this.attack = 1;
        this.defence = 1;
        this.hp = 50;
        this.experience = 0;
        this.artifacts = new ArrayList<>();
        switch (classType) {
            case "ARCHER":
                this.attack += 10;
                break;
            case "WARRIOR":
                this.defence += 10;
                break;
            case "WIZARD":
                this.hp += 20;
                break;
        }
    }

    public void incPositionX(Integer value) {
        positionX += value;
    }

    public void incPositionY(Integer value) {
        positionY += value;
    }

    @Override
    public String toString() {
        return id + "." + getHeroInfo();
    }

    public String getHeroInfo() {
        return " name='" + name + '\'' +
                ", classType='" + classType + '\'' +
                ", level=" + level +
                ", experience=" + experience +
                ", attack=" + attack +
                ", defence=" + defence +
                ", hp=" + hp;
    }

    public Integer setDamage(Integer damage) {
        Integer readDamage;
        if (damage > defence) {
            readDamage = damage - defence;
        } else {
            readDamage = 1;
        }
        currentHp -= readDamage;
        if (currentHp < 0)
            currentHp = 0;
        return readDamage;
    }

    public String getHtmlInfo(boolean isCurrentHp) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<html>")
                .append("<ul style=\"list-style-type:none;\">")
                .append("<li>")
                .append("<b>Name</b>: ")
                .append(name)
                .append("</li>")
                .append("<li>")
                .append("<b>Level</b>: ")
                .append(level)
                .append("</li>")
                .append("<li>")
                .append("<b>Class</b>: ")
                .append(getClassType())
                .append("</li>")
                .append("<li>")
                .append("<b>Attack</b>: ")
                .append(attack)
                .append("</li>")
                .append("<li>")
                .append("<b>Experience</b>: ")
                .append(isCurrentHp ? experience + "/" + Util.getLevelExperience(level + 1) : experience)
                .append("</li>")
                .append("<li>")
                .append("<b>Defence</b>: ")
                .append(defence)
                .append("</li>")
                .append("<li>")
                .append("<b>HP</b>: ")
                .append(isCurrentHp ? currentHp + "/" + hp : hp)
                .append("</li>");
//                .append("<li>")
//                .append("<strong>Artefacts</strong>: ");

//        for (Artifact artifact : artifacts) {
//            stringBuilder.append("<br/>")
//                    .append("<b>Type</b>: ").append(artifact.getType())
//                    .append(" <b>Attack</b>: ").append(artifact.getAttack())
//                    .append(" <b>Defence</b>: ").append(artifact.getDefence())
//                    .append(" <b>HP</b>: ").append(artifact.getHp());
//        }
//        if (artifacts.size() == 0)
//            stringBuilder.append("none");

        stringBuilder
//                .append("</li>")
                .append("</ul>")
                .append("</html>");
        return stringBuilder.toString();
    }

    public Icon getIcon() {
        switch (classType) {
            case "ARCHER":
                return Util.archer;
            case "WARRIOR":
                return Util.warrior;
            case "WIZARD":
                return Util.wizard;
        }
        return null;
    }
}
