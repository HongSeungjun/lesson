package com.join.lesson.feature.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@RequestMapping("/reservation")
@Controller
public class ReservationController {

    private final ReservationUsecase reservationUsecase;

    @GetMapping
    public ModelAndView getReservation() {
        ModelAndView modelAndView = new ModelAndView("/member/calendar");
//        modelAndView.addObject("schedules", reservationUsecase.getReservation());
        return modelAndView;
    }

//    @GetMapping("/history")
//    public ModelAndView getReservationHistory() {
//
//        ModelAndView modelAndView = new ModelAndView("/member/reservation");
//        modelAndView.addObject("reservations", reservationUsecase.getReservationHistory(SecurityUtils.getUserId()));
//        return modelAndView;
//    }
}
