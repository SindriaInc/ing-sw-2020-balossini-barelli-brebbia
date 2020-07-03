package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientApplication;
import it.polimi.ingsw.server.ServerApplication;

import java.util.Arrays;

/**
 * The entry point of the application
 *
 * The application must be launched by specifying whether to run the client or the server
 */
public class Application {

    private static final String ARGUMENT_CLIENT = "client";
    private static final String ARGUMENT_SERVER = "server";
    private static final String[] ARGUMENTS = {ARGUMENT_CLIENT, ARGUMENT_SERVER};

    public static void main(String... args) {
        if (args.length <= 0 || Arrays.stream(ARGUMENTS).noneMatch(argument -> argument.equals(args[0]))) {
            System.out.println("Specify what you want to run using one of these parameters: " + String.join(", ", ARGUMENTS));
            return;
        }

        String[] applicationArgs = Arrays.copyOfRange(args, 1, args.length);

        if (args[0].equals(ARGUMENT_CLIENT)) {
            ClientApplication.main(applicationArgs);
        } else if (args[0].equals(ARGUMENT_SERVER)) {
            ServerApplication.main(applicationArgs);
        }
    }

}
