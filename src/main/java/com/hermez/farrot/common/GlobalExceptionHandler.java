package com.hermez.farrot.common;

import static org.springframework.http.HttpStatus.*;

import com.hermez.farrot.chat.chatmessage.exception.NoMatchUniqueReceiverException;
import com.hermez.farrot.common.dto.ErrorResult;
import com.hermez.farrot.member.exception.NotFoundMemberException;
import com.hermez.farrot.product.exception.ResourceNotFoundException;
import com.hermez.farrot.wishlist.exception.BadWishRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(ResourceNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/404";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        model.addAttribute("errorMessage", "알수 없는 에러 발생" + ex.getMessage());
        return "error/500";
    }

    @ExceptionHandler
    public String handleNoMatchUniqueReceiverException(NoMatchUniqueReceiverException ex,Model model){
        model.addAttribute("errorMessage", "알림을 받을 유저를 찾을 수 없습니다."+ ex.getMessage());
        return "error/500";
    }


    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler
    @ResponseBody
    public ErrorResult handleBadWishRequestException(BadWishRequestException ex) {
        return new ErrorResult("BAD_REQUEST", ex.getMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler
    @ResponseBody
    public ErrorResult handleNotFoundMemberException(NotFoundMemberException ex) {
        return new ErrorResult("BAD_REQUEST", ex.getMessage());
      
    @ExceptionHandler(ResponseStatusException.class)
    public String handleResponseStatusException(ResponseStatusException ex, Model model) {
        model.addAttribute("errorMessage", ex.getReason());
        return "error/error";

    }
}
