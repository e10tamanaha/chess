package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private final ChessPosition startPosition;
    private final ChessPosition endPosition;
    private final ChessPiece.PieceType promotionPiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    /**
     * @param o the object that is being compared with the current object
     * @return True if this and o are the same reference or the moves are equivalent, False if not
     */
    @Override 
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChessMove other = (ChessMove) o;

        return Objects.equals(this.startPosition, other.startPosition) &&
            Objects.equals(this.endPosition, other.endPosition) &&
            Objects.equals(this.promotionPiece, other.promotionPiece);
    }

    /**
     * @return int hash code calculating by hashing startPosition, endPosition, and promotionPiece
     */
    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, promotionPiece);
    }

    /**
     * @return String representation of chess move
     */
    @Override
    public String toString() {
        return "Start Position: " + startPosition + ", End Position: " + endPosition + ", Promotion Piece: " + promotionPiece;
    }
}