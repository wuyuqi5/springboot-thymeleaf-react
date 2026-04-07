package io.github.dutianze.springbootthymeleafreact.shared.config;

import io.github.dutianze.springbootthymeleafreact.shared.exception.BizException;
import io.github.dutianze.springbootthymeleafreact.shared.exception.ErrorCode;
import io.github.dutianze.springbootthymeleafreact.shared.log.ErrorResponse;
import io.github.dutianze.springbootthymeleafreact.shared.log.Monitor;
import io.github.dutianze.springbootthymeleafreact.shared.log.Severity;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxRequest;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxReswap;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BizException.class)
  public Object handleBizException(
      BizException ex,
      HttpServletRequest request,
      Model model,
      HtmxRequest htmxRequest,
      HtmxResponse htmxResponse
  ) {
    log.error(
        Monitor.builder()
            .errorCode(ex.getErrorCode())
            .severity(Severity.HIGH)
            .context("uri:{}, method:{}",
                request.getRequestURI(),
                request.getMethod())
            .build(),
        ex
    );

    String message = ex.getExternalMessage() != null
        ? ex.getExternalMessage()
        : ex.getErrorCode().getMessage();
    return handleError(
        ex.getErrorCode(),
        message,
        request,
        model,
        htmxRequest,
        htmxResponse
    );
  }

  @ExceptionHandler(AccessDeniedException.class)
  public Object handleAccessDeniedException(
      AccessDeniedException ex,
      HttpServletRequest request,
      Model model,
      HtmxRequest htmxRequest,
      HtmxResponse htmxResponse
  ) {
    log.warn(
        Monitor.builder()
            .errorCode(ErrorCode.AUTH_ACCESS_DENIED)
            .severity(Severity.LOW)
            .context("uri:{}, method:{}",
                request.getRequestURI(),
                request.getMethod())
            .build(),
        ex
    );

    return handleError(
        ErrorCode.AUTH_ACCESS_DENIED,
        request,
        model,
        htmxRequest,
        htmxResponse
    );
  }

  @ExceptionHandler(Exception.class)
  public Object handleException(
      Exception ex,
      HttpServletRequest request,
      Model model,
      HtmxRequest htmxRequest,
      HtmxResponse htmxResponse
  ) {
    log.error(
        Monitor.builder()
            .errorCode(ErrorCode.SYS_ERROR)
            .severity(Severity.HIGH)
            .context("uri:{}, method:{}",
                request.getRequestURI(),
                request.getMethod())
            .build(),
        ex
    );

    if (ex instanceof MethodArgumentNotValidException validationEx) {
      String message = validationEx.getBindingResult()
          .getFieldErrors()
          .stream()
          .findFirst()
          .map(DefaultMessageSourceResolvable::getDefaultMessage)
          .orElse("There is an error in the submitted input.");
      return handleError(
          ErrorCode.COMMON_ARGUMENT_INVALID,
          message,
          request,
          model,
          htmxRequest,
          htmxResponse
      );
    }

    return handleError(
        ErrorCode.SYS_ERROR,
        request,
        model,
        htmxRequest,
        htmxResponse
    );
  }

  private Object handleError(
      ErrorCode errorCode,
      HttpServletRequest request,
      Model model,
      HtmxRequest htmxRequest,
      HtmxResponse htmxResponse
  ) {
    return this.handleError(errorCode, errorCode.getMessage(), request, model, htmxRequest,
        htmxResponse);
  }

  private Object handleError(
      ErrorCode errorCode,
      String message,
      HttpServletRequest request,
      Model model,
      HtmxRequest htmxRequest,
      HtmxResponse htmxResponse
  ) {

    /* 1. htmx -> modal fragment */
    if (htmxRequest.isHtmxRequest()) {
      model.addAttribute("code", errorCode.getCode());
      model.addAttribute("message", message);

      htmxResponse.setRetarget("#modal-content");
      htmxResponse.setReswap(HtmxReswap.innerHtml());
      htmxResponse.addTrigger("open-modal");

      return "fragments/modal :: content";
    }

    /* 2. API / Ajax -> JSON */
    if (isApi(request)) {
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ErrorResponse(errorCode, message));
    }

    /* 3. Page -> error page */
    model.addAttribute("code", errorCode.getCode());
    model.addAttribute("message", message);
    return "error";
  }

  private boolean isApi(HttpServletRequest request) {
    String accept = request.getHeader("Accept");
    return accept != null && accept.contains("application/json");
  }
}
