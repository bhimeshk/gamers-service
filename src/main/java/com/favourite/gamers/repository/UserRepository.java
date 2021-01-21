package com.favourite.gamers.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.favourite.gamers.model.Gamer;

@RepositoryRestResource
public interface UserRepository extends JpaRepository<Gamer,Long>,JpaSpecificationExecutor<Gamer>{


	public Gamer findByGamerName(String gamerName);


}
