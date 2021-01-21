package com.favourite.gamers.service;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.favourite.gamers.model.Game;
import com.favourite.gamers.model.Gamer;

public interface GameStoreService {

	public Gamer createGamer(Gamer gamer);

	public Game createGame(Game game);

	public List<Gamer> search(String spec);

	public List<Gamer> getAllGamers();

	public List<Game> getAllGames();

	public Gamer updateCredits(Gamer gamer, @NotEmpty String game, @NotEmpty String levelName, @NotNull Long credit);

}
