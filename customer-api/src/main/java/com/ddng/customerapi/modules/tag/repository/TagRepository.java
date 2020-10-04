package com.ddng.customerapi.modules.tag.repository;

import com.ddng.customerapi.modules.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long>
{
    Optional<Tag> findByTitle(String title);
}
