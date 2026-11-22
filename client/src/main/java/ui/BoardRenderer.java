package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;
import java.util.Set;

public class BoardRenderer {
    private static final String LIGHT_SQUARE = SET_BG_COLOR_WHITE;
    private static final String DARK_SQUARE = SET_BG_COLOR_LIGHT_GREY;
    private static final String BORDER_BG = SET_BG_COLOR_DARK_GREY;
    private static final String BORDER_TEXT = SET_TEXT_COLOR_WHITE;
    private static final String WHITE_PIECE_TEXT = SET_TEXT_COLOR_RED;
    private static final String BLACK_PIECE_TEXT = SET_TEXT_COLOR_BLUE;
    private static final String SELECTED_SQUARE = SET_BG_COLOR_YELLOW;
    private static final String LEGAL_MOVE_ON_LIGHT_SQUARE = SET_BG_COLOR_PALE_GREEN;
    private static final String LEGAL_MOVE_ON_DARK_SQUARE = SET_BG_COLOR_GREEN;

    private BoardRenderer() {
    }

    public static String draw(ChessBoard board, ChessGame.TeamColor perspective) {
        return draw(board, perspective, null, Set.of());
    }

    public static String draw(ChessBoard board, ChessGame.TeamColor perspective, 
        ChessPosition selectedSquare, Set<ChessPosition> targetSquares) {
        boolean white = perspective != ChessGame.TeamColor.BLACK;
        StringBuilder sb = new StringBuilder();
        sb.append(fileLabels(white)).append("\n");
        for (int i = 0; i < 8; i++) {
            int row = white ? 8 - i : i + 1;
            sb.append(rankLine(board, row, white, selectedSquare, targetSquares)).append("\n");
        }
        sb.append(fileLabels(white));
        return sb.toString();
    }

    private static String fileLabels(boolean white) {
        StringBuilder sb = new StringBuilder();
        sb.append(BORDER_BG).append(BORDER_TEXT).append("   ");
        for (int j = 0; j < 8; j++) {
            int col = white ? j + 1 : 8 - j;
            char file = (char) ('a' + col - 1);
            sb.append(" ").append(file).append(" ");
        }
        sb.append("   ").append(RESET_BG_COLOR).append(RESET_TEXT_COLOR);
        return sb.toString();
    }

    private static String rankLine(ChessBoard board, int row, boolean white, ChessPosition selectedSquare, 
        Set<ChessPosition> targetSquares) {
        StringBuilder sb = new StringBuilder();
        sb.append(BORDER_BG).append(BORDER_TEXT).append(" ").append(row).append(" ");
        for (int j = 0; j < 8; j++) {
            int col = white ? j + 1 : 8 - j;
            ChessPosition position = new ChessPosition(row, col);
            ChessPiece piece = board.getPiece(position);
            sb.append(square(piece, position, selectedSquare, targetSquares));
        }
        sb.append(BORDER_BG).append(BORDER_TEXT).append(" ").append(row).append(" ");
        sb.append(RESET_BG_COLOR).append(RESET_TEXT_COLOR);
        return sb.toString();
    }

    private static String backgroundFor(ChessPosition position, ChessPosition selectedSquare, 
        Set<ChessPosition> targetSquares) {
        if (position.equals(selectedSquare)) {
            return SELECTED_SQUARE;
        }

        int row = position.getRow();
        int col = position.getColumn();
        boolean isLight = (row + col) % 2 == 1;

        if (targetSquares.contains(position)) {
            return (isLight) ? LEGAL_MOVE_ON_LIGHT_SQUARE : LEGAL_MOVE_ON_DARK_SQUARE;
        }

        return (isLight) ? LIGHT_SQUARE : DARK_SQUARE;
    }

    private static String square(ChessPiece piece, ChessPosition position, ChessPosition selectedSquare, 
        Set<ChessPosition> targetSquares) {
        String background = backgroundFor(position, selectedSquare, targetSquares);
        if (piece == null) {
            return background + EMPTY;
        }
        String textColor = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_PIECE_TEXT : BLACK_PIECE_TEXT;
        return background + textColor + symbol(piece);
    }

    private static String symbol(ChessPiece piece) {
        boolean isWhite = piece.getTeamColor() == ChessGame.TeamColor.WHITE;
        return switch (piece.getPieceType()) {
            case KING -> isWhite ? WHITE_KING : BLACK_KING;
            case QUEEN -> isWhite ? WHITE_QUEEN : BLACK_QUEEN;
            case BISHOP -> isWhite ? WHITE_BISHOP : BLACK_BISHOP;
            case KNIGHT -> isWhite ? WHITE_KNIGHT : BLACK_KNIGHT;
            case ROOK -> isWhite ? WHITE_ROOK : BLACK_ROOK;
            case PAWN -> isWhite ? WHITE_PAWN : BLACK_PAWN;
        };
    }
}
