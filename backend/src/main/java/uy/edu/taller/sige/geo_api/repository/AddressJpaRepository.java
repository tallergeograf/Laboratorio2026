package uy.edu.taller.sige.geo_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
        WHERE nombre_via IS NOT NULL AND nombre_via ~ '^[A-ZÁÉÍÓÚÑ0-9 .''-]+$'
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
        WHERE nombre_via IS NOT NULL AND nombre_via ~ '^[A-ZÁÉÍÓÚÑ0-9 .''-]+$'
            AND num_puerta IS NOT NULL
            AND departamento NOT IN ('MONTEVIDEO', 'CANELONES', 'MALDONADO')
        ORDER BY md5(punto_wkb)
        LIMIT 200
    ) other_departments
    """, nativeQuery = true)
    List<Address> findStreetNumber();
    @Query(value = """
    SELECT *
    FROM (
        SELECT *
        FROM vista_uruguay
        WHERE nombre_via IS NOT NULL AND nombre_via ~ '^[A-ZÁÉÍÓÚÑ0-9 .''-]+$'
            AND num_puerta IS NOT NULL 
            AND localidad IS NOT NULL AND localidad ~ '^[A-ZÁÉÍÓÚÑ0-9 .''-]+$'
            AND departamento IS NOT NULL AND departamento ~ '^[A-ZÁÉÍÓÚÑ0-9 .''-]+$'
            AND departamento IN ('MONTEVIDEO', 'CANELONES', 'MALDONADO')
        ORDER BY md5(punto_wkb)
        LIMIT 200
    ) urban
    UNION ALL
    SELECT *
    FROM (
        SELECT *
        FROM vista_uruguay
        WHERE nombre_via IS NOT NULL AND nombre_via ~ '^[A-ZÁÉÍÓÚÑ0-9 .''-]+$'
            AND num_puerta IS NOT NULL
            AND localidad IS NOT NULL AND localidad ~ '^[A-ZÁÉÍÓÚÑ0-9 .''-]+$'
            AND departamento IS NOT NULL AND departamento ~ '^[A-ZÁÉÍÓÚÑ0-9 .''-]+$'
            AND departamento NOT IN ('MONTEVIDEO', 'CANELONES', 'MALDONADO')
        ORDER BY md5(punto_wkb)
        LIMIT 200
    ) other_departments
    """, nativeQuery = true)
    List<Address> findStreetNumberDepartamentlocality();
    @Query(value = """
    SELECT *
    FROM (
        SELECT *
        FROM vista_uruguay
        WHERE nombre_via IS NOT NULL AND nombre_via ~ '^[A-ZÁÉÍÓÚÑ0-9 .''-]+$'
            AND km IS NOT NULL AND km <>'N/A'
            AND departamento IN ('MONTEVIDEO', 'CANELONES', 'MALDONADO')
        ORDER BY md5(punto_wkb)
        LIMIT 200
    ) urban
    UNION ALL
    SELECT *
    FROM (
        SELECT *
        FROM vista_uruguay
        WHERE nombre_via IS NOT NULL AND nombre_via ~ '^[A-ZÁÉÍÓÚÑ0-9 .''-]+$'
            AND km IS NOT NULL AND km <>'N/A'
            AND departamento NOT IN ('MONTEVIDEO', 'CANELONES', 'MALDONADO')
        ORDER BY md5(punto_wkb)
        LIMIT 200
    ) other_departments
    """, nativeQuery = true)
    List<Address> findRouteKilometer();
    @Query(value = """
    SELECT *
    FROM (
        SELECT *
        FROM vista_uruguay
        WHERE nombre_inmueble IS NOT NULL AND nombre_inmueble ~ '^[A-ZÁÉÍÓÚÑ0-9 .''-]+$'
            AND departamento IN ('MONTEVIDEO', 'CANELONES', 'MALDONADO')
        ORDER BY md5(punto_wkb)
        LIMIT 200
    ) urban
    UNION ALL
    SELECT *
    FROM (
        SELECT *
        FROM vista_uruguay
        WHERE nombre_inmueble IS NOT NULL AND nombre_inmueble ~ '^[A-ZÁÉÍÓÚÑ0-9 .''-]+$'
            AND departamento NOT IN ('MONTEVIDEO', 'CANELONES', 'MALDONADO')
        ORDER BY md5(punto_wkb)
        LIMIT 200
    ) other_departments
    """, nativeQuery = true)
    List<Address> findInterestPoint();
    @Query(value = """
    SELECT *
    FROM (
        SELECT *
        FROM vista_uruguay
        WHERE nombre_via IS NOT NULL
          AND num_puerta IS NOT NULL
          AND localidad IS NOT NULL
          AND departamento IN ('MONTEVIDEO', 'CANELONES', 'MALDONADO')
          AND nombre_via ~ '^[A-ZÁÉÍÓÚÑ0-9 .''-]+$'
          AND localidad  ~ '^[A-ZÁÉÍÓÚÑ0-9 .''-]+$'
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
          AND nombre_via ~ '^[A-ZÁÉÍÓÚÑ0-9 .''-]+$'
          AND localidad  ~ '^[A-ZÁÉÍÓÚÑ0-9 .''-]+$'
        ORDER BY md5(punto_wkb)
        LIMIT 200
    ) other_departments
    """, nativeQuery = true)
    List<Address> findStreetNumberLocality();

    @Query(value = """
    SELECT *
    FROM (
        SELECT *
        FROM vista_uruguay
        WHERE nombre_via IS NOT NULL
          AND num_puerta IS NOT NULL
          AND departamento IN ('MONTEVIDEO', 'CANELONES', 'MALDONADO')
          AND nombre_via ~ '^[A-ZÁÉÍÓÚÑ0-9 .''-]+$'
          AND departamento  ~ '^[A-ZÁÉÍÓÚÑ0-9 .''-]+$'
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
          AND nombre_via ~ '^[A-ZÁÉÍÓÚÑ0-9 .''-]+$'
          AND departamento  ~ '^[A-ZÁÉÍÓÚÑ0-9 .''-]+$'
        ORDER BY md5(punto_wkb)
        LIMIT 200
    ) other_departments
    """, nativeQuery = true)
    List<Address> findStreetNumberDepartament();

    @Query(value = """
    SELECT *
    FROM (
        SELECT *
        FROM vista_uruguay
        WHERE solar IS NOT NULL
          AND manzana IS NOT NULL
          AND departamento IN ('MONTEVIDEO', 'CANELONES', 'MALDONADO')
          AND solar   ~ '^[A-ZÁÉÍÓÚÑ0-9 .''-]+$'
          AND manzana ~ '^[A-ZÁÉÍÓÚÑ0-9 .''-]+$'
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
          AND solar   ~ '^[A-ZÁÉÍÓÚÑ0-9 .''-]+$'
          AND manzana ~ '^[A-ZÁÉÍÓÚÑ0-9 .''-]+$'
        ORDER BY md5(punto_wkb)
        LIMIT 200
    ) other_departments
    """, nativeQuery = true)
    List<Address> findLotBlock();

    @Query(value = """
    SELECT *
    FROM vista_uruguay
    WHERE punto_wkb IN (
        '0101000020D17F000085EB51B8CFA32141E17A14BE53735741',
        '0101000020D17F000085EB51B8D2A3214185EB51C850735741',
        '0101000020D17F00001F85EB511593214185EB5148026E5741',
        '0101000020D17F0000E6652AED59AD2141FEA141E2EE725741',
        '0101000020D17F00000AD7A3F05FA92141E17A14CED2725741',
        '0101000020D17F00000AD7A3709FA921410AD7A3D0FC725741',
        '0101000020D17F0000C3F528DC98AA21415C8FC2557A735741',
        '0101000020D17F00005C8FC2F5D4A32141295C8F924E735741',
        '0101000020D17F000052B81E05DBA321419A99999948735741',
        '0101000020D17F000005F6F6B5ACDE21417B03A14F2F6A5741',
        '0101000020D17F0000EC51B89EDE912141C3F528DCF7685741',
        '0101000020D17F0000EC51B81EDBCF214185EB5198B26A5741',
        '0101000020D17F00004EC3C3FF7CD221414B4B1638D26B5741',
        '0101000020D17F0000A4703D8A5B8C214100000050056F5741',
        '0101000020D17F0000A4703D8A60C721417B14AE57746A5741',
        '0101000020D17F000000000000AFDC2141CDCCCC2C8A6A5741',
        '0101000020D17F00004670DC05C5C321416C641ED6E66A5741',
        '0101000020D17F00002CDA30C9E5C62141176F393BD76A5741',
        '0101000020D17F0000CDCCCC4CCEC921413D0AD7A3896A5741',
        '0101000020D17F00004185507B729521411407B3ECE6675741',
        '0101000020D17F00005C8FC2750A972141AE47E11A17695741',
        '0101000020D17F000048E17A1464932141B81E85AB7D6D5741',
        '0101000020D17F00001F85EB51DA7C2141D7A370BD4F6F5741',
        '0101000020D17F0000F6285C0F877B2141D7A370AD816F5741',
        '0101000020D17F0000E17A14AE617F214185EB51D8136F5741',
        '0101000020D17F00007B14AEC77D812141713D0A67E46A5741',
        '0101000020D17F00000AD7A37013A92141E17A142ECA685741',
        '0101000020D17F000066666666D7AB2141F6285C1F88695741',
        '0101000020D17F0000997DE92919B8214110142CAE326B5741'
    )
    """, nativeQuery = true)
    List<Address> findByWkb();
}