package ru.mooncess.onlinestore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mooncess.onlinestore.entity.*;
import ru.mooncess.onlinestore.repository.ArticleRepository;
import ru.mooncess.onlinestore.repository.OrderItemRepository;
import ru.mooncess.onlinestore.repository.OrderRepository;
import ru.mooncess.onlinestore.repository.UserRepository;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderStatusService orderStatusService;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ArticleService articleService;

    public Optional<Order> createOrder(String address, User user) {
        try {
            List<BasketItem> basketItemList = user.getBasketList();
            if (basketItemList.isEmpty()) {
                return Optional.empty();
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            Order order = new Order();
            order.setOrderNumber(generateOrderNumber());
            order.setAddress(address);
            order.setOrderDate(LocalDateTime.now().format(formatter));
            order.setStatus(orderStatusService.getOrderStatusById(1L).get());

            double total = 0;
            List<OrderItem> list = new ArrayList<>();
            HashMap<Long, Integer> quantityOfItem = new HashMap<>();

            for(int i = 0; i < basketItemList.size(); i++){
                OrderItem orderItem = new OrderItem();
                orderItem.setArticleId(basketItemList.get(i).getArticle().getId());
                orderItem.setPrice(basketItemList.get(i).getArticle().getActualPrice());

                if (basketItemList.get(i).getQuantity() <= articleService.getArticleById(orderItem.getArticleId()).get().getReserves()) {
                    orderItem.setQuantity(basketItemList.get(i).getQuantity());
                    quantityOfItem.put(orderItem.getArticleId(), orderItem.getQuantity());
                }
                else {
                    return Optional.empty();
                }

                orderItemRepository.save(orderItem);
                list.add(orderItem);

                total = total + (orderItem.getPrice() * orderItem.getQuantity());
            }

            if(user.getPersonalDiscount() != 0) order.setTotal(total * (user.getPersonalDiscount() / 100));
            else order.setTotal(total);

            order.setOrderItemList(list);
            order.setBuyer(user);

            user.getBasketList().clear();
            userRepository.save(user);

            order = orderRepository.save(order);

            quantityOfItem.forEach(articleService::updateArticleReserves);

            return Optional.of(order);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    public String generateOrderNumber() {
        int length = 8;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();

        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

    public List<Order> getAllOrder() {
        return orderRepository.findAllSortByDate();
    }
    public List<Order> getUserOrder(User user) {
        return orderRepository.findAllOrderByBuyerSortByDate(user);
    }
    public Optional<Order> getOrderByIdForAdmin(Long id) {
        return orderRepository.findById(id);
    }
    public Optional<List<Order>> getOrderByUserForAdmin(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(orderRepository::findAllOrderByBuyerSortByDate);
    }
    public Optional<Order> getOrderByIdAndUserId(Long id, User user) {
        return orderRepository.getByIdAndBuyer(id, user);
    }

    public Optional<Order> updateOrder(Long id, Long status) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            try {
                Order updatedOrder = optionalOrder.get();
                updatedOrder.setStatus(orderStatusService.getOrderStatusById(status).get());
                return Optional.of(orderRepository.save(updatedOrder));
            } catch (Exception e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    public boolean deleteOrder(Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            orderRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
