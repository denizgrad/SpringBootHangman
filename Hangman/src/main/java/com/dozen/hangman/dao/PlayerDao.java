package com.dozen.hangman.dao;

import java.util.List;

import com.dozen.hangman.model.Player;

public interface PlayerDao {

	Player getPlayer(int id);

	void deletePlayer(int id);

	List<Player> getPlayers();

	Player addPlayer(Player Player);

	void updatePlayer(Player Player);

	boolean isPlayerExists(String name);

}
