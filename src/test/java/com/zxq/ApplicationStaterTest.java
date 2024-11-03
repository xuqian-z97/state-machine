package com.zxq;

import com.zxq.config.OrderEvents;
import com.zxq.config.OrderStatus;
import com.zxq.domain.Order;
import com.zxq.service.OrderProcessor;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ApplicationStaterTest {

    @Resource
    private OrderProcessor orderProcessor;

    @Test
    public void test() {
        Order order = new Order();
        order.setStatus(OrderStatus.INIT);
        Boolean process = orderProcessor.process(order, OrderEvents.PAY);
        System.out.println(process);
    }

}
