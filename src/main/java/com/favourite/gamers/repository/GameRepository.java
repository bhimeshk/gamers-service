package com.favourite.gamers.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.favourite.gamers.model.Game;

@RepositoryRestResource
public interface GameRepository extends JpaRepository<Game,Long>{

	public List<Game> findGameById(Long id);

	public Game findByGameName(String gameName);
	
	

}
