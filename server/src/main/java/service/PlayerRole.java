package service;

import chess.ChessGame;

public enum PlayerRole {
    WHITE,
    BLACK,
    OBSERVER;

    public ChessGame.TeamColor teamColor() {
        return switch (this) {
            case WHITE -> ChessGame.TeamColor.WHITE;
            case BLACK -> ChessGame.TeamColor.BLACK;
            case OBSERVER -> throw new IllegalStateException("Observers have no team color");
        };
    }
}