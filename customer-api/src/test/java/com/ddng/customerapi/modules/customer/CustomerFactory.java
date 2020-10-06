package com.ddng.customerapi.modules.customer;

import com.ddng.customerapi.modules.customer.domain.Customer;
import com.ddng.customerapi.modules.customer.domain.CustomerType;
import com.ddng.customerapi.modules.customer.repository.CustomerRepository;
import com.ddng.customerapi.modules.customer.service.CustomerService;
import com.ddng.customerapi.modules.tag.domain.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Random;

@Component
@Transactional
@RequiredArgsConstructor
public class CustomerFactory
{
    private final EntityManager entityManager;
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;

    public Customer createCustomer ()
    {
        Customer customer = new Customer();
        customer.setName(getRandomString(10));
        customer.setJoinDate(LocalDateTime.now());
        customer.setType(CustomerType.MALTESE);
        customer.setBigo("비고");
        customer.setTelNo("01012345678");

        return customerRepository.save(customer);
    }

    public Customer createCustomerWithTag (String tagTitle)
    {
        Customer customer = new Customer();
        customer.setName(getRandomString(10));
        customer.setJoinDate(LocalDateTime.now());
        customer.setType(CustomerType.YORKSHIRE_TERRIER);
        customer.setBigo("비고");
        customer.setTelNo("01012345678");

        Customer save = customerRepository.save(customer);

        Tag tag = new Tag();
        tag.setTitle(tagTitle);

        customerService.addTag(save, tag);

        entityManager.flush();

        return save;
    }

    public Customer createCustomerWithNameAndType (String name, CustomerType type)
    {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setJoinDate(LocalDateTime.now());
        customer.setType(type);
        customer.setBigo("비고");
        customer.setTelNo("01012345678");

        return customerRepository.save(customer);
    }

    public Customer createCustomerWithNameAndTypeAndTag (String name, CustomerType type, String tagTitle)
    {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setJoinDate(LocalDateTime.now());
        customer.setType(type);
        customer.setBigo("비고");
        customer.setTelNo("01012345678");

        Customer save = customerRepository.save(customer);

        Tag tag = new Tag();
        tag.setTitle(tagTitle);

        customerService.addTag(save, tag);

        entityManager.flush();

        return save;
    }

    private String getRandomString(int length)
    {
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();

        for(int idx = 0; idx < length; idx++)
        {
            int index = random.nextInt(3);
            switch (index)
            {
                case 0: stringBuffer.append((char) (random.nextInt(26) + 97)); break;
                case 1: stringBuffer.append((char) (random.nextInt(26) + 65)); break;
                case 2: stringBuffer.append((char) random.nextInt(10)); break;
            }
        }

        return stringBuffer.toString();
    }
}
