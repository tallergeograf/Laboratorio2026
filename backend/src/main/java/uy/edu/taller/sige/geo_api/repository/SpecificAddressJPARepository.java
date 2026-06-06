package uy.edu.taller.sige.geo_api.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uy.edu.taller.sige.geo_api.model.SpecificAddress;

@Repository
public interface SpecificAddressJPARepository extends JpaRepository<SpecificAddress, String> {
}
