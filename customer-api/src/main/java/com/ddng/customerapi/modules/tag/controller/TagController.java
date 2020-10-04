package com.ddng.customerapi.modules.tag.controller;

import com.ddng.customerapi.modules.tag.domain.Tag;
import com.ddng.customerapi.modules.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
public class TagController
{
    private final TagRepository tagRepository;

    @GetMapping
    public ResponseEntity getTagList ()
    {
        List<Tag> tags = tagRepository.findAll();
        return ResponseEntity.ok(tags);
    }
}
