package chess;

import java.util.Collection;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;

import static chess.ChessPiece.PieceType.*;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor teamTurn;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);

        if (piece == null) {
            return null;
        }

        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> valid = new ArrayList<ChessMove>();
        ChessPosition end;
        ChessPiece captured;

        for (ChessMove move : moves) {
            end = move.getEndPosition();
            captured = board.getPiece(end);

            board.addPiece(startPosition, null);
            board.addPiece(end, piece);

            if (!isInCheck(piece.getTeamColor())) {
                valid.add(move);
            }

            board.addPiece(startPosition, piece);
            board.addPiece(end, captured);
        }

        return valid;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        Collection<ChessMove> valid = validMoves(start);

        if (valid == null || !valid.contains(move)) {
            throw new InvalidMoveException("Invalid move: " + move);
        }

        ChessPiece piece = board.getPiece(start);
        TeamColor color = piece.getTeamColor();
        if (color != teamTurn) {
            throw new InvalidMoveException("Not " + color + "'s turn");
        }

        ChessPiece.PieceType type = move.getPromotionPiece();
        if (type != null) {
            piece = new ChessPiece(color, type);
        }

        board.addPiece(end, piece);
        board.addPiece(start, null);

        teamTurn = (teamTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        TeamColor enemy = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        ChessPosition kingPos = kingPosition(teamColor);
        List<ChessPosition> teamPos = teamPositions(enemy);
        Collection<ChessMove> moves;
        ChessPiece piece;

        for (ChessPosition pos : teamPos) {
            piece = board.getPiece(pos);
            moves = piece.pieceMoves(board, pos);

            for (ChessMove move : moves) {
                if (move.getEndPosition().equals(kingPos)) {
                    return true;
                }
            }
        }

        return false;
    }

    private ChessPosition kingPosition(TeamColor teamColor) {
        ChessPosition position;
        ChessPiece piece;

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                position = new ChessPosition(i, j);
                piece = board.getPiece(position);

                if (piece != null && piece.getPieceType() == KING && piece.getTeamColor() == teamColor) {
                    return position;
                }
            }
        }

        return null;
    }

    private List<ChessPosition> teamPositions(TeamColor teamColor) {
        List<ChessPosition> positions = new ArrayList<ChessPosition>();
        ChessPosition position;
        ChessPiece piece;

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                position = new ChessPosition(i, j);
                piece = board.getPiece(position);

                if (piece != null && piece.getTeamColor() == teamColor) {
                    positions.add(position);
                }
            }
        }

        return positions;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ChessPosition kingPos = kingPosition(teamColor);
        boolean check = isInCheck(teamColor);
        Collection<ChessMove> valid = validMoves(kingPos);
        
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    /**
     * @param o the object that is being compared with the current object
     * @return True if this and o are the same reference or their boards have equal contents and both chess games have the same team currently on their turn, False otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChessGame chessGame = (ChessGame) o;
        return board.equals(chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    /**
     * @return int hash code calculating by hashing the board and teamTurn
     */
    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
    }

    /**
     * @return String representation of board and teamTurn
     */
    @Override
    public String toString() {
        return "ChessGame{" +
                "board=" + board +
                ", teamTurn=" + teamTurn +
                '}';
    }
}
