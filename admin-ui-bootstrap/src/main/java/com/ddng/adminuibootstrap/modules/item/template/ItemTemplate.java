package com.ddng.adminuibootstrap.modules.item.template;

import com.ddng.adminuibootstrap.modules.item.dto.ItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class ItemTemplate
{
    @Autowired
    private RestTemplate restTemplate;
    private static final String SALE_SERVER_NAME = "http://ddng-sale-api";

    public ItemDto getItem (Long id)
    {
        String apiPath = "/item/" + id;
        URI targetUrl= UriComponentsBuilder.fromUriString(SALE_SERVER_NAME)
                .path(apiPath)
                .build()
                .encode()
                .toUri();
        ItemDto forObject = restTemplate.getForObject(targetUrl, ItemDto.class);

        return forObject;
    }
}
