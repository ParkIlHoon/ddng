package com.ddng.adminuibootstrap.infra.config;

import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <h1>Spring Cloud Feign 인코더 클래스</h1>
 */
public class PageableQueryEncoder implements Encoder
{
    private final Encoder delegate;

    public PageableQueryEncoder(Encoder delegate)
    {
        this.delegate = delegate;
    }

    @Override
    public void encode(Object o, Type type, RequestTemplate requestTemplate) throws EncodeException
    {
        if(o instanceof Pageable)
        {
            Pageable pageable = (Pageable) o;
            requestTemplate.query("page", String.valueOf(pageable.getPageNumber()));
            requestTemplate.query("size", String.valueOf(pageable.getPageSize()));

            if(pageable.getSort() != null)
            {
                Collection<String> existingSorts = requestTemplate.queries().get("sort");
                List<String> sortQueries = (existingSorts != null)? new ArrayList<>(existingSorts) : new ArrayList<>();
                for (Sort.Order order : pageable.getSort())
                {
                    sortQueries.add(order.getProperty() + "," + order.getDirection());
                }
                requestTemplate.query("sort", sortQueries);
            }
            else
            {
                delegate.encode(o, type, requestTemplate);
            }
        }
    }
}
