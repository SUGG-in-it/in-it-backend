package com.example.initbackend.tag.repository;

import com.example.initbackend.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByTag(String tag);

    List<Tag> findAllByOrderByTag();
}
