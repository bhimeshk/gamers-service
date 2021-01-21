package com.favourite.gamers.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import com.favourite.gamers.GamersServiceApplication;
import com.favourite.gamers.model.Game;
import com.favourite.gamers.model.GameLevel;
import com.favourite.gamers.model.Gamer;
import com.favourite.gamers.model.Geography;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GamersServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class GamersServiceApplicationTests {
	@LocalServerPort
	private int port;

	TestRestTemplate restTemplate = new TestRestTemplate();
	HttpHeaders headers = new HttpHeaders();

	@Test
	public void testGameStore() throws JSONException {

		// 1. Create a Game
		Game game = createGameObject();
		HttpEntity<Game> entity = new HttpEntity<Game>(game, headers);
		ResponseEntity<Game> postResponse = restTemplate.exchange(createURLWithPort("/gamestore/games"),
				HttpMethod.POST, entity, Game.class);
		assertNotNull(postResponse);
		assertEquals(postResponse.getStatusCode(), HttpStatus.CREATED);

		// 2. Get Register game
		testGetGames();

		// 3. Test to add gamer with register game
		testAddGamerWithFavouriteGame();

		// 4. Test to get Gamers Details
		testGetGamerDetails();

		// 5. Test to filter by game name
		testFilterByGameName();

		// 6. Test to Add credits to user based on game and his level
		testAddCredits();

		// 7. Test failure scenarios for adding credits to user, id user id not found
		testADDCreditsToUnknownGamer();

	}

	private void testGetGames() throws JSONException {
		HttpEntity<Game> entity = new HttpEntity<Game>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/gamestore/games"), HttpMethod.GET,
				entity, String.class);
		String expected = "[{\"id\":1,\"gameName\":\"Call of Duty\",\"gameLevel\":{\"id\":1,\"levelName\":\"pro\",\"credits\":20}}]";
		JSONAssert.assertEquals(expected, response.getBody(), true);

	}

	private void testAddGamerWithFavouriteGame() {
		Gamer gamer = createGamerObject();
		HttpEntity<Gamer> entity = new HttpEntity<Gamer>(gamer, headers);
		ResponseEntity<Gamer> postResponse = restTemplate.exchange(createURLWithPort("/gamestore/gamers"),
				HttpMethod.POST, entity, Gamer.class);
		assertNotNull(postResponse);
		assertEquals(postResponse.getStatusCode(), HttpStatus.CREATED);

	}

	private void testGetGamerDetails() throws JSONException {
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/gamestore/gamers"), HttpMethod.GET,
				entity, String.class);
		JSONAssert.assertEquals(getExptedResponseBody(), response.getBody(), true);
	}

	private void testFilterByGameName() throws JSONException {
		HttpEntity<Gamer> entity = new HttpEntity<Gamer>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/gamestore/favourite?search=gameName:Call of Duty"), HttpMethod.GET, entity,
				String.class);
		JSONAssert.assertEquals(getExptedResponseBody(), response.getBody(), true);
	}

	private void testAddCredits() throws JSONException {

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURLWithPort("/gamestore/credits"))
				.queryParam("gamerId", 1).queryParam("game", "Call of Duty").queryParam("level", "pro")
				.queryParam("credit", "35");

		HttpEntity<Gamer> entity = new HttpEntity<>(headers);
		ResponseEntity<Gamer> response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.PUT, entity,
				Gamer.class);
		assertNotNull(response);
		assertEquals(response.getStatusCode(), HttpStatus.OK);

	}

	private void testADDCreditsToUnknownGamer() throws JSONException {

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURLWithPort("/gamestore/credits"))
				.queryParam("gamerId", 2).queryParam("game", "Call of Duty").queryParam("level", "pro")
				.queryParam("credit", "35");

		HttpEntity<Gamer> entity = new HttpEntity<>(headers);
		ResponseEntity<Gamer> response = null;
		try {
			response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.PUT, entity,
					Gamer.class);
		} catch (RestClientException e) {
			
		}
		assertNotNull(response);
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);

	}

	private Gamer createGamerObject() {

		Gamer gamer = new Gamer();
		gamer.setGamerName("Stefan");
		gamer.setGender("Male");
		gamer.setNickName("Steef");

		Geography loc = new Geography();
		loc.setName("Europe");
		gamer.setGeography(loc);
		gamer.addGame(createGameObject());

		return gamer;
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

	private Game createGameObject() {
		Game game = new Game();
		game.setGameName("Call of Duty");
		GameLevel level = new GameLevel("pro", 20L);
		game.setGameLevel(level);
		return game;
	}

	private String getExptedResponseBody() {
		return "[{\"id\":1,\"gamerName\":\"Stefan\",\"nickName\":\"Steef\",\"gender\":\"Male\",\"geography\":{\"id\":1,\"name\":\"Europe\"},"
				+ "\"games\":[{\"id\":1,\"gameName\":\"Call of Duty\",\"gameLevel\":{\"id\":1,\"levelName\":\"pro\",\"credits\":20}}]}]";
	}

	private String getCreditExptectedResponse() {
		return "[{\"id\":1,\"gamerName\":\"Stefan\",\"nickName\":\"Steef\",\"gender\":\"Male\",\"geography\":{\"id\":1,\"name\":\"Europe\"},"
				+ "\"games\":[{\"id\":1,\"gameName\":\"Call of Duty\",\"gameLevel\":{\"id\":1,\"levelName\":\"pro\",\"credits\":35}}]}]";
	}

}
