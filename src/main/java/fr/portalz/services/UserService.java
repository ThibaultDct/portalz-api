package fr.portalz.services;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

import fr.portalz.business.entities.User;
import fr.portalz.business.repositories.UserRepository;
import io.quarkus.elytron.security.common.BcryptUtil;

@Singleton
public class UserService {

    @Inject
    UserRepository userRepository;
    
    /**
     * Get all users in database
     */
    public List<User> getAll() { 
        return userRepository.findAll().list();
    }

    /**
     * Get user with given id
     * @param id the user id
     */
    public User getByUserId(UUID id) { 
        return userRepository.findByUserId(id);
    }

    /**
     * Get user with given username
     * @param username the user username
     */
    public User getByUsername(String username) { 
        return userRepository.findByUsername(username);
    }

    /**
     * Adds a new user in the database
     * @param username the user name
     * @param password the unencrypted password (it will be encrypted with bcrypt)
     * @param role the comma-separated roles
     */
    @Transactional
    public User add(String username, String password, String role, String mail) { 
        User user = new User();
        user.setUserId(UUID.randomUUID());
        user.setUsername(username);
        user.setPassword(BcryptUtil.bcryptHash(password));
        user.setRole(role);
        user.setMail(mail);
        user.setCreationDate(Date.from(Instant.now()));
        userRepository.persistAndFlush(user);
        return user;
    }
    
}