package ru.swingy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.swingy.controller.ViewController;
import ru.swingy.repositories.ArtifactRepository;
import ru.swingy.repositories.HeroRepository;

import javax.swing.*;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    HeroRepository heroRepository;

    @Autowired
    ArtifactRepository artifactRepository;

    @Autowired
    ViewController viewController;

    public static JFrame jFrame = new JFrame();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    @Override
    public void run(String... strings) throws Exception {
        if (strings.length > 1 || strings.length == 1 && !strings[0].equals("gui") && !strings[0].equals("-help")) {
            System.out.println("Incorrect input. For help use option -help");
            return;
        }
        if (strings.length == 1 && strings[0].equals("-help")) {
            printHelpInfo();
            return;
        }
        viewController.initJFrame(jFrame);
        viewController.initConsole();

        if (strings.length == 1 && strings[0].equals("gui"))
            viewController.activeJFrame();
        else
            viewController.activeConsole();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread t, Throwable e) {
            }
        });

        viewController.start();
    }

    private void printHelpInfo() {
        System.out.println("Options:");
        System.out.println("'-help': start information");
        System.out.println("'gui': start graphical interface mode");
        System.out.println("no args: start console mode");
        System.out.println();
    }
}
