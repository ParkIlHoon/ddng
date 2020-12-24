package com.ddng.scheduleapi.modules.tag.controller;

import com.ddng.scheduleapi.modules.tag.domain.Tag;
import com.ddng.scheduleapi.modules.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tags")
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
