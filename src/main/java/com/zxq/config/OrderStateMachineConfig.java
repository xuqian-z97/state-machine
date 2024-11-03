package com.zxq.config;

import com.zxq.domain.Order;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.support.DefaultStateMachineContext;

import java.util.EnumSet;

/**
 * 状态机配置类
 */

@Configuration
@EnableStateMachine(name = "orderStateMachine")
public class OrderStateMachineConfig extends StateMachineConfigurerAdapter<OrderStatus, OrderEvents> {

    /**
     * 配置状态机可转换的状态
     * @param states
     * @throws Exception
     */
    @Override
    public void configure(StateMachineStateConfigurer<OrderStatus, OrderEvents> states) throws Exception {
        states
                .withStates()
                .initial(OrderStatus.INIT)
                .states(EnumSet.allOf(OrderStatus.class));
    }

    /**
     * 配置状态与转换事件的关系
     * @param transitions
     * @throws Exception
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStatus, OrderEvents> transitions) throws Exception {
        transitions
                //初始化 -> 付款 ： 付款
                .withExternal().source(OrderStatus.INIT).target(OrderStatus.PAYED)
                .event(OrderEvents.PAY)
                .and()
                //付款 -> 发货 ： 发货
                .withExternal().source(OrderStatus.PAYED).target(OrderStatus.SHIPPED)
                .event(OrderEvents.SHIPPING)
                .and()
                //发货 -> 收货 ： 收货
                .withExternal().source(OrderStatus.SHIPPED).target(OrderStatus.RECEIVED)
                .event(OrderEvents.RECEIVE);
    }

    @Bean
    public DefaultStateMachinePersister persister(){
        return new DefaultStateMachinePersister<>(new StateMachinePersist<Object, Object, Order>() {
            @Override
            public void write(StateMachineContext<Object, Object> stateMachineContext, Order order) throws Exception {
                //此处为实现持久化的方法实现
            }

            @Override
            public StateMachineContext<Object, Object> read(Order order) throws Exception {
                //此处为实现获取Order的状态，其实并没有进行持久化读取操作
                return new DefaultStateMachineContext<Object, Object>(order.getStatus(), null, null, null);
            }
        });
    }
}
