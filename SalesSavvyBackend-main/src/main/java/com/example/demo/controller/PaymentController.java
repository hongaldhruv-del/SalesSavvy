package com.example.demo.controller;

import com.example.demo.entity.OrderItem;
import com.example.demo.entity.User;
import com.example.demo.service.PaymentService;
import com.razorpay.RazorpayException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:5174", allowCredentials = "true")
@RequestMapping("/api/payment")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    /**
     * Create Razorpay Order
     * @param requestBody Map containing totalAmount and cartItems
     * @param request HttpServletRequest for authenticated user
     * @return ResponseEntity with Razorpay Order ID
     */
    @PostMapping("/create")
    public ResponseEntity<String> createPaymentOrder(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) {
        try {
            logger.info("Received payment order creation request");
            
            // Fetch authenticated user
            User user = (User) request.getAttribute("authenticatedUser");
            if (user == null) {
                logger.warn("Unauthorized payment order creation attempt");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }

            // Validate request body
            if (requestBody == null || !requestBody.containsKey("totalAmount") || !requestBody.containsKey("cartItems")) {
                logger.error("Invalid request body: missing required fields");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request: missing totalAmount or cartItems");
            }

            // Extract totalAmount and cartItems from the request body
            Object totalAmountObj = requestBody.get("totalAmount");
            if (totalAmountObj == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Total amount is required");
            }
            BigDecimal totalAmount = new BigDecimal(totalAmountObj.toString());
            
            Object cartItemsObj = requestBody.get("cartItems");
            if (cartItemsObj == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cart items are required");
            }
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> cartItemsRaw = (List<Map<String, Object>>) cartItemsObj;

            if (cartItemsRaw == null || cartItemsRaw.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cart items cannot be empty");
            }

            // Convert cartItemsRaw to List<OrderItem>
            List<OrderItem> cartItems = cartItemsRaw.stream().map(item -> {
                OrderItem orderItem = new OrderItem();
                orderItem.setProductId((Integer) item.get("productId"));
                orderItem.setQuantity((Integer) item.get("quantity"));
                BigDecimal pricePerUnit = new BigDecimal(item.get("price").toString());
                orderItem.setPricePerUnit(pricePerUnit);
                orderItem.setTotalPrice(pricePerUnit.multiply(BigDecimal.valueOf((Integer) item.get("quantity"))));
                return orderItem;
            }).collect(Collectors.toList());

            logger.info("Creating Razorpay order for user {} with {} items", user.getUserId(), cartItems.size());

            // Call the payment service to create a Razorpay order
            String razorpayOrderId = paymentService.createOrder(user.getUserId(), totalAmount, cartItems);

            logger.info("Razorpay order created successfully: {}", razorpayOrderId);
            return ResponseEntity.ok(razorpayOrderId);
        } catch (RazorpayException e) {
            logger.error("Razorpay error while creating order: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error creating Razorpay order: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Invalid argument: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error while creating payment order: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error processing payment request: " + e.getMessage());
        }
    }

    /**
     * Verify Razorpay Payment
     * @param requestBody Map containing Razorpay payment details
     * @param request HttpServletRequest for authenticated user
     * @return ResponseEntity with success or failure message
     */
    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) {
        try {
            logger.info("Received payment verification request");
            
            // Fetch authenticated user
            User user = (User) request.getAttribute("authenticatedUser");
            if (user == null) {
                logger.warn("Unauthorized payment verification attempt");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }
            int userId = user.getUserId();
            
            // Validate request body
            if (requestBody == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request body is required");
            }
            
            // Extract Razorpay payment details from the request body
            String razorpayOrderId = (String) requestBody.get("razorpayOrderId");
            String razorpayPaymentId = (String) requestBody.get("razorpayPaymentId");
            String razorpaySignature = (String) requestBody.get("razorpaySignature");

            // Validate required fields
            if (razorpayOrderId == null || razorpayOrderId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Razorpay order ID is required");
            }
            if (razorpayPaymentId == null || razorpayPaymentId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Razorpay payment ID is required");
            }
            if (razorpaySignature == null || razorpaySignature.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Razorpay signature is required");
            }

            logger.info("Verifying payment for order: {}, payment: {}, user: {}", 
                razorpayOrderId, razorpayPaymentId, userId);

            // Call the payment service to verify the payment
            boolean isVerified = paymentService.verifyPayment(razorpayOrderId, razorpayPaymentId, razorpaySignature, userId);

            if (isVerified) {
                logger.info("Payment verified successfully for order: {}", razorpayOrderId);
                return ResponseEntity.ok("Payment verified successfully");
            } else {
                logger.warn("Payment verification failed for order: {}", razorpayOrderId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment verification failed");
            }
        } catch (Exception e) {
            logger.error("Error verifying payment: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error verifying payment: " + e.getMessage());
        }
    }
}
