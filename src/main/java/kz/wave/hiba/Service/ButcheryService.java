package kz.wave.hiba.Service;

import kz.wave.hiba.DTO.ButcheryCreateDTO;
import kz.wave.hiba.DTO.ButcheryUpdateDTO;
import kz.wave.hiba.Entities.Butchery;
import kz.wave.hiba.Entities.City;
import kz.wave.hiba.Entities.Country;
import kz.wave.hiba.Entities.Region;
import kz.wave.hiba.Response.ButcheryResponse;

import java.util.List;

public interface ButcheryService {

    List<Butchery> getAllButchery();
    ButcheryResponse getOneButchery(Long id);
    Butchery createButchery(ButcheryCreateDTO butcheryCreateDTO, City city);
    Butchery updateButchery(ButcheryUpdateDTO butcheryUpdateDTO, City city);
    void deleteButchery(Long id);

}
