package com.zxq.service;

import com.zxq.config.OrderEvents;
import com.zxq.config.OrderStatus;
import com.zxq.domain.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

@Component("orderStateListen")
@Slf4j
@WithStateMachine(name = "orderStateMachine")
public class OrderStateListener {

    @OnTransition(source = "INIT", target = "PAYED")
    public boolean pay(Message<OrderEvents> msg){
        Order order = (Order) msg.getHeaders().get("order");
        order.setStatus(OrderStatus.PAYED);
        log.info("----模拟操作开始----");
        log.info("订单表保存数据");
        log.info("发送站内消息");
        log.info("回调支付接口");
        log.info("同步至数据仓库");
        log.info("----模拟操作结束----");
        return true;
    }

    @OnTransition(source = "PAYED", target = "SHIPPED")
    public boolean shipping(Message<OrderEvents> msg){
        Order order = (Order) msg.getHeaders().get("order");
        order.setStatus(OrderStatus.SHIPPED);
        log.info("----模拟操作开始----");
        log.info("创建物流订单");
        log.info("更新物流订单状态");
        log.info("同步至数据仓库");
        log.info("----模拟操作结束----");
        return true;
    }

    @OnTransition(source = "SHIPPED", target = "RECEIVED")
    public boolean receive(Message<OrderEvents> msg){
        Order order = (Order) msg.getHeaders().get("order");
        order.setStatus(OrderStatus.PAYED);
        log.info("----模拟操作开始----");
        log.info("更新订单数据");
        log.info("同步至数据仓库");
        log.info("----模拟操作结束----");
        return true;
    }

}
