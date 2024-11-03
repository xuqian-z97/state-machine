package com.zxq.service;

import com.zxq.config.OrderEvents;
import com.zxq.config.OrderStatus;
import com.zxq.domain.Order;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
@WithStateMachine(name = "orderStateMachine")
public class OrderProcessor {

    @Resource
    private StateMachine<OrderStatus, OrderEvents> orderStateMachine;

    @Resource
    private StateMachinePersister<OrderStatus, OrderEvents, Order> persist;

    public Boolean process(Order order, OrderEvents orderEvents) {
        //定义状态改变时发送的消息
        Message<OrderEvents> msg = MessageBuilder
                .withPayload(orderEvents)
                .setHeader("order", order)
                .build();
        return sendEvent(msg);
    }

    @SneakyThrows
    private boolean sendEvent(Message<OrderEvents> msg) {
        Order order = (Order) msg.getHeaders().get("order");
        persist.restore(orderStateMachine, order);
        boolean result = orderStateMachine.sendEvent(msg);
        return result;
    }

}
