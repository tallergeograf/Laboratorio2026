package uy.edu.taller.sige.geo_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import uy.edu.taller.sige.geo_api.model.Address;

import java.util.Collection;
import java.util.List;

@Repository
public interface AddressJpaRepository extends JpaRepository<Address, Integer> {

    @Query(value = """
    SELECT *
    FROM (
        SELECT *
        FROM vista_uruguay
        WHERE nombre_via IS NOT NULL
          AND num_puerta IS NOT NULL
          AND departamento IN ('MONTEVIDEO', 'CANELONES', 'MALDONADO')
        ORDER BY md5(punto_wkb)
        LIMIT 200
    ) urban

    UNION ALL

    SELECT *
    FROM (
        SELECT *
        FROM vista_uruguay
        WHERE nombre_via IS NOT NULL
          AND num_puerta IS NOT NULL
          AND departamento NOT IN ('MONTEVIDEO', 'CANELONES', 'MALDONADO')
        ORDER BY md5(punto_wkb)
        LIMIT 200
    ) other_departments
    """, nativeQuery = true)
    List<Address> findStreetNumber();

}