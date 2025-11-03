package com.synerge.order101.storeorder;

import com.synerge.order101.order.model.entity.StoreOrder;
import com.synerge.order101.order.model.repository.StoreOrderRepository;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StoreOrderRepositoryTest {

    @Autowired
    StoreOrderRepository storeOrderRepository;

    @After("")
    public void cleanUp(){
        storeOrderRepository.deleteAll();
    }

    @Test
    public void testOrder(){

        StoreOrder storeOrderDto = new StoreOrder();

        storeOrderRepository.save(storeOrderDto);

        storeOrderRepository.findAll()

    }


}
