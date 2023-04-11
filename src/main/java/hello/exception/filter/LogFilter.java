package hello.exception.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

// (1) 클라이언트에서 error-ex 경로 호출
// (2) LogFilter 호출
@Slf4j
public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException
    {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        try {
            // DispatcherType
            log.info("REQUEST  [{}][{}][{}]", uuid, request.getDispatcherType(), requestURI);
            // (3) DispatcherServlet 지나서 ServletExController 호출
            // (5) WAS 에서 ErrorPageController - /error-page/404 경로 요청, DispatcherType:ERROR
            chain.doFilter(request, response);
        } catch (Exception e) {
            // (4)ServletExController 에서 익셉션 터지면 잡음
            log.info("EXCEPTION {}", e.getMessage());
            // WAS 까지 예외 올라감
            throw e;
        } finally {
            // finally 실행
            log.info("RESPONSE [{}][{}][{}]", uuid, request.getDispatcherType(), requestURI);
        }
    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}