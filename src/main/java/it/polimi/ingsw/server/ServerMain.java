package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.abilities.decorators.AdditionalMove;
import it.polimi.ingsw.model.abilities.decorators.BuildBeforeMove;
import it.polimi.ingsw.model.abilities.decorators.BuildBelow;
import it.polimi.ingsw.server.socket.SocketServer;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ServerMain {

    private static final String COMMAND_STOP = "stop";

    private static final long SLEEP_PERIOD_MS = 10;

    private final IServer server;

    private boolean active;

    public ServerMain(String... args) {
        // TODO: Configuration loading
        // Read first from optional command line args, then from file, then from constants
        ServerConfiguration configuration = ServerConfiguration.fromFile(Path.of(""));

        active = true;
        System.out.println("Starting server");
        server = new SocketServer(configuration.getPort());
        System.out.println("Server started");

        // TODO: The initialization below serves as an example and will be replaced
        GameController controller = new GameController(server);
        controller.selectGame(
                List.of(new Player("A", 1),
                        new Player("B", 2),
                        new Player("C", 3)),
                new Deck(
                        List.of(
                                new God("G1", 1, "A", "A", "A", Map.of(BuildBeforeMove.class, false)),
                                new God("G2", 2, "A", "A", "A", Map.of(BuildBelow.class, false)),
                                new God("G3", 3, "A", "A", "A", Map.of(AdditionalMove.class, false))
                        )),
                false
        );

        System.out.println("Starting ticking");
        new Thread(this::loop, "ServerLoop").start();
        System.out.println("Ticking started");

        System.out.println("Starting listening");
        listenForInput();

        System.out.println("Goodbye!");
    }

    private void listenForInput() {
        System.out.println("Listening started");

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String input = scanner.next();

            if (input.equals(COMMAND_STOP)) {
                active = false;
                break;
            }

            System.out.println("Unknown command");
        }
    }

    private void loop() {
        while (active) {
            server.tick();

            try {
                Thread.sleep(SLEEP_PERIOD_MS);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }

        server.shutdown();

        System.exit(0);
    }

}
