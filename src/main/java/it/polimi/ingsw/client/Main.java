package it.polimi.ingsw.client;

public class Main {

    public static void main(String... args) {

        boolean gui = false;

        FactoryPattern factoryPattern = new FactoryPattern(gui);
        Client client = new Client(factoryPattern);

        inputState(client);


    }

    private static void inputState(Client client) {
        client.setNameAndAge();
        System.out.println("Name: " + client.getName() + "\nAge: " + client.getAge());

        client.setServerData();
        System.out.println("IP address: " + client.getServerIP() + "\nPort: " + client.getServerPort());

        client.updateClientState();
    }

    private static void connectionState(Client client) {
        
    }

}
