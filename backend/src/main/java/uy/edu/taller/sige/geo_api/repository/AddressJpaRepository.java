package uy.edu.taller.sige.geo_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uy.edu.taller.sige.geo_api.model.Address;
import java.util.List;

@Repository
public interface AddressJpaRepository extends JpaRepository<Address, Integer> {

    @Query(value = """
    SELECT *
    FROM (
        SELECT *
        FROM vista_uruguay
        WHERE nombre_via IS NOT NULL AND nombre_via ~ '^[A-ZÁÉÍÓÚ0-9 .''-]+$'
            AND num_puerta IS NOT NULL
            AND departamento IN ('MONTEVIDEO', 'CANELONES', 'MALDONADO')
            AND punto_wkb NOT IN (:ids)
        ORDER BY md5(punto_wkb)
        LIMIT 200
    ) urban

    UNION ALL
        
    SELECT *
    FROM (
        SELECT *
        FROM vista_uruguay
        WHERE nombre_via IS NOT NULL AND nombre_via ~ '^[A-ZÁÉÍÓÚ0-9 .''-]+$'
            AND num_puerta IS NOT NULL
            AND departamento NOT IN ('MONTEVIDEO', 'CANELONES', 'MALDONADO')
            AND punto_wkb NOT IN (:ids)
        ORDER BY md5(punto_wkb)
        LIMIT 200
    ) other_departments
    """, nativeQuery = true)
    List<Address> findStreetNumber(@Param("ids") List<String> pointWKB);
    @Query(value = """
    SELECT *
    FROM (
        SELECT *
        FROM vista_uruguay
        WHERE nombre_via IS NOT NULL AND nombre_via ~ '^[A-ZÁÉÍÓÚ0-9 .''-]+$'
            AND num_puerta IS NOT NULL 
            AND localidad IS NOT NULL AND localidad ~ '^[A-ZÁÉÍÓÚ0-9 .''-]+$'
            AND departamento IS NOT NULL AND departamento ~ '^[A-ZÁÉÍÓÚ0-9 .''-]+$'
            AND departamento IN ('MONTEVIDEO', 'CANELONES', 'MALDONADO')
            AND punto_wkb NOT IN (:ids)
        ORDER BY md5(punto_wkb)
        LIMIT 200
    ) urban
    UNION ALL
    SELECT *
    FROM (
        SELECT *
        FROM vista_uruguay
        WHERE nombre_via IS NOT NULL AND nombre_via ~ '^[A-ZÁÉÍÓÚ0-9 .''-]+$'
            AND num_puerta IS NOT NULL
            AND localidad IS NOT NULL AND localidad ~ '^[A-ZÁÉÍÓÚ0-9 .''-]+$'
            AND departamento IS NOT NULL AND departamento ~ '^[A-ZÁÉÍÓÚ0-9 .''-]+$'
            AND departamento NOT IN ('MONTEVIDEO', 'CANELONES', 'MALDONADO')
            AND punto_wkb NOT IN (:ids)
        ORDER BY md5(punto_wkb)
        LIMIT 200
    ) other_departments
    """, nativeQuery = true)
    List<Address> findStreetNumberDepartamentlocality(@Param("ids") List<String> pointWKB);
    @Query(value = """
    SELECT *
    FROM (
        SELECT *
        FROM vista_uruguay
        WHERE nombre_via IS NOT NULL AND nombre_via ~ '^[A-ZÁÉÍÓÚ0-9 .''-]+$'
            AND km IS NOT NULL AND km <>'N/A'
            AND departamento IN ('MONTEVIDEO', 'CANELONES', 'MALDONADO')
            AND punto_wkb NOT IN (:ids)
        ORDER BY md5(punto_wkb)
        LIMIT 200
    ) urban
    UNION ALL
    SELECT *
    FROM (
        SELECT *
        FROM vista_uruguay
        WHERE nombre_via IS NOT NULL AND nombre_via ~ '^[A-ZÁÉÍÓÚ0-9 .''-]+$'
            AND km IS NOT NULL AND km <>'N/A'
            AND departamento NOT IN ('MONTEVIDEO', 'CANELONES', 'MALDONADO')
            AND punto_wkb NOT IN (:ids)
        ORDER BY md5(punto_wkb)
        LIMIT 200
    ) other_departments
    """, nativeQuery = true)
    List<Address> findRouteKilometer(@Param("ids") List<String> pointWKB);
    @Query(value = """
    SELECT *
    FROM (
        SELECT *
        FROM vista_uruguay
        WHERE nombre_inmueble IS NOT NULL AND nombre_inmueble ~ '^[A-ZÁÉÍÓÚ0-9 .''-]+$'
            AND departamento IN ('MONTEVIDEO', 'CANELONES', 'MALDONADO')
            AND punto_wkb NOT IN (:ids)
        ORDER BY md5(punto_wkb)
        LIMIT 200
    ) urban
    UNION ALL
    SELECT *
    FROM (
        SELECT *
        FROM vista_uruguay
        WHERE nombre_inmueble IS NOT NULL AND nombre_inmueble ~ '^[A-ZÁÉÍÓÚ0-9 .''-]+$'
            AND departamento NOT IN ('MONTEVIDEO', 'CANELONES', 'MALDONADO')
            AND punto_wkb NOT IN (:ids)
        ORDER BY md5(punto_wkb)
        LIMIT 200
    ) other_departments
    """, nativeQuery = true)
    List<Address> findInterestPoint(@Param("ids") List<String> pointWKB);
    @Query(value = """
    SELECT *
    FROM (
        SELECT *
        FROM vista_uruguay
        WHERE nombre_via IS NOT NULL
            AND num_puerta IS NOT NULL
            AND localidad IS NOT NULL
            AND departamento IN ('MONTEVIDEO', 'CANELONES', 'MALDONADO')
            AND nombre_via ~ '^[A-ZÁÉÍÓÚ0-9 .''-]+$'
            AND localidad  ~ '^[A-ZÁÉÍÓÚ0-9 .''-]+$'
            AND punto_wkb NOT IN (:ids)
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
            AND localidad IS NOT NULL
            AND departamento NOT IN ('MONTEVIDEO', 'CANELONES', 'MALDONADO')
            AND nombre_via ~ '^[A-ZÁÉÍÓÚ0-9 .''-]+$'
            AND localidad  ~ '^[A-ZÁÉÍÓÚ0-9 .''-]+$'
            AND punto_wkb NOT IN (:ids)
        ORDER BY md5(punto_wkb)
        LIMIT 200
    ) other_departments
    """, nativeQuery = true)
    List<Address> findStreetNumberLocality(@Param("ids") List<String> pointWKB);

