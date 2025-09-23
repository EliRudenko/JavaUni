package org.example.service;


import org.example.model.Order;
import io.reactivex.rxjava3.core.Observable;

public class OrderService
{
    public Observable<String> processOrders(Observable<Order> orderStream)
    {
        return orderStream.map(order -> {
            if (order.getAmount() > 400)
                return "!!! VIP заказ: " + order;
            else { return "Обычный заказ: " + order; }
        });
    }
}
