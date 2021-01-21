package com.favourite.gamers.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.favourite.gamers.dao.SearchOperation;
import com.favourite.gamers.dao.UserSpecificationsBuilder;
import com.favourite.gamers.model.Game;
import com.favourite.gamers.model.Gamer;
import com.favourite.gamers.repository.GameRepository;
import com.favourite.gamers.repository.UserRepository;
import com.google.common.base.Joiner;

@Service
public class GameStoreServiceImpl implements GameStoreService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private GameRepository gameRepository;
	ModelMapper modelMapper = new ModelMapper();

	@Transactional
	@Override
	public Gamer createGamer(Gamer gamerReqObj) {
		Gamer gamer = userRepository.findByGamerName(gamerReqObj.getGamerName());
		if (null == gamer) {
			gamer = new Gamer();
			gamer.setGamerName(gamerReqObj.getGamerName());
			gamer.setGender(gamerReqObj.getGender());
			gamer.setGeography(gamerReqObj.getGeography());
			gamer.setNickName(gamerReqObj.getNickName());
		}
		gamerRegisterWithGame(gamer, gamerReqObj);
		return userRepository.save(gamer);
	}

	@Transactional
	@Override
	public Game createGame(Game game) {
		return gameRepository.save(game);
	}

	@Override
	public List<Gamer> getAllGamers() {
		return userRepository.findAll();
	}

	@Override
	public List<Game> getAllGames() {
		return gameRepository.findAll();
	}

	@Override
	public Gamer updateCredits(Gamer gamer, String gameName, String levelName, Long credit) {

		gamer.getGames().stream().forEach(gameObj -> {
			if (gameObj.getGameName().equals(gameName)) {
				if (gameObj.getGameLevel().getLevelName().equals(levelName)) {
					gameObj.getGameLevel().setCredits(credit);
				}
			}
		});
		return userRepository.save(gamer);
	}

	@Override
	public List<Gamer> search(String search) {
		UserSpecificationsBuilder builder = new UserSpecificationsBuilder();
		String operationSetExper = Joiner.on("|").join(SearchOperation.SIMPLE_OPERATION_SET);
		Pattern pattern = Pattern.compile("(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),");
		Matcher matcher = pattern.matcher(search + ",");
		while (matcher.find()) {
			builder.with(matcher.group(1), matcher.group(2), matcher.group(4), matcher.group(3), matcher.group(5));
		}

		Specification<Gamer> spec = builder.build();
		List<Gamer> gamers = userRepository.findAll(spec);
		return gamers;

	}

	private void gamerRegisterWithGame(Gamer gamer, Gamer gamerReqObj) {

		gamerReqObj.getGames().stream().forEach(gameReqObt -> {
			Game game = gameRepository.findByGameName(gameReqObt.getGameName());
			if (null == game) {
				game = gameReqObt;
			} else if (null != gameReqObt.getGameLevel()) {
				game.getGameLevel().setLevelName(gameReqObt.getGameLevel().getLevelName());
				game.getGameLevel().setCredits(gameReqObt.getGameLevel().getCredits());
			}
			gamer.addGame(game);

		});
	}

}
