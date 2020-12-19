package com.ddng.adminuibootstrap.modules.customer.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;


public class FamilyPageImpl<T> extends PageImpl<T>
{
    private static final long serialVersionUID = 1L;

    @JsonCreator
    public FamilyPageImpl(@JsonProperty("content") List<T> content,
                          @JsonProperty("pageable") JsonNode pageable,
                          @JsonProperty("totalPages") Long totalPages,
                          @JsonProperty("totalElements") Long totalElements,
                          @JsonProperty("last") boolean last,
                          @JsonProperty("first") boolean first,
                          @JsonProperty("number") int number,
                          @JsonProperty("numberOfElements") Long numberOfElements,
                          @JsonProperty("size") int size,
                          @JsonProperty("sort") JsonNode sort,
                          @JsonProperty("empty") boolean empty
                          )
    {
//        super(content, pageable, totalElements);
        super(content, PageRequest.of(number, size), totalElements);
    }

    public FamilyPageImpl(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public FamilyPageImpl(List<T> content) {
        super(content);
    }
    public FamilyPageImpl() {
        super(new ArrayList<T>());
    }
}
