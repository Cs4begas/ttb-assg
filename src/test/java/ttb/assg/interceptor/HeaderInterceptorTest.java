package ttb.assg.interceptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import ttb.assg.common.ValidationException;
import ttb.assg.config.interceptor.HeaderInterceptor;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class HeaderInterceptorTest {


    @InjectMocks
    private HeaderInterceptor headerInterceptor;

    @Test
    public void givenValidHeaders_whenPreHandle_thenReturnTrue() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Content-Type", "application/json; charset=UTF-8");
        request.addHeader("service-name", "customer-service");
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean result = headerInterceptor.preHandle(request, response, null);

        assertTrue(result);
    }

    @Test
    public void givenInvalidContentType_whenPreHandle_thenThrowsException() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Content-Type", "application/xml");
        request.addHeader("service-name", "customer-service");
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertThrows(ValidationException.class, () -> headerInterceptor.preHandle(request, response, null));
    }

    @Test
    public void givenNullContentType_whenPreHandle_thenThrowsException() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();


        assertThrows(ValidationException.class, () -> headerInterceptor.preHandle(request, response, null));
    }

}
