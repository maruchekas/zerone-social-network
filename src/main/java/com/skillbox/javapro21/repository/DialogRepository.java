package com.skillbox.javapro21.repository;

import com.skillbox.javapro21.domain.Dialog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DialogRepository extends JpaRepository<Dialog, Integer> {
    @Query("SELECT d FROM Dialog d " +
            "left join PersonToDialog p2d on p2d.dialogId = d.id " +
            "WHERE p2d.personId = :id " +
            "AND d.isBlocked = 0 ")
    List<Dialog> findDialogsListByPersonId(Long id);

    @Query("select d from Dialog d " +
            "where d.id = :id and d.isBlocked = 0")
    Optional<Dialog> findDialogById(int id);

    @Query("select d from Dialog d where d.code = :link")
    Dialog findByCode(String link);
}
