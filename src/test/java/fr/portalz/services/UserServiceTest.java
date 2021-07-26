package fr.portalz.services;

import fr.portalz.business.entities.User;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
public class UserServiceTest {

    @Inject
    UserService userService;

    @Test
    public void testVerifyPasswordOk() throws Exception {
        // given
        User user = new User();
        user.setPassword("password");
        // then
        boolean result = userService.verifyPassword("password", user.getPassword());
        // finally
        Assertions.assertTrue(result);
    }

    @Test
    public void testVerifyPasswordKo() throws Exception {
        // given
        User user = new User();
        user.setPassword("password");
        // then
        boolean result = userService.verifyPassword("not_password", user.getPassword());
        // finally
        Assertions.assertFalse(result);
    }

    @Test
    void getAll() {
    }

    @Test
    void getByUserId() {
    }

    @Test
    void getByUsername() {
    }

    @Test
    void getByMail() {
    }

    @Test
    void patchUserById() {
    }

    @Test
    void add() {
    }

    @Test
    void basicAuthentication() {
    }

    @Test
    void verifyPassword() {
    }
}
