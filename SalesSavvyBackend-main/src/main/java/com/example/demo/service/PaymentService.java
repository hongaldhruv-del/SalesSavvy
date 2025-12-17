package com.example.demo.service;

import com.example.demo.entity.CartItem;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.OrderStatus;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Value("${razorpay.key_id}")
    private String razorpayKeyId;

    @Value("${razorpay.key_secret}")
    private String razorpayKeySecret;

    private RazorpayClient razorpayClient;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;

    public PaymentService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
    }

    @PostConstruct
    public void initializeRazorpayClient() {
        try {
            if (razorpayKeyId == null || razorpayKeyId.isEmpty()) {
                logger.error("Razorpay Key ID is not configured!");
                throw new IllegalStateException("Razorpay Key ID is missing in application.properties");
            }
            if (razorpayKeySecret == null || razorpayKeySecret.isEmpty()) {
                logger.error("Razorpay Key Secret is not configured!");
                throw new IllegalStateException("Razorpay Key Secret is missing in application.properties");
            }
            
            this.razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
            logger.info("Razorpay client initialized successfully with Key ID: {}", razorpayKeyId.substring(0, Math.min(10, razorpayKeyId.length())) + "...");
        } catch (RazorpayException e) {
            logger.error("Failed to initialize Razorpay client: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize Razorpay payment gateway", e);
        }
    }

    @Transactional
    public String createOrder(int userId, BigDecimal totalAmount, List<OrderItem> cartItems) throws RazorpayException {
        try {
            // Validate inputs
            if (totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Total amount must be greater than zero");
            }
            if (cartItems == null || cartItems.isEmpty()) {
                throw new IllegalArgumentException("Cart items cannot be empty");
            }
            if (razorpayClient == null) {
                throw new IllegalStateException("Razorpay client is not initialized");
            }

            logger.info("Creating Razorpay order for user {} with amount: {}", userId, totalAmount);

            // Prepare Razorpay order request
            JSONObject orderRequest = new JSONObject();
            int amountInPaise = totalAmount.multiply(BigDecimal.valueOf(100)).intValue();
            orderRequest.put("amount", amountInPaise); // Amount in paise
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "txn_" + System.currentTimeMillis());

            logger.debug("Razorpay order request: {}", orderRequest.toString());

            // Create Razorpay order
            com.razorpay.Order razorpayOrder = razorpayClient.orders.create(orderRequest);
            String orderId = razorpayOrder.get("id");
            
            logger.info("Razorpay order created successfully with ID: {}", orderId);

            // Save order details in the database
            Order order = new Order();
            order.setOrderId(orderId);
            order.setUserId(userId);
            order.setTotalAmount(totalAmount);
            order.setStatus(OrderStatus.PENDING);
            order.setCreatedAt(LocalDateTime.now());
            orderRepository.save(order);

            logger.info("Order saved to database with ID: {}", orderId);
            return orderId;
        } catch (RazorpayException e) {
            logger.error("Razorpay error while creating order for user {}: {}", userId, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while creating order for user {}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Failed to create payment order: " + e.getMessage(), e);
        }
    }

    @Transactional
    public boolean verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature, int userId) {
        try {
            // Validate inputs
            if (razorpayOrderId == null || razorpayOrderId.isEmpty()) {
                logger.error("Razorpay order ID is missing");
                return false;
            }
            if (razorpayPaymentId == null || razorpayPaymentId.isEmpty()) {
                logger.error("Razorpay payment ID is missing");
                return false;
            }
            if (razorpaySignature == null || razorpaySignature.isEmpty()) {
                logger.error("Razorpay signature is missing");
                return false;
            }
            if (razorpayKeySecret == null || razorpayKeySecret.isEmpty()) {
                logger.error("Razorpay key secret is not configured");
                return false;
            }

            logger.info("Verifying payment for order: {}, payment: {}, user: {}", 
                razorpayOrderId, razorpayPaymentId, userId);

            // Prepare signature validation attributes
            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", razorpayOrderId);
            attributes.put("razorpay_payment_id", razorpayPaymentId);
            attributes.put("razorpay_signature", razorpaySignature);

            // Verify Razorpay signature
            boolean isSignatureValid = com.razorpay.Utils.verifyPaymentSignature(attributes, razorpayKeySecret);

            if (isSignatureValid) {
                logger.info("Payment signature verified successfully for order: {}", razorpayOrderId);

                // Update order status to SUCCESS
                Order order = orderRepository.findById(razorpayOrderId)
                    .orElseThrow(() -> {
                        logger.error("Order not found in database: {}", razorpayOrderId);
                        return new RuntimeException("Order not found: " + razorpayOrderId);
                    });

                // Check if order already processed
                if (order.getStatus() == OrderStatus.SUCCESS) {
                    logger.warn("Order {} already processed, skipping duplicate payment", razorpayOrderId);
                    return true;
                }

                order.setStatus(OrderStatus.SUCCESS);
                order.setUpdatedAt(LocalDateTime.now());
                orderRepository.save(order);

                // Fetch cart items for the user
                List<CartItem> cartItems = cartRepository.findCartItemsWithProductDetails(userId);

                if (cartItems == null || cartItems.isEmpty()) {
                    logger.warn("No cart items found for user {} during payment verification", userId);
                } else {
                    // Save order items
                    for (CartItem cartItem : cartItems) {
                        OrderItem orderItem = new OrderItem();
                        orderItem.setOrder(order);
                        orderItem.setProductId(cartItem.getProduct().getProductId());
                        orderItem.setQuantity(cartItem.getQuantity());
                        orderItem.setPricePerUnit(cartItem.getProduct().getPrice());
                        orderItem.setTotalPrice(cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
                        orderItemRepository.save(orderItem);
                    }
                    logger.info("Saved {} order items for order: {}", cartItems.size(), razorpayOrderId);
                }

                // Clear user's cart
                cartRepository.deleteAllCartItemsByUserId(userId);
                logger.info("Cleared cart for user: {}", userId);

                logger.info("Payment verified and order processed successfully for order: {}", razorpayOrderId);
                return true;
            } else {
                logger.error("Payment signature verification failed for order: {}", razorpayOrderId);
                return false;
            }
        } catch (Exception e) {
            logger.error("Error verifying payment for order {}: {}", razorpayOrderId, e.getMessage(), e);
            return false;
        }
    }

    @Transactional
    public void saveOrderItems(String orderId, List<OrderItem> items) {
        if (orderId == null || orderId.isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be null or empty");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order items cannot be null or empty");
        }
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        for (OrderItem item : items) {
            item.setOrder(order);
            orderItemRepository.save(item);
        }
    }
}
