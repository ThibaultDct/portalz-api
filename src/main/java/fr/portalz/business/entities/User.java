package fr.portalz.business.entities;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;

@Entity
@Table(name = "users")
@UserDefinition
public class User {
    
    @Id
    private UUID user_id;
    @Username
    private String username;
    @Password
    private String password;
    @Roles
    private String role;
    @Email(message = "Email should be valid")
    private String mail;
    private Date creation_date;

    public User(){}

    public User(UUID user_id, String username, String password, String role, String mail, Date creation_date){
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.mail = mail;
        this.creation_date = creation_date;
    }

    public UUID getUserId(){
        return user_id;
    }

    public void setUserId(UUID id){
        this.user_id = id;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = BcryptUtil.bcryptHash(password);
    }

    public String getRole(){
        return role;
    }

    public void setRole(String role){
        this.role = role;
    }

    public String getMail(){
        return mail;
    }

    public void setMail(String mail){
        this.mail = mail;
    }

    public Date getCreationDate(){
        return creation_date;
    }

    public void setCreationDate(Date creation_date){
        this.creation_date = creation_date;
    }

}
