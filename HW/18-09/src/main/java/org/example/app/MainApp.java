package org.example.app;


import org.example.data.OrderApi;
import org.example.service.OrderService;
import io.reactivex.rxjava3.disposables.Disposable;

public class MainApp
{
    public static void main(String[] args) throws InterruptedException
    {
        OrderApi api = new OrderApi();
        OrderService service = new OrderService();

        Disposable subscription = service.processOrders(api.getOrderStream())
                .subscribe(
                        result -> System.out.println("> " + result),
                        error -> System.err.println("Ошибка: " + error),
                        () -> System.out.println("Поток завершен")
                );

        Thread.sleep(10000);
        subscription.dispose();
    }
}
