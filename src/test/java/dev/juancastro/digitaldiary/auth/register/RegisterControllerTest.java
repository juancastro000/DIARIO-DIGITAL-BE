package dev.juancastro.digitaldiary.auth.register;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import dev.juancastro.digitaldiary.users.UserDto;

public class RegisterControllerTest {

    @Test
    public void testRegisterSuccess() {

        RegisterService mockService = mock(RegisterService.class);
        RegisterController controller = new RegisterController(mockService);
        UserDto userDto = new UserDto("testuser", "test@example.com", "dGVzdHBhc3N3b3Jk");

        doNothing().when(mockService).save(userDto);

        ResponseEntity<Map<String, String>> response = controller.registerUser(userDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Usuario creado con éxito", response.getBody().get("message"));

 
        verify(mockService, times(1)).save(userDto);
    }

}
