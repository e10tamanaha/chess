package chess;

public class ChessNotation {
    private ChessNotation() {}

    public static ChessPosition parsePosition(String square) {
        if (square == null || square.length() != 2) {
            throw new IllegalArgumentException("Invalid square: " + square);
        }

        String normalized = square.toLowerCase();

        char columnLetter = normalized.charAt(0);
        char rowDigit = normalized.charAt(1);

        if (columnLetter < 'a' || columnLetter > 'h' || rowDigit < '1' || rowDigit > '8') {
            throw new IllegalArgumentException("Invalid square: " + square);
        }

        int column = columnLetter - 'a' + 1;
        int row = rowDigit - '0';

        return new ChessPosition(row, column);
    }
}