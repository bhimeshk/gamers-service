package com.favourite.gamers.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;


@Entity
@Table(name = "GAMER")
public class Gamer implements Serializable{
 
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gamer_sequence")
    @SequenceGenerator(name = "gamer_sequence", sequenceName = "gamer_sequence")
    private Long id;
 
    @NotEmpty(message = "Please provide a name")
	private String gamerName;
    
    private String nickName;
    
    private String gender;
    
    @ManyToOne (cascade = CascadeType.PERSIST)
    public Geography geography;
 
    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(name = "GAMER_GAME", joinColumns = { @JoinColumn(name = "GAMER_ID") }, inverseJoinColumns = {
            @JoinColumn(name = "GAME_ID") })
    public Set<Game> games = new HashSet<Game>();
    
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGamerName() {
		return gamerName;
	}

	public void setGamerName(String gamerName) {
		this.gamerName = gamerName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Geography getGeography() {
		return geography;
	}

	public void setGeography(Geography geography) {
		this.geography = geography;
	}

	public Set<Game> getGames() {
		return games;
	}

	public void setGames(Set<Game> games) {
		this.games = games;
	}

	public void addGame(Game game) {
        this.games.add(game);
        game.getGamers().add(this);
    }
 
    public void removeGame(Game game) {
        this.getGames().remove(game);
        game.getGamers().remove(this);
    }
 
    public void removeGames() {
        for (Game game : new HashSet<>(games)) {
        	removeGame(game);
        }
    }
}