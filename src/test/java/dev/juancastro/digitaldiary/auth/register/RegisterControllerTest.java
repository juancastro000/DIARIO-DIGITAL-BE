package dev.juancastro.digitaldiary.auth.register;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        UserDto userDto = new UserDto("testuser", "dGVzdHBhc3N3b3Jk", "test@example.com");


        Map<String, String> mockResponse = Map.of("message", "Success");
        when(mockService.save(userDto)).thenReturn(mockResponse);
        ResponseEntity<Map<String, String>> response = controller.register(userDto);


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().get("message"));
    }

}
