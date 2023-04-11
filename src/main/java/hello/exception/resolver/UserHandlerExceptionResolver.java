package hello.exception.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.exception.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// UserException 을 HandlerResolver 에서 끝내도록 설정
// 예외는 먹어버리지만 서블릿 컨테이너까지 정상적 흐름으로 리턴된다.
@Slf4j
public class UserHandlerExceptionResolver implements HandlerExceptionResolver {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Object handler,
                                         Exception ex)
    {
        try {
            if (ex instanceof UserException) {
                log.info("UserException resolver to 400");
                // Accept 헤더를 꺼낸다.
                String acceptHeader = request.getHeader("accept");
                // Http 상태 코드 변경
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                // Accept 헤더에 따라서 분기 처리
                if ("application/json".equals(acceptHeader)) {
                    // 에러 결과 셋팅해서 json 으로 변경
                    Map<String, Object> errorResult = new HashMap<>();
                    errorResult.put("ex", ex.getClass());
                    errorResult.put("message", ex.getMessage());

                    // errorResult 객체를 문자로 변경
                    String result = objectMapper.writeValueAsString(errorResult);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                    response.getWriter().write(result);
                    return new ModelAndView();
                } else {
                    // 그외 TEXT/HTML 등
                    return new ModelAndView("error/500");
                }
            }
        } catch (IOException e) {
            log.error("resolver ex",e);
        }
        return null;
    }
}
