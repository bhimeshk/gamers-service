package com.favourite.gamers.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class GameLevel {
 
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    private String levelName = "noob";
 
    @NotNull(message = "Please provide credits value")
    private Long credits=20L;

    public GameLevel() {
		
	}
    
    public GameLevel(String levelName, Long credits) {
		this.levelName = levelName;
		this.credits = credits;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public Long getCredits() {
		return credits;
	}

	public void setCredits(Long credits) {
		this.credits = credits;
	}
	
	
    
}
