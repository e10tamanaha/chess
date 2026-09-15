package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Objects;
import java.util.Dictionary;
import java.util.Map;

import static chess.ChessGame.TeamColor.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Map<PieceType, int[][]> pieceTypeMap = Map.of(
            PieceType.ROOK, new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}},
            PieceType.BISHOP, new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}},
            PieceType.QUEEN, new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}},
            PieceType.KING, new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}},
            PieceType.KNIGHT, new int[][]{{2, 1}, {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}, {-1, -2}, {1, -2}, {2, -1}}
        );

        if (this.type == PieceType.ROOK || this.type == PieceType.BISHOP || this.type == PieceType.QUEEN) {
            return straightAndDiagonal(board, myPosition, pieceTypeMap.get(this.type));
        } else if (this.type == PieceType.KING || this.type == PieceType.KNIGHT) {
            return kingAndKnight(board, myPosition, pieceTypeMap.get(this.type));
        } else {
            return pawnMoves(board, myPosition);
        }
    }

    private Collection<ChessMove> straightAndDiagonal(ChessBoard board, ChessPosition myPosition, int[][] directions) {
        ChessPosition currentPosition;
        ChessPiece currentPiece;
        List<ChessMove> moves = new ArrayList<>();

        for (int[] dir : directions) {
            int row = myPosition.getRow() + dir[0];
            int col = myPosition.getColumn() + dir[1];

            while (row >= 1 && row <= 8 && col >= 1 && col <= 8) {
                currentPosition = new ChessPosition(row, col);
                currentPiece = board.getPiece(currentPosition);

                if (currentPiece != null) {
                    if (currentPiece.pieceColor != this.pieceColor) {
                        moves.add(new ChessMove(myPosition, currentPosition, null));
                    }
                    break;
                }

                moves.add(new ChessMove(myPosition, currentPosition, null));
                row += dir[0];
                col += dir[1];
            }
        }

        return moves;
    }

    private Collection<ChessMove> kingAndKnight(ChessBoard board, ChessPosition myPosition, int[][] directions) {
        int startingRow = myPosition.getRow();
        int startingCol = myPosition.getColumn();
        List<ChessMove> moves = new ArrayList<>();

        for (int[] dir : directions) {
            int currentRow = startingRow + dir[0];
            int currentCol = startingCol + dir[1];
            ChessPosition currentPosition = new ChessPosition(currentRow, currentCol);
            ChessPiece target;

            if (currentRow >= 1 && currentRow <= 8 && currentCol >= 1 && currentCol <= 8) {
                target = board.getPiece(currentPosition);
                if (target == null || target.getTeamColor() != this.getTeamColor()) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(currentRow, currentCol), null));
                }
            }
        }

        return moves;
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        int startingRow = myPosition.getRow();
        int startingCol = myPosition.getColumn();
        List<ChessMove> moves = new ArrayList<>();
        int direction = (pieceColor == ChessGame.TeamColor.WHITE) ? 1 : -1;
        int beforeMoving = (pieceColor == ChessGame.TeamColor.WHITE) ? 2 : 7;
        int nextRow = startingRow + direction;

        if (isOnBoard(nextRow, startingCol)) {
            ChessPosition oneAhead = new ChessPosition(nextRow, startingCol);
            if (board.getPiece(oneAhead) == null) {
                addPawnMove(moves, myPosition, oneAhead);

                ChessPosition twoAhead = new ChessPosition(startingRow + 2 * direction, startingCol);
                if (startingRow == beforeMoving && board.getPiece(twoAhead) == null) {
                    addPawnMove(moves, myPosition, twoAhead);
                }
            }
        }

        for (int offset : new int[]{1, -1}) {
            if (isOnBoard(nextRow, startingCol + offset)) {
                ChessPosition diagonalPosition = new ChessPosition(nextRow, startingCol + offset);
                ChessPiece diagonalPiece = board.getPiece(diagonalPosition);

                if(diagonalPiece != null && diagonalPiece.getTeamColor() != this.getTeamColor()) {
                    addPawnMove(moves, myPosition, diagonalPosition);
                }
            }
        }

        return moves;
    }

    private boolean isOnBoard(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }

    private void addPawnMove(List<ChessMove> moves, ChessPosition start, ChessPosition end) {
        if (end.getRow() == 8 || end.getRow() == 1) {
            for (PieceType promo : new PieceType[]{PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT}) {
                moves.add(new ChessMove(start, end, promo));
            }
        } else {
            moves.add(new ChessMove(start, end, null));
        }
    }

    @Override 
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChessPiece other = (ChessPiece) o;

        return this.pieceColor == other.pieceColor && this.type == other.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "Team: " + pieceColor + ", Type: " + type;
    }
}
