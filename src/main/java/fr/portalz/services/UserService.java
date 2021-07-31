package fr.portalz.services;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.jboss.logging.Logger;
import org.wildfly.security.WildFlyElytronBaseProvider;
import org.wildfly.security.password.Password;
import org.wildfly.security.password.PasswordFactory;
import org.wildfly.security.password.WildFlyElytronPasswordProvider;
import org.wildfly.security.password.interfaces.BCryptPassword;
import org.wildfly.security.password.util.ModularCrypt;

import fr.portalz.business.dto.UserDTO;
import fr.portalz.business.entities.User;
import fr.portalz.business.repositories.UserRepository;

@Singleton
public class UserService {

    private static final Logger LOGGER = Logger.getLogger(UserService.class);

    @Inject
    UserRepository userRepository;
    
    /**
     * Get all users in database
     */
    public List<User> getAll() { 
        UserService.LOGGER.info("Returning users list");
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
     * Get user with given mail
     * @param mail the user mail
     */
    public User getByMail(String mail) { return userRepository.findByMail(mail); }

    /**
     * Update a user from id
     * @param id the user to update id
     * @param user the infos to update
     */
    @Transactional
    public User patchUserById(UUID id, User user){
        User toUpdate = userRepository.findByUserId(id);
        toUpdate.setUsername(user.getUsername());
        toUpdate.setPassword(user.getPassword());
        toUpdate.setRole(user.getRole());
        toUpdate.setMail(user.getMail());
        userRepository.persistAndFlush(toUpdate);
        return toUpdate;
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
        user.setPassword(password);
        user.setRole(role);
        user.setMail(mail);
        user.setCreationDate(Date.from(Instant.now()));
        userRepository.persistAndFlush(user);
        return user;
    }

    /**
     * Return the user if user exists with given username and matching password
     * @param dto the user to authenticate
     * @throws Exception
     */
    public User basicAuthentication(UserDTO dto) throws Exception {
        User toAuthenticate = userRepository.findByUsername(dto.getUsername());
        if (toAuthenticate == null) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
        try {
            if (!verifyPassword(dto.getPassword(), toAuthenticate.getPassword())){
                throw new WebApplicationException(Status.BAD_REQUEST);
            }
        } catch (Exception e) {
            throw e;
        }
        return toAuthenticate;
    }

    /**
     * Register a new user and save it to database
     * @param dto DTO with new user information
     * @return created user
     */
    @Transactional
    public User basicRegistration(UserDTO dto) {
        User toRegister = new User();
        if ((getByUsername(dto.getUsername()) != null) || (getByMail(dto.getMail()) != null)) {
            UserService.LOGGER.errorf("Impossible d'inscrire l'utilisteur {} ({}) : Un utilisateur avec ce nom d'utilisateur ou mail existe déjà.", dto.getUsername(), dto.getMail());
            throw new WebApplicationException(Status.BAD_REQUEST);
        } else {
            toRegister.setUserId(UUID.randomUUID());
            toRegister.setUsername(dto.getUsername());
            toRegister.setPassword(dto.getPassword());
            toRegister.setMail(dto.getMail());
            toRegister.setRole("user");
            toRegister.setCreationDate(Date.from(Instant.now()));
            userRepository.persistAndFlush(toRegister);
            UserService.LOGGER.infof("L'utilisateur {} ({}) a ete inscrit avec pour ID {}", toRegister.getUsername(), toRegister.getMail(), toRegister.getUserId());
        }
        return toRegister;
    }

    /**
     * Verify password with encrypted one
     * @param password the uncrypted password
     * @param encrypted_password the encrypted password
     */
    public boolean verifyPassword(String password, String encrypted_password) throws Exception {
        WildFlyElytronBaseProvider provider = new WildFlyElytronPasswordProvider();
        // 1. Create BCrypt password factory
        PasswordFactory passwordFactory = PasswordFactory.getInstance(BCryptPassword.ALGORITHM_BCRYPT, provider);
        // 2. Decode the hashed user password
        Password decoded_password = ModularCrypt.decode(encrypted_password);
        // 3. Translate the decoded user password object to one which is consumable by this factory
        Password restored_password = passwordFactory.translate(decoded_password);
        // 4. Verify existing password
        return passwordFactory.verify(restored_password, password.toCharArray());
    }
    
}