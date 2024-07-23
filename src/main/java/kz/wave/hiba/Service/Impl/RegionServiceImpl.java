package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.DTO.RegionCreateDTO;
import kz.wave.hiba.DTO.RegionUpdateDTO;
import kz.wave.hiba.Entities.Country;
import kz.wave.hiba.Entities.Region;
import kz.wave.hiba.Repository.RegionRepository;
import kz.wave.hiba.Service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link RegionService} interface.
 */
@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    /**
     * Creates a new region.
     *
     * @param regionCreateDTO the data transfer object containing region creation details
     * @param country the country entity to associate with the region
     * @return the created region entity, or null if an error occurs
     */
    @Override
    public Region createRegion(RegionCreateDTO regionCreateDTO, Country country) {
        try {
            Region region = new Region();
            region.setName(regionCreateDTO.getName());
            region.setCountry(country);
            return regionRepository.save(region);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Updates an existing region.
     *
     * @param regionUpdateDTO the data transfer object containing region update details
     * @return the updated region entity, or null if the region does not exist
     */
    @Override
    public Region updateRegion(RegionUpdateDTO regionUpdateDTO) {
        Optional<Region> existingRegionOpt = regionRepository.findById(regionUpdateDTO.getId());
        if (existingRegionOpt.isEmpty()) {
            return null;
        }

        Region existingRegion = existingRegionOpt.get();

        existingRegion.setName(regionUpdateDTO.getName());
        existingRegion.setCountry(regionUpdateDTO.getCountry());

        return regionRepository.save(existingRegion);
    }

    /**
     * Retrieves all regions.
     *
     * @return a list of all region entities
     */
    @Override
    public List<Region> getAllRegions() {
        return regionRepository.findAll();
    }

    /**
     * Retrieves a region by its ID.
     *
     * @param id the ID of the region
     * @return the region entity, or null if the region does not exist
     */
    @Override
    public Region getOneRegion(Long id) {
        return regionRepository.findById(id).orElse(null);
    }

    /**
     * Deletes a region by its ID.
     *
     * @param id the ID of the region to delete
     */
    @Override
    public void deleteRegion(Long id) {
        regionRepository.deleteById(id);
    }
}
