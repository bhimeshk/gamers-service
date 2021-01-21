package com.favourite.gamers.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.json.JSONException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
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
import org.springframework.web.util.UriComponentsBuilder;

import com.favourite.gamers.GamersServiceApplication;
import com.favourite.gamers.model.Game;
import com.favourite.gamers.model.GameLevel;
import com.favourite.gamers.model.Gamer;
import com.favourite.gamers.model.Geography;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GamersServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GameStoreControllerIT {
	@LocalServerPort
	private int port;

	TestRestTemplate restTemplate = new TestRestTemplate();
	HttpHeaders headers = new HttpHeaders();

	@Test
	public void testA_createGame() {
		Game game = createGameObject();
		HttpEntity<Game> entity = new HttpEntity<Game>(game, headers);
		ResponseEntity<Game> postResponse = restTemplate.exchange(createURLWithPort("/gamestore/games"),
				HttpMethod.POST, entity, Game.class);
		assertNotNull(postResponse);
		assertEquals(postResponse.getStatusCode(), HttpStatus.CREATED);
	}

	@Test
	public void testB_getGame() throws JSONException {
		HttpEntity<Game> entity = new HttpEntity<Game>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/gamestore/games"), HttpMethod.GET,
				entity, String.class);
		String expected = "[{\"id\":1,\"gameName\":\"Call of Duty\",\"gameLevel\":{\"id\":1,\"levelName\":\"pro\",\"credits\":20}}]";
		JSONAssert.assertEquals(expected, response.getBody(), true);
	}

	@Test
	public void testC_addGamerWithFavouriteGame() {
		Gamer gamer = createGamerObject();
		HttpEntity<Gamer> entity = new HttpEntity<Gamer>(gamer, headers);
		ResponseEntity<Gamer> postResponse = restTemplate.exchange(createURLWithPort("/gamestore/gamers"),
				HttpMethod.POST, entity, Gamer.class);
		assertNotNull(postResponse);
		assertEquals(postResponse.getStatusCode(), HttpStatus.CREATED);
	}

	@Test
	public void testD_getGamerDetails() throws JSONException {
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/gamestore/gamers"), HttpMethod.GET,
				entity, String.class);
		JSONAssert.assertEquals(getExptedResponseBody(), response.getBody(), true);
	}

	@Test
	public void testE_searchByGameName() throws JSONException {
		HttpEntity<Gamer> entity = new HttpEntity<Gamer>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/gamestore/favourite?search=gameName:Call of Duty"), HttpMethod.GET, entity,
				String.class);
		JSONAssert.assertEquals(getExptedResponseBody(), response.getBody(), true);
	}

	@Test
	public void testF_addCredits() throws JSONException {

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURLWithPort("/gamestore/credits"))
				.queryParam("gamerId", 1).queryParam("game", "Call of Duty").queryParam("level", "pro")
				.queryParam("credit", "35");

		HttpEntity<Gamer> entity = new HttpEntity<>(headers);
		ResponseEntity<Gamer> response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.PUT, entity,
				Gamer.class);
		assertNotNull(response);
		assertEquals(response.getStatusCode(), HttpStatus.OK);

	}

	@Test
	public void testG_addCredits_UnknownGamer() throws JSONException {

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURLWithPort("/gamestore/credits"))
				.queryParam("gamerId", 2).queryParam("game", "Call of Duty").queryParam("level", "pro")
				.queryParam("credit", "35");

		HttpEntity<Gamer> entity = new HttpEntity<>(headers);
		ResponseEntity<Gamer> response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.PUT, entity,
				Gamer.class);
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
