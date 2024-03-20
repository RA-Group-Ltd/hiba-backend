package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.DTO.ButcheryCreateDTO;
import kz.wave.hiba.DTO.ButcheryUpdateDTO;
import kz.wave.hiba.Entities.*;
import kz.wave.hiba.Repository.ButcheryRepository;
import kz.wave.hiba.Response.ButcheryCategoryResponse;
import kz.wave.hiba.Response.ButcheryResponse;
import kz.wave.hiba.Service.ButcheryCategoryService;
import kz.wave.hiba.Service.ButcheryService;
import kz.wave.hiba.Service.CategoryService;
import kz.wave.hiba.Service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ButcheryServiceImpl implements ButcheryService {

    private final ButcheryRepository butcheryRepository;
    private final ButcheryCategoryService butcheryCategoryService;
    private final MenuService menuService;
    private final CategoryService categoryService;

    @Override
    public List<Butchery> getAllButchery() {
        return butcheryRepository.findAll();
    }

    @Override
    public ButcheryResponse getOneButchery(Long id) {
        Butchery butchery = butcheryRepository.findById(id).orElseThrow();
        ButcheryResponse butcheryResponse = new ButcheryResponse();
        List<ButcheryCategory> butcheryCategories = butcheryCategoryService.getCategoriesByButcheryId(butchery.getId());
        List<ButcheryCategoryResponse> cats = new ArrayList<>();
        for (int i = 0; i < butcheryCategories.size(); i++) {
            ButcheryCategory butcheryCategory = butcheryCategories.get(i);
            List<Menu> menuItems = menuService.getMenuListByButcheryCategoryId(butcheryCategory.getId());
            Category category = categoryService.getCategoryById(butcheryCategory.getCategoryId());

            ButcheryCategoryResponse butcheryCategoryResponse = new ButcheryCategoryResponse(butcheryCategory.getId(), category, menuItems);
            cats.add(butcheryCategoryResponse);
        }
        butcheryResponse.setId(butchery.getId());
        butcheryResponse.setName(butchery.getName());
        butcheryResponse.setLatitude(butchery.getLatitude());
        butcheryResponse.setLongitude(butchery.getLongitude());
        butcheryResponse.setAddress(butchery.getAddress());
        butcheryResponse.setCity(butchery.getCity());
        butcheryResponse.setCategories(cats);

        return butcheryResponse;
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
