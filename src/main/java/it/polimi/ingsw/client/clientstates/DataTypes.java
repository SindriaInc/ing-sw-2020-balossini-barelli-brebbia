package it.polimi.ingsw.client.clientstates;

public class DataTypes {

    public static class GamerData {
        /**
         * The name
         */
        private String name;

        /**
         * The age
         */
        private int age;

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

}
