package com.ticket.reservation.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ticket.reservation.beans.User;

@RestController
@RequestMapping("/tickets")
public class TicketController {
	
	private static Map<String, User> users = new HashMap<>();
	
	private static Map<String, String> a = new HashMap<>(1);
	private static Map<String, String> b = new HashMap<>(1);
	
    private static double ticketPrice = 20.0;

    @PostMapping("/getTicket")
    public ResponseEntity<String> getTicket(@RequestBody User user) {
        users.put(user.getEmail(), user);
        assignSeat(user);
        return ResponseEntity.ok("Receipt: From: London, To: France, User: " + user.getFirstName() + " " + user.getLastName() + ", Price Paid: " + ticketPrice);
    }

    private void assignSeat(User user) {
    	String freeSection = a.size() <= b.size() ? "A" : "B"; 
        String seatNumber = Integer.toString((freeSection.equals("A") ? a.size() + 1 :b.size() + 1));
        if (freeSection.equals("A")) {
            a.put(seatNumber, user.getEmail());
        } else {
            b.put(seatNumber, user.getEmail());
        }
       user.setSection(freeSection);
       user.setSeatNumber(seatNumber);
    }

    @GetMapping("/receipt/{email}")
    public ResponseEntity<String> getReceipt(@PathVariable String email) {
        User user = users.get(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        return ResponseEntity.ok("Receipt: From: London, To: France, User: " + user.getFirstName() + " " + user.getLastName() + ", Price Paid: " + ticketPrice);
    }

    @GetMapping("/section/{section}")
    public ResponseEntity<Map<String, String>> getUsersBySection(@PathVariable String section) {
        Map<String, String> seats = new HashMap<>();
        if (section.equalsIgnoreCase("A")) {
            seats = a;
        } else if (section.equalsIgnoreCase("B")) {
            seats = b;
        } else {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(seats);
    }

    @DeleteMapping("/removeUser/{email}")
    public ResponseEntity<String> removeUser(@PathVariable String email) {
        User user = users.remove(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        String section = user.getSection();
        String seatNumber = user.getSeatNumber();
        if (section.equalsIgnoreCase("A")) {
            a.remove(seatNumber);
        } else {
            b.remove(seatNumber);
        }
        return ResponseEntity.ok("User removed from the train.");
    }

    @PutMapping("/updateSeat/{email}")
    public ResponseEntity<String> modifyUserSeat(@PathVariable String email, 
    		@RequestParam String newSection, @RequestParam String newSeatNumber) {
   
        User user = users.get(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        String oldSection = user.getSection();
        String oldSeatNumber = user.getSeatNumber();

        if (oldSection.equalsIgnoreCase("A")) {
            a.remove(oldSeatNumber);
        } else {
            b.remove(oldSeatNumber);
        }

        if (newSection.equalsIgnoreCase("A")) {
            a.put(newSeatNumber, email);
        } else {
            b.put(newSeatNumber, email);
        }

        user.setSection(newSection);
        user.setSeatNumber(newSeatNumber);

        return ResponseEntity.ok("User's seat modified successfully.");
    }
}
