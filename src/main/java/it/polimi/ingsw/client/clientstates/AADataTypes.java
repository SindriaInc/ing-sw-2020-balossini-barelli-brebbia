package it.polimi.ingsw.client.clientstates;

import it.polimi.ingsw.common.Coordinates;

import java.util.function.Predicate;

public class AADataTypes {

    public static class GamerData {
        /**
         * The name
         */
        private String name;

        /**
         * The age
         */
        private int age;

        public GamerData(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }


    public static class ConnectionData {
        /**
         * The IP
         */
        private String ip;

        /**
         * The port
         */
        private int port;

        public ConnectionData(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }

    public static class LobbyData {
        /**
         * The name
         * True if simple
         */
        private boolean type;

        /**
         * The age
         */
        private int playerNumber;

        public Boolean getType() {
            return type;
        }

        public void setType(Boolean type) {
            this.type = type;
        }

        public int getPlayerNumber() {
            return playerNumber;
        }

        public void setPlayerNumber(int playerNumber) {
            this.playerNumber = playerNumber;
        }
    }

    public static class Response {

        private boolean allow;
        private String message;

        public Response(boolean allow, String message) {
            this.allow = allow;
            this.message = message;
        }

    }

    public static class CellInfo {

        private final int x;
        private final int y;
        private int level;
        private boolean doomed;

        public CellInfo(int x, int y) {
            this.x = x;
            this.y = y;
            this.level = 0;
            this.doomed = false;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getLevel() {
            return level;
        }

        public boolean isDoomed() {
            return doomed;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public void setDoomed(boolean doomed) {
            this.doomed = doomed;
        }

    }

    public static class WorkerInfo {

        private final int id;
        private final String owner;
        private Coordinates position;

        public WorkerInfo(int id, String owner, Coordinates position) {
            this.id = id;
            this.owner = owner;
            this.position = position;
        }

        public int getId() {
            return id;
        }

        public String getOwner() {
            return owner;
        }

        public Coordinates getPosition() {
            return position;
        }

        public void setPosition(Coordinates position) {
            this.position = position;
        }

    }

    public static class Command {

        private final String label;

        private final String argsSyntax;

        private final Predicate<String[]> executor;

        public Command(String label, String argsSyntax, Predicate<String[]> executor) {
            this.label = label;
            this.argsSyntax = argsSyntax;
            this.executor = executor;
        }

        public String getLabel() {
            return label;
        }

        public String getSyntax() {
            return label + (argsSyntax != null ? ":" + argsSyntax : "");
        }

        public Predicate<String[]> getExecutor() {
            return executor;
        }

    }

    public enum InGame {
        IN_GAME,
        SPECTATOR,
        ENDED
    }

}
