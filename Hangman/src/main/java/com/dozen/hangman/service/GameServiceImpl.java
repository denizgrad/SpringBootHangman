package com.dozen.hangman.service;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.dozen.hangman.dao.GameDao;
import com.dozen.hangman.dao.PlayerDao;
import com.dozen.hangman.model.Game;
import com.dozen.hangman.model.GameStatus;
import com.dozen.hangman.model.Guess;
import com.dozen.hangman.model.Player;
import com.dozen.hangman.utility.exception.GameOverExc;
import com.dozen.hangman.utility.exception.GuessAlreadyMadeExc;
import com.dozen.hangman.utility.exception.HangmanException;
import com.dozen.hangman.utility.exception.NoResultExc;
/**
 * 
 * @author deniz.ozen
 *
 */
@Service
@Transactional
public class GameServiceImpl implements GameService{

	public static int maximumGuess = 6;
	@Autowired
	WordService wordService;
	@Autowired
	GameDao gameDao;
	@Autowired
	PlayerDao playerDao;
	@Resource
	private Environment env;
	
	@Override
	public List<Game> getGames() {
		List<Game> games = gameDao.getGames();
		games.forEach(g->g.populateStatusAndJsonFields());
		return games;
	}

	@Override
	public Game createGame(Player player) {
		Player playerDb = playerDao.getPlayer(player.getId());
		if(playerDb == null) {
			throw new NoResultExc("player");
		}
		Game newGame = new Game();
		newGame.setPlayerObj(playerDb);
		newGame.setGameStatus(GameStatus.ongoing);
		newGame.setWord(wordService.getRandomWord());
		newGame.setGuessesLeft(maximumGuess);
		gameDao.addGame(newGame);
		newGame.populateStatusAndJsonFields();
		return newGame;
	}

	@Override
	public Game getGame(Integer id) {
		Game game = gameDao.getGame(id);
		if(game == null) {
			throw new NoResultExc();
		}
		game.populateStatusAndJsonFields();
		return game;
	}

	/**
	 * all guess logic
	 */
	@Override
	public Game makeGuess(Integer gameId, Guess guess) {

		Game game = gameDao.getGame(gameId);
		if(game == null) {
			throw new NoResultExc("game");
		}
		game.populateStatusAndJsonFields();

		guess.setLetter(guess.getLetter().toLowerCase());
		
		if(game.getGameStatus() == GameStatus.won) {
			throw new GameOverExc(HangmanException.GAME_WON);
		}
		if(isGuessMadeBefore(game.getGuessList(), (guess))){
			throw new GuessAlreadyMadeExc();
		}
		if(game.getGameStatus() == GameStatus.lost) {
			throw new GameOverExc(HangmanException.GAME_LOST);
		}
		if(!game.getWord().contains(guess.getLetter())){
			game.setGuessesLeft(game.getGuessesLeft() - 1);
		}
		
		game.getGuessList().add(guess);
		guess.setGame(game);
		
		game.populateStatusAndJsonFields();
		gameDao.updateGame(game);
		return game;
	}

	private boolean isGuessMadeBefore(Set<Guess> guessList, Guess guess) {
		for (Guess guessDb : guessList) {
			if(guessDb.equals(guess)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Game deleteGame(Integer id) {
		Game game = gameDao.getGame(id);
		gameDao.deleteGame(id);
		return game;
	}
	
	
}
