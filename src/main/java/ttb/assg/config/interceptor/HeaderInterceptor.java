package ttb.assg.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import ttb.assg.customer.constant.CustomerConstants;


@Component
public class HeaderInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String contentType = request.getHeader("Content-Type");
        String serviceName = request.getHeader("service-name");

        if (contentType == null ||!contentType.equals(CustomerConstants.APPLICATION_JSON_UTF8_VALUE)) {
            throw new RuntimeException("Invalid Content-Type");
        }
        return true;
    }
}