package com.favourite.gamers.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "Game")
public class Game implements Serializable {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "game_sequence")
    @SequenceGenerator(name = "game_sequence", sequenceName = "game_sequence")
    public Long id;
 
    @NotEmpty(message = "Please provide a name")
    private String gameName;
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    private GameLevel gameLevel;
 
    @ManyToMany(mappedBy = "games")
    @JsonIgnore
    public Set<Gamer> gamers = new HashSet<Gamer>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public GameLevel getGameLevel() {
		return gameLevel != null ? this.gameLevel : new GameLevel("noob",20L);
	}

	public void setGameLevel(GameLevel gameLevel) {
		this.gameLevel = gameLevel;
	}

	public Set<Gamer> getGamers() {
		return gamers;
	}

	public void setGamers(Set<Gamer> gamers) {
		this.gamers = gamers;
	}

	
    
}
