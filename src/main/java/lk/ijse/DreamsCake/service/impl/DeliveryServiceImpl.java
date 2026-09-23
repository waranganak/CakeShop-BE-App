package lk.ijse.DreamsCake.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.DreamsCake.dto.DeliveryDTO;
import lk.ijse.DreamsCake.dto.DeliveryDetailsResponseDTO;
import lk.ijse.DreamsCake.dto.OrderDetailDTO;
import lk.ijse.DreamsCake.entity.*;
import lk.ijse.DreamsCake.enums.OrderStatus;
import lk.ijse.DreamsCake.repository.DeliveryRepo;
import lk.ijse.DreamsCake.repository.CustomerOrderRepo;
import lk.ijse.DreamsCake.repository.RiderRepo;
import lk.ijse.DreamsCake.repository.UserRepo;
import lk.ijse.DreamsCake.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepo deliveryRepo;
    private final CustomerOrderRepo customerOrderRepo;
    private final RiderRepo riderRepo;
    private final UserRepo userRepo;

    @Override
    public void assignDelivery(DeliveryDTO dto) {
        CustomerOrder order = customerOrderRepo.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + dto.getOrderId()));

        Rider rider = riderRepo.findById(dto.getRiderId())
                .orElseThrow(() -> new RuntimeException("Rider not found with id: " + dto.getRiderId()));

        Delivery delivery = deliveryRepo.findByCustomerOrderId(dto.getOrderId())
                .orElse(null);

        if (delivery == null) {
            delivery = new Delivery();
            delivery.setCustomerOrder(order);
        }

        delivery.setRider(rider);

        if (dto.getDeliveryStatus() != null) {
            delivery.setDeliveryStatus(OrderStatus.valueOf(dto.getDeliveryStatus()));
        } else {
            if (delivery.getDeliveryStatus() == null) {
                delivery.setDeliveryStatus(OrderStatus.PROCESSING);
            }
        }

        deliveryRepo.save(delivery);
    }
    @Override
    public DeliveryDetailsResponseDTO getOrderByOrderId(Long orderId) {
        CustomerOrder order = customerOrderRepo.findById(orderId).orElse(null);

        if (order == null) {
            return null;
        }

        Delivery delivery = deliveryRepo.findByCustomerOrderId(orderId).orElse(null);

        Customer customer = order.getCustomer();

        List<OrderDetailDTO> orderDetailDTOs = Collections.emptyList();
        if (order.getOrderDetails() != null) {
            orderDetailDTOs = order.getOrderDetails().stream().map(detail -> {
                OrderDetailDTO detailDTO = new OrderDetailDTO();
                detailDTO.setQuantity(detail.getQuantity());
                detailDTO.setPrice(detail.getSubTotal());

                try {
                    detailDTO.setProductId(detail.getProduct() != null ? detail.getProduct().getId() : null);
                    detailDTO.setProductName(detail.getProduct() != null ? detail.getProduct().getProductName() : "Cake Item");
                } catch (Exception ignored) {
                    detailDTO.setProductId(null);
                    detailDTO.setProductName("Cake Item");
                }
                return detailDTO;
            }).collect(Collectors.toList());
        }

        DeliveryDetailsResponseDTO responseDTO = new DeliveryDetailsResponseDTO();

        if (delivery != null) {
            responseDTO.setDeliveryId(delivery.getId());
            responseDTO.setDeliveryStatus(delivery.getDeliveryStatus() != null ? delivery.getDeliveryStatus().name() : "PROCESSING");
            if (delivery.getRider() != null) {
                responseDTO.setRiderId(delivery.getRider().getId());
                responseDTO.setRiderName(delivery.getRider().getName());
            }
        } else {
            responseDTO.setDeliveryId(null);
            responseDTO.setDeliveryStatus("PROCESSING");
            responseDTO.setRiderName("Not Assigned");
        }

        responseDTO.setOrderId(order.getId());
        responseDTO.setOrderDate(order.getOrderDate());
        responseDTO.setTotalAmount(order.getTotalAmount() != null ? order.getTotalAmount() : 0.0);
        responseDTO.setOrderStatus(order.getStatus());

        if (order.getPayment() != null) {
            responseDTO.setPaymentMethod(String.valueOf(order.getPayment().getPaymentMethod()));
        } else {
            responseDTO.setPaymentMethod("COD");
        }

        if (customer != null) {
            responseDTO.setCustomerName(customer.getName());
            responseDTO.setCustomerPhone(customer.getPhone());
            responseDTO.setCustomerAddress(customer.getAddress());
        } else {
            responseDTO.setCustomerName("Walk-in Customer");
            responseDTO.setCustomerPhone("N/A");
            responseDTO.setCustomerAddress("N/A");
        }

        responseDTO.setOrderDetails(orderDetailDTOs);

        return responseDTO;
    }

    @Override
    public void updateDeliveryStatus(Long orderId, String status) {
        Delivery delivery = deliveryRepo.findByCustomerOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Delivery not found for Order ID: " + orderId));

        delivery.setDeliveryStatus(OrderStatus.valueOf(status));
        deliveryRepo.save(delivery);

        System.out.println("Delivery status updated to " + status + " for Order ID: " + orderId);
    }

    @Override
    public List<DeliveryDetailsResponseDTO> getDeliveriesByRiderId(Long id) {
        User user = userRepo.findById(id).orElse(null);

        Long actualRiderId = id;

        if (user != null) {
            String email = user.getUserName();

            Rider rider = riderRepo.findByEmail(email).orElse(null);
            if (rider != null) {
                actualRiderId = rider.getId();
            }
        }

        List<Delivery> deliveries = deliveryRepo.findByRider_Id(actualRiderId);

        if (deliveries.isEmpty()) {
            return Collections.emptyList();
        }

        return deliveries.stream().map(delivery -> {
            CustomerOrder order = delivery.getCustomerOrder();
            Customer customer = order != null ? order.getCustomer() : null;

            DeliveryDetailsResponseDTO responseDTO = new DeliveryDetailsResponseDTO();

            responseDTO.setDeliveryId(delivery.getId());
            responseDTO.setDeliveryStatus(delivery.getDeliveryStatus() != null ? delivery.getDeliveryStatus().name() : "PROCESSING");

            if (delivery.getRider() != null) {
                responseDTO.setRiderId(delivery.getRider().getId());
                responseDTO.setRiderName(delivery.getRider().getName());
            }

            if (order != null) {
                responseDTO.setOrderId(order.getId());
                responseDTO.setOrderDate(order.getOrderDate());
                responseDTO.setTotalAmount(order.getTotalAmount() != null ? order.getTotalAmount() : 0.0);
                responseDTO.setOrderStatus(order.getStatus());

                if (order.getPayment() != null) {
                    responseDTO.setPaymentMethod(String.valueOf(order.getPayment().getPaymentMethod()));
                } else {
                    responseDTO.setPaymentMethod("COD");
                }
            }

            if (customer != null) {
                responseDTO.setCustomerName(customer.getName());
                responseDTO.setCustomerPhone(customer.getPhone());
                responseDTO.setCustomerAddress(customer.getAddress());
            } else {
                responseDTO.setCustomerName("Walk-in Customer");
                responseDTO.setCustomerPhone("N/A");
                responseDTO.setCustomerAddress("N/A");
            }

            return responseDTO;
        }).collect(Collectors.toList());
    }
}