package com.springBatch.processor;

import com.springBatch.entity.Customer;
import org.springframework.batch.item.ItemProcessor;

public class CustomerProcessor implements ItemProcessor<Customer, Customer> {

    @Override
    public Customer process(Customer item) throws Exception {

        // Processing Logic

        return null;
    }
}
