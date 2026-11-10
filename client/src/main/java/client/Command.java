package client;

import java.util.Arrays;

public record Command(String name, String[] params) {
    public static Command parse(String input) {
        String[] tokens = input.trim().split("\\s+");
        return new Command(tokens[0].toLowerCase(), Arrays.copyOfRange(tokens, 1, tokens.length));
    }
}
