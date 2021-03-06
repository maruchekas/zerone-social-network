package com.skillbox.javapro21.repository;

import com.skillbox.javapro21.domain.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostFileRepository extends JpaRepository<PostFile, Integer> {


}
