package com.ddng.adminuibootstrap.modules.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>RESTful-API 페이징 처리된 응답처리용</h1>
 * @param <T>
 */
public class RestPageImpl<T> extends PageImpl<T>
{
    @JsonCreator
    public RestPageImpl(@JsonProperty("content") List<T> content,
                        @JsonProperty("pageable") JsonNode pageable,
                        @JsonProperty("totalPages") Long totalPages,
                        @JsonProperty("totalElements") Long totalElements,
                        @JsonProperty("last") boolean last,
                        @JsonProperty("first") boolean first,
                        @JsonProperty("number") int number,
                        @JsonProperty("numberOfElements") Long numberOfElements,
                        @JsonProperty("size") int size,
                        @JsonProperty("sort") JsonNode sort,
                        @JsonProperty("empty") boolean empty)
    {
        super((content == null)? new ArrayList<T>() : content, PageRequest.of(number, size), totalElements);
    }

    public RestPageImpl(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public RestPageImpl(List<T> content) {
        super(content);
    }
    public RestPageImpl() {
        super(new ArrayList<T>());
    }
}
