package org.example.data;


import org.example.model.Order;
import io.reactivex.rxjava3.core.Observable;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class OrderApi
{
    private final Random random = new Random();

    public Observable<Order> getOrderStream()
    {
        return Observable.interval(1, TimeUnit.SECONDS)
                .map(tick -> new Order(tick.intValue(), 50 + random.nextInt(500)));
    }
}
