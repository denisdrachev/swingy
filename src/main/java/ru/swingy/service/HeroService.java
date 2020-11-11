package ru.swingy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import ru.swingy.model.Artifact;
import ru.swingy.model.Hero;
import ru.swingy.repositories.HeroRepository;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class HeroService {

    private final HeroRepository heroRepository;
    private Random random = new Random();

    public Hero createHero(String heroName, String classType) {
        return new Hero(heroName, classType);
    }

    public void gainExperienceAfterWinning(Hero hero) {

        Integer minExpForCurrentLevel = getLevelExperience(hero.getLevel());
        Integer maxExpForCurrentLevel = getLevelExperience(hero.getLevel() + 1);

        Integer deltaExp = maxExpForCurrentLevel - minExpForCurrentLevel;
        Integer additionExp = random.nextInt(deltaExp / 4) + deltaExp / 20;
        hero.setExperience(hero.getExperience() + additionExp);
        updateLevelIfNeeded(hero);
    }

    private void updateLevelIfNeeded(Hero hero) {
        Integer currentLevel = hero.getLevel();
        Integer experience = hero.getExperience();

        while (getLevelExperience(currentLevel + 1) <= experience) {
            currentLevel++;

            Integer attack = hero.getAttack();
            Integer defence = hero.getDefence();
            Integer hp = hero.getHp();

            Integer newAttack = hero.getAttack();
            Integer newDefence = hero.getDefence();
            Integer newHp = hero.getHp();

            if (attack >= 10)
                newAttack += random.nextInt(attack / 10) + (attack * 7) / 100;
            else
                newAttack += (attack * 7) / 100;
            if (defence >= 10)
                newDefence += random.nextInt(defence / 10) + (defence * 7) / 100;
            else
                newDefence += (defence * 7) / 100;
            if (hp > 10)
                newHp += random.nextInt(hp / 10) + (hp * 7) / 100;
            else
                newHp += (hp * 7) / 100;

            hero.setAttack(newAttack);
            hero.setDefence(newDefence);
            hero.setHp(newHp);

            Integer toHealth = hp / 10;
            if (hero.getCurrentHp() + toHealth > hero.getHp())
                hero.setCurrentHp(hero.getHp());
            else
                hero.setCurrentHp(hero.getCurrentHp() + toHealth);
        }
        hero.setLevel(currentLevel);
    }

    private Integer getLevelExperience(Integer level) {
        if (level == 0)
            return 0;
        return (level * 1000) + (level - 1) * (level - 1) * 450;
    }

    public List<Hero> getAllHeroes() {
        return heroRepository.findAll();
    }

    @Bean
    public String qqs() {
        Hero hero = createHero("hero name", "WARRIOR");
        Hero hero2 = createHero("hero name2", "ARCHER");
        Hero hero3 = createHero("hero name3", "ARC");
        Artifact artifact = new Artifact(0);
        Artifact artifact2 = new Artifact(0);
        hero.getArtifacts().add(artifact);
        hero.getArtifacts().add(artifact2);

//        heroRepository.save(hero);
//        heroRepository.save(hero2);


        Iterable<Hero> all = heroRepository.findAll();
        for (Hero hero1 : all) {
            System.err.println(hero1);
        }
        return "aa";
    }

    public void saveHero(Hero hero) {
        heroRepository.save(hero);
    }
}
