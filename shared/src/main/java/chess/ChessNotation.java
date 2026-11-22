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

    public static String formatPosition(ChessPosition position) {
        int row = position.getRow();
        int column = position.getColumn();
        char columnLetter = (char) ('a' + column - 1);

        return "" + columnLetter + row;
    }

    public static String formatMove(ChessMove move) {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece.PieceType promotionPiece = move.getPromotionPiece();
        String promotion = (promotionPiece != null) ? " (promoted to " + promotionPiece + ")" : "";

        return formatPosition(start) + " to " + formatPosition(end) + promotion;
    }
} 