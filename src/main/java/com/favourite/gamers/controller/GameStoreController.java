package com.favourite.gamers.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.favourite.gamers.exception.GameServiceException;
import com.favourite.gamers.exception.ResourceNotFoundException;
import com.favourite.gamers.model.Game;
import com.favourite.gamers.model.Gamer;
import com.favourite.gamers.repository.UserRepository;
import com.favourite.gamers.service.GameStoreService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/gamestore")

public class GameStoreController {

	@Autowired
	private GameStoreService gameStoreService;

	@Autowired
	private UserRepository userRepository;

	ModelMapper modelMapper = new ModelMapper();

	@Operation(summary = "Create a new game")
	@PostMapping("/games")
	public ResponseEntity<Game> createGame(@Valid @RequestBody Game game) {
		Game gameObj = gameStoreService.createGame(game);
		return new ResponseEntity<>(gameObj, HttpStatus.CREATED);
	}

	@Operation(summary = "Create a new gamer with his gaming details")
	@PostMapping("/gamers")
	public ResponseEntity<Gamer> createGamer(@Valid @RequestBody Gamer gamer) {
		Gamer gamerObj = gameStoreService.createGamer(gamer);
		return new ResponseEntity<>(gamerObj, HttpStatus.CREATED);
	}

	@Operation(summary = "Retrieve the all information about Gamers")
	@GetMapping("/gamers")
	public ResponseEntity<List<Gamer>> getAllGamer() {
		List<Gamer> gamers = gameStoreService.getAllGamers();
		return new ResponseEntity<>(gamers, HttpStatus.OK);
	}

	@Operation(summary = "Retrieve the all information about Games")
	@GetMapping("/games")
	public ResponseEntity<List<Game>> getAllGames() {
		List<Game> games = gameStoreService.getAllGames();
		return new ResponseEntity<>(games, HttpStatus.OK);
	}

	@Operation(summary = "Retrieve favourite game based on search")
	@GetMapping("/favourite")
	public ResponseEntity<List<Gamer>> search(@NotEmpty @RequestParam(value = "search") String search) {

		List<Gamer> userDto = gameStoreService.search(search);
		return new ResponseEntity<>(userDto, HttpStatus.OK);

	}

	@Operation(summary = "Update the credits based on game and level name to perticular gamer")
	@PutMapping("/credits")
	public ResponseEntity<Gamer> addCredits(@RequestParam(value = "gamerId") @Min(1) Long gamerId,
			@RequestParam(value = "game") @NotEmpty String game,
			@RequestParam(value = "level") @NotEmpty String levelName,
			@RequestParam(value = "credit") @NotNull Long credit)
			throws ResourceNotFoundException, GameServiceException {

		Gamer gamer = userRepository.findById(gamerId)
				.orElseThrow(() -> new ResourceNotFoundException("Gamer not found for this id :: " + gamerId));

		gamer.getGames().stream().forEach(gameObj -> {
			if (!gameObj.getGameName().equals(game)) {
				throw new GameServiceException(" Game name " + game + " not found for this GamerId :: " + gamerId);
			} else if (!gameObj.getGameLevel().getLevelName().equals(levelName)) {
				throw new GameServiceException(
						" Game Level " + levelName + " not found for this GamerId :: " + gamerId);
			}
		});

		Gamer gamerRes = gameStoreService.updateCredits(gamer, game, levelName, credit);
		return new ResponseEntity<>(gamerRes, HttpStatus.OK);

	}

}
