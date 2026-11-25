package service;

import model.GameData;

public record GameplayContext(String username, GameData gameData, PlayerRole role) {}