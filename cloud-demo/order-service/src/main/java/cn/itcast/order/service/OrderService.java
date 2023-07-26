package cn.itcast.order.service;

import cn.itcast.order.mapper.OrderMapper;
import cn.itcast.order.pojo.Order;
import cn.itcast.order.pojo.User;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RestTemplate restTemplate;

    //定义一个新的IRule
    @Bean
    public IRule randomRule(){
        return new RandomRule();
    }

    public Order queryOrderById(Long orderId) {
        // 1.查询订单
        Order order = orderMapper.findById(orderId);
        // 2.远程查询user
        // 2.1 url地址
        //String url="http://localhost:8081/user/"+order.getUserId();
        String url="http://userservice/user/"+order.getUserId();//修改访问的url路径，用服务名代替ip、端口
        // 2.2 发起调用
        User user=restTemplate.getForObject(url, User.class);
        // 3.存入order
        order.setUser(user);
        // 4.返回
        return order;
    }
}
