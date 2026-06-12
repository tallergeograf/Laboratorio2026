package uy.edu.taller.sige.geo_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uy.edu.taller.sige.geo_api.model.SpecificAddressResult;
import uy.edu.taller.sige.geo_api.model.SpecificAddressResultId;

@Repository
public interface SpecificAddressResultRepository extends JpaRepository<SpecificAddressResult, SpecificAddressResultId> {

}
