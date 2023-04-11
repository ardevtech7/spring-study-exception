package hello.exception.api;

import hello.exception.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ApiExceptionV2Controller {
    // 이 컨트롤러에서 예외가 발생하면,
    // 서블릿 컨테이너까지 올라가지 않고, illegalExHandler 가 잡아서 처리한다.

    // [순서] 컨트롤러에서 예외가 터지면 -> 디스패처 서블릿으로 전달 -> ExceptionHandlerExceptionResolver 호출
    // controller 에 @ExceptionHandler 가 있는지 확인하고, 호출
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ErrorResult illegalExHandler(IllegalArgumentException e) {
//        log.info("[exceptionHandler] ex", e);
//        // 정상 흐름(200, 400, 500 이든 응답 코드로 처리해서 리턴)으로 리턴
//        // 단, 응답 코드를 변경하고자 한다면 @ResponseStatus 추가
//        return new ErrorResult("BAD", e.getMessage());
//    }
//
////    @ExceptionHandler(UserException.class)
//    @ExceptionHandler
//    public ResponseEntity<ErrorResult> userExHandler(UserException e) {
//        log.error("[exceptionHandler] ex", e);
//        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
//        return new ResponseEntity(errorResult, HttpStatus.BAD_REQUEST);
//    }
//
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler
//    public ErrorResult exHandler(Exception e) {
//        log.error("[exceptionHandler] ex", e);
//        return new ErrorResult("EX", "내부 오류");
//    }
// --> ExControllerAdvice 이동

    @GetMapping("/api2/members/{id}")
    public MemberDto getMember(@PathVariable String id) {
        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자");
        }
        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }
        if (id.equals("user-ex")) {
            throw new UserException("사용자 오류");
        }
        return new MemberDto(id, "hello" + id);
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
        private String name;
    }
}
