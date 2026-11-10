package client;

public class ClientMain {
    public static void main(String[] args) {
        int port = args.length == 1 ? Integer.parseInt(args[0]) : 8080;
        new Repl(new ServerFacade(port)).run();
    }
}
