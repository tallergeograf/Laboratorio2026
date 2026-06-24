package uy.edu.taller.sige.geo_api.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uy.edu.taller.sige.geo_api.model.SpecificAddress;
import uy.edu.taller.sige.geo_api.model.enums.AddressCategory;
import uy.edu.taller.sige.geo_api.model.enums.AddressType;

import java.util.List;

@Repository
public interface SpecificAddressJPARepository extends JpaRepository<SpecificAddress, Long> {

    List<SpecificAddress> findByCategoriaAndTipoDireccion(
            AddressCategory categoria,
            AddressType tipoDireccion
    );

    List<SpecificAddress> findByIsDemo(boolean isDemo);

    @Modifying
    @Transactional
    void deleteByIsDemo(boolean isDemo);
}
