package com.hermez.farrot.common;

import com.hermez.farrot.chat.chatmessage.exception.NoMatchUniqueReceiverException;
import com.hermez.farrot.product.exception.ResourceNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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

    @ExceptionHandler(NoMatchUniqueReceiverException.class)
    public String handleNoMatchUniqueReceiverException(NoMatchUniqueReceiverException ex,Model model){
        model.addAttribute("errorMessage", "알림을 받을 유저를 찾을 수 없습니다."+ ex.getMessage());
        return "error/500";
    }
}
