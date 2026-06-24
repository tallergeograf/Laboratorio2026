package uy.edu.taller.sige.geo_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import uy.edu.taller.sige.geo_api.model.SpecificAddressResult;
import uy.edu.taller.sige.geo_api.model.SpecificAddressResultId;

@Repository
public interface SpecificAddressResultRepository extends JpaRepository<SpecificAddressResult, SpecificAddressResultId> {

    @Modifying
    @Transactional
    @Query("DELETE FROM SpecificAddressResult r WHERE r.id.isDemo = :isDemo")
    void deleteByIsDemo(boolean isDemo);
}
