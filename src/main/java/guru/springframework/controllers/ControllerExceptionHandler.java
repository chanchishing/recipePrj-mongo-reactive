package guru.springframework.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {


    //@ResponseStatus(HttpStatus.NOT_FOUND)
    //@ExceptionHandler(NotFoundException.class)
    //ModelAndView handleNotFoundError(Exception exception){
    //    ModelAndView modelAndView=new ModelAndView();
    //
    //    log.error("Handling not found exception");
    //
    //    modelAndView.setViewName("404error");
    //    modelAndView.addObject("exception", exception);
    //    modelAndView.setStatus(HttpStatus.NOT_FOUND);
    //
    //    return modelAndView;
    //}

    //@ResponseStatus(HttpStatus.BAD_REQUEST)
    //@ExceptionHandler(NumberFormatException.class)
    //ModelAndView handleNUmberFormatError(Exception exception){
    //    ModelAndView modelAndView=new ModelAndView();
    //
    //    log.error("Handling number format exception");
    //
    //    modelAndView.setViewName("400error");
    //    modelAndView.addObject("exception", exception);
    //    modelAndView.setStatus(HttpStatus.BAD_REQUEST);
    //
    //    return modelAndView;
    //}
}
