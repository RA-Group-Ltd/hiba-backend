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

@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

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

    @Override
    public List<Region> getAllRegions() {
        return regionRepository.findAll();
    }

    @Override
    public Region getOneRegion(Long id) {
        return regionRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteRegion(Long id) {
        regionRepository.deleteById(id);
    }
}
