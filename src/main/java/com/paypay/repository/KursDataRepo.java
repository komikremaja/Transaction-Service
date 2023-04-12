package com.paypay.repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.paypay.model.KursData;

@Repository
public interface KursDataRepo extends JpaRepository<KursData, BigDecimal>{
    // @Query(value = "SELECT DISTINCT created_date from kurs_data order by 1 desc", nativeQuery = true)
    // List<Timestamp> findNewKurs();

    @Query("SELECT DISTINCT createdDate FROM KursData s WHERE s.idBank=:idBank  order by 1 desc")
    List<LocalDateTime> findNewKurs(@Param("idBank")BigDecimal idBank);
}
