package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.DTO.ButcheryCreateDTO;
import kz.wave.hiba.DTO.ButcheryUpdateDTO;
import kz.wave.hiba.Entities.Butchery;
import kz.wave.hiba.Entities.City;
import kz.wave.hiba.Repository.ButcheryRepository;
import kz.wave.hiba.Service.ButcheryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ButcheryServiceImpl implements ButcheryService {

    private final ButcheryRepository butcheryRepository;

    @Override
    public List<Butchery> getAllButchery() {
        return butcheryRepository.findAll();
    }

    @Override
    public Butchery getOneButchery(Long id) {
        return butcheryRepository.findById(id).orElseThrow();
    }

    @Override
    public Butchery createButchery(ButcheryCreateDTO butcheryCreateDTO, City city) {

        Butchery newButchery = new Butchery();
        newButchery.setName(butcheryCreateDTO.getName());
        newButchery.setAddress(butcheryCreateDTO.getAddress());
        newButchery.setLongitude(butcheryCreateDTO.getLongitude());
        newButchery.setLatitude(butcheryCreateDTO.getLatitude());
        newButchery.setCity(city);

        return butcheryRepository.save(newButchery);
    }

    @Override
    public Butchery updateButchery(ButcheryUpdateDTO butcheryUpdateDTO, City city) {
        Optional<Butchery> butcheryOptional = butcheryRepository.findById(butcheryUpdateDTO.getId());

        if (butcheryOptional.isPresent()) {
            Butchery updateButchery = butcheryOptional.get();

            updateButchery.setName(butcheryUpdateDTO.getName());
            updateButchery.setAddress(butcheryUpdateDTO.getAddress());
            updateButchery.setLongitude(butcheryUpdateDTO.getLongitude());
            updateButchery.setLatitude(butcheryUpdateDTO.getLatitude());
            updateButchery.setCity(city);

            return butcheryRepository.save(updateButchery);
        } else {
            return null;
        }
    }

    @Override
    public void deleteButchery(Long id) {
        butcheryRepository.deleteById(id);
    }
}
