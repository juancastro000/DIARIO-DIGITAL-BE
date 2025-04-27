package dev.juancastro.digitaldiary.auth.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ApiExceptionHandlerTest {

    private ApiExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new ApiExceptionHandler();
    }

    static class TestDto {
        private String field;
        public TestDto(String field) { this.field = field; }
        public String getField() { return field; }
    }

    static class DummyController {
        public void dummy(TestDto dto) {}
    }

    @Test
    void handleValidation_singleError() throws NoSuchMethodException {

        TestDto target = new TestDto("");
        BindingResult br = new BeanPropertyBindingResult(target, "testDto");
        br.addError(new FieldError("testDto", "field", "Field is required"));

        Method method = DummyController.class.getMethod("dummy", TestDto.class);
        MethodParameter param = new MethodParameter(method, 0);

        MethodArgumentNotValidException ex =
            new MethodArgumentNotValidException(param, br);

        Map<String,String> response = handler.handleValidation(ex);
        assertEquals(Map.of("error", "Field is required"), response);
    }

    @Test
    void handleValidation_multipleErrors() throws NoSuchMethodException {

        TestDto target = new TestDto("");
        BindingResult br = new BeanPropertyBindingResult(target, "testDto");
        br.addError(new FieldError("testDto", "field1", "Must not be blank"));
        br.addError(new FieldError("testDto", "field2", "Size must be ≥ 3"));

        Method method = DummyController.class.getMethod("dummy", TestDto.class);
        MethodParameter param = new MethodParameter(method, 0);
        MethodArgumentNotValidException ex =
            new MethodArgumentNotValidException(param, br);

        Map<String,String> response = handler.handleValidation(ex);
        String errors = response.get("error");
        assertTrue(errors.contains("Must not be blank"));
        assertTrue(errors.contains("Size must be ≥ 3"));

        assertEquals("Must not be blank; Size must be ≥ 3", errors);
    }

    @Test
    void handleDuplicateUserException() {
        DuplicateUserException dup = new DuplicateUserException("Username taken");
        Map<String,String> response = handler.handleDuplicate(dup);
        assertEquals(Map.of("error", "Username taken"), response);
    }

    @Test
    void handleRegistrationException() {
        RegistrationException reg = new RegistrationException("DB error", new RuntimeException());
        Map<String,String> response = handler.handleRegistration(reg);
        assertEquals(Map.of("error", "DB error"), response);
    }
}