    @Query(value = """
    SELECT *
    FROM (
        SELECT *
        FROM vista_uruguay
        WHERE nombre_via IS NOT NULL
            AND num_puerta IS NOT NULL
            AND departamento IN ('MONTEVIDEO', 'CANELONES', 'MALDONADO')
            AND nombre_via ~ '^[A-ZÁÉÍÓÚ0-9 .''-]+$'
            AND departamento  ~ '^[A-ZÁÉÍÓÚ0-9 .''-]+$'
            AND punto_wkb NOT IN (:ids)
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
            AND departamento IS NOT NULL
            AND departamento NOT IN ('MONTEVIDEO', 'CANELONES', 'MALDONADO')
            AND nombre_via ~ '^[A-ZÁÉÍÓÚ0-9 .''-]+$'
            AND departamento  ~ '^[A-ZÁÉÍÓÚ0-9 .''-]+$'
            AND punto_wkb NOT IN (:ids)
        ORDER BY md5(punto_wkb)
        LIMIT 200
    ) other_departments
    """, nativeQuery = true)
    List<Address> findStreetNumberDepartament(@Param("ids") List<String> pointWKB);

    @Query(value = """
    SELECT *
    FROM (
        SELECT *
        FROM vista_uruguay
        WHERE solar IS NOT NULL
            AND manzana IS NOT NULL
            AND departamento IN ('MONTEVIDEO', 'CANELONES', 'MALDONADO')
            AND solar   ~ '^[A-ZÁÉÍÓÚ0-9 .''-]+$'
            AND manzana ~ '^[A-ZÁÉÍÓÚ0-9 .''-]+$'
            AND punto_wkb NOT IN (:ids)
        ORDER BY md5(punto_wkb)
        LIMIT 200
    ) urban

    UNION ALL

    SELECT *
    FROM (
        SELECT *
        FROM vista_uruguay
        WHERE solar IS NOT NULL AND solar <> 'N/A'
            AND manzana IS NOT NULL AND manzana <> 'N/A'
            AND departamento NOT IN ('MONTEVIDEO', 'CANELONES', 'MALDONADO')
            AND solar   ~ '^[A-ZÁÉÍÓÚ0-9 .''-]+$'
            AND manzana ~ '^[A-ZÁÉÍÓÚ0-9 .''-]+$'
            AND punto_wkb NOT IN (:ids)
        ORDER BY md5(punto_wkb)
        LIMIT 200
    ) other_departments
    """, nativeQuery = true)
    List<Address> findLotBlock(@Param("ids") List<String> pointWKB);

    @Query(value = """
    SELECT *
    FROM vista_uruguay
    WHERE punto_wkb IN (:ids)
    """, nativeQuery = true)
    List<Address> findByWkb(@Param("ids") List<String> pointWKB);
}