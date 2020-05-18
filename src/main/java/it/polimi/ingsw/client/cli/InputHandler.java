package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.common.logging.Logger;

import java.io.IOException;
import java.util.Scanner;
import java.util.function.Consumer;

public class InputHandler implements Runnable {

    private Consumer<String> reader;

    public InputHandler(Consumer<String> reader) {
        this.reader = reader;
    }

    @Override
    public void run() {

        Scanner in = new Scanner(System.in);

        while (in.hasNextLine()) {
            String line = in.nextLine();
            reader.accept(line);
        }
    }

}
