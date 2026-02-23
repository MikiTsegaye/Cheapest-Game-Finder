package server;

public class ServerDriver {
    public static void main(String[] args) {
        Server server= new Server(5000);

        new Thread(server).start();
        System.out.println("Server driver started!");
    }
}
