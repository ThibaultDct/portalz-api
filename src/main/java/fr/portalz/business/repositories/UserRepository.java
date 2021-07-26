package fr.portalz.business.repositories;

import java.util.UUID;

import javax.inject.Singleton;

import fr.portalz.business.entities.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@Singleton
public class UserRepository implements PanacheRepository<User>{
    
    public User findByUserId(UUID id){
        return find("user_id", id).firstResult();
    }

    public User findByUsername(String username){
        return find("username", username).firstResult();
    }

    public User findByMail(String mail) { return find("mail", mail).firstResult(); }

}
