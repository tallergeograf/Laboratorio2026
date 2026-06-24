package uy.edu.taller.sige.geo_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uy.edu.taller.sige.geo_api.model.Monumento;

public interface MonumentoRepository extends JpaRepository<Monumento, Long> {
    boolean existsByOsmId(Long osmId);
}
