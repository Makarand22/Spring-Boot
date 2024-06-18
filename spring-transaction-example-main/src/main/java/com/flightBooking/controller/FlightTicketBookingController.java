package com.flightBooking.controller;

import com.flightBooking.dto.FlightBookingAcknowledgement;
import com.flightBooking.dto.FlightBookingRequest;
import com.flightBooking.service.FlightBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class FlightTicketBookingController {

    @Autowired
    private FlightBookingService service;


    @PostMapping("/bookFlightTicket")
    public FlightBookingAcknowledgement bookFlightTicket(@RequestBody FlightBookingRequest request){
        return service.bookFlightTicket(request);
    }
}
