package com.ticket.reservation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ticket.reservation.beans.User;
import com.ticket.reservation.controller.TicketController;

class TicketControllerTest {

    @InjectMocks
    TicketController ticketController;

    @Mock
    Map<String, User> users;

    @Mock
    Map<String, String> a;

    @Mock
    Map<String, String> b;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTicket() {
        User user = new User();
        user.setEmail("kalai@gmail.com");
        user.setFirstName("Kalaiselvan");
        user.setLastName("Neelkandan");

        ResponseEntity<String> responseEntity = ticketController.getTicket(user);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Receipt: From: London, To: France, User: Kalaiselvan Neelkandan, Price Paid: 20.0", responseEntity.getBody());
     }
    
    @Test
    public void testGetReceiptUserFound() {
    	User user = new User();
        user.setEmail("kalai@gmail.com");
        user.setFirstName("Kalaiselvan");
        user.setLastName("Neelkandan");

        ResponseEntity<String> responseEntity = ticketController.getTicket(user);    	
    	
        ResponseEntity<String> response = ticketController.getReceipt("kalai@gmail.com");      
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Receipt: From: London, To: France, User: Kalaiselvan Neelkandan, Price Paid: 20.0", response.getBody());
//        			  Receipt: From: London, To: France, User: Kalaiselvan Neelkandan, Price Paid: 20.0
    }

    @Test
    public void testGetReceiptUserNotFound() {
        ResponseEntity<String> response = ticketController.getReceipt("siva@gmail.com");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found.", response.getBody());
    }
    
    @Test
    public void testViewUsersBySectionA() {
    	a = new HashMap<String, String>();
        a.put("1", "kalai@gmail.com");
        a.put("2", "siva@gmail.com");
        
        ResponseEntity<Map<String, String>> response = ticketController.getUsersBySection("A");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testViewUsersBySectionB() {
        b.put("1", "vaishali@gmail.com");
        b.put("2", "kala@gmail.com");

        ResponseEntity<Map<String, String>> response = ticketController.getUsersBySection("B");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    @Test
    public void testViewUsersByBadRequest() {
        ResponseEntity<Map<String, String>> response = ticketController.getUsersBySection("C");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    
    @Test
    public void testRemoveUserSectionA() {
    	User user = new User();
        user.setEmail("kalai@gmail.com");
        user.setFirstName("Kalaiselvan");
        user.setLastName("Neelkandan");
        user.setSection("A");
        user.setSeatNumber("1");
       
        users.put("kalai@gmail.com", user);
        a.put("1", "kalai@gmail.com");

        ticketController.getTicket(user);
             
        ResponseEntity<String> response = ticketController.removeUser("kalai@gmail.com");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User removed from the train.", response.getBody());
    }

    @Test
    public void testRemoveUserSectionB() {
    	User user = new User();
        user.setEmail("kalai@gmail.com");
        user.setFirstName("Kalaiselvan");
        user.setLastName("Neelkandan");
        user.setSection("B");
        user.setSeatNumber("2");
        
        users.put("kalai@gmail.com", user);
        b.put("2", "kalai@gmail.com");
        
        ticketController.getTicket(user);
        ResponseEntity<String> response = ticketController.removeUser("kalai@gmail.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User removed from the train.", response.getBody());
    }

    @Test
    public void testRemoveUserNotFound() {
        ResponseEntity<String> response = ticketController.removeUser("none@gmail.com");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found.", response.getBody());
    }
    
    @Test
    public void testModifyUserSeat_UserFoundInSectionA() {
    	User user = new User();
        user.setEmail("kalai@gmail.com");
        user.setFirstName("Kalaiselvan");
        user.setLastName("Neelkandan");
        user.setSection("A");
        user.setSeatNumber("1");

        users.put("kalai@gmail.com", user);
        
        a.put("1", "kalai@gmail.com");
        ticketController.getTicket(user);   
        
        ResponseEntity<String> response = ticketController.modifyUserSeat("kalai@gmail.com", "B", "1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User's seat modified successfully.", response.getBody());
    }

    @Test
    public void testModifyUserSeat_UserNotFound() {
        ResponseEntity<String> response = ticketController.modifyUserSeat("nonet@gmail.com", "A", "1");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found.", response.getBody());
    }
}
