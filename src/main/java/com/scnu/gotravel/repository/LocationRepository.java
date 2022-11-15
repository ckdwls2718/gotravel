package com.scnu.gotravel.repository;

import com.scnu.gotravel.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query("select l from Location l where l.parent.name = :name")
    Optional<List<Location>> findByParent(@Param("name") String name);

    @Query("select l from Location l where l.name = :name")
    Location findByName(@Param("name") String name);

    @Query("select l.name from Location l where l.id = :id")
    Optional<String> getLocationName(@Param("id") Long id);

    @Query("select l from Location l where l.parent.id is not null")
    List<Location> findAll1();

    @Query("select l from Location l where l.parent.id is null")
    List<Location> findAll2();
}
