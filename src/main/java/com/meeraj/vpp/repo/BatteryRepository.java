package com.meeraj.vpp.repo;

import com.meeraj.vpp.entity.Battery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BatteryRepository extends JpaRepository<Battery, Long> {

    @Query("SELECT b FROM Battery b WHERE b.postcode BETWEEN :startCode AND :endCode" +
            " AND (:minWattCapacity is NULL OR b.wattCapacity >= :minWattCapacity) " +
            " AND (:maxWattCapacity is NULL OR b.wattCapacity <= :maxWattCapacity) " +
            " ORDER BY b.name"
    )
    List<Battery> findByPostcodeRange(@Param("startCode") String startCode, @Param("endCode") String endCode, @Param("minWattCapacity") Integer minWattCapacity, @Param("maxWattCapacity") Integer maxWattCapacity);
}
