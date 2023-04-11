package hello.exception;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;

// 서블릿 오류 페이지 등록
// ServletExController 에서 발생한 에러가 WAS 까지 전달되면서, WebServerCustomizer 에 등록한 에러페이지를 보고 적용된 에러 경로를 호출한다.
// @Component 를 주석 처리하면, 스프링 제공하는 오류 사용
//@Component
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    // 스프링 부트가 뜰 때, 등록해놓은 에러 페이지를 등록해준다.
    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        // 404, 500 상태 코드 에러 페이지 생성
        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404");
        ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");

        // RuntimeException 에러 페이지 생성
        // 자식 예외도 전부 해당
        ErrorPage errorPageEx = new ErrorPage(RuntimeException.class, "/error-page/500");

        factory.addErrorPages(errorPage404, errorPage500, errorPageEx);
    }
}
