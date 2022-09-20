package com.study.repository;

import com.study.entity.Diary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRepository extends CrudRepository<Diary, Integer> {
    Page<Diary> findByTitleContaining(String searchTitle, Pageable pageable);
}
