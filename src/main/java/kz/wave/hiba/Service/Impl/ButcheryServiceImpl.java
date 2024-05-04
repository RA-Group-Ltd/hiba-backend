package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.DTO.ButcheryCreateDTO;
import kz.wave.hiba.DTO.ButcheryUpdateDTO;
import kz.wave.hiba.Entities.*;
import kz.wave.hiba.Repository.ButcheryRepository;
import kz.wave.hiba.Repository.RoleRepository;
import kz.wave.hiba.Repository.UserRepository;
import kz.wave.hiba.Repository.UserRoleRepository;
import kz.wave.hiba.Response.ButcheryCategoryResponse;
import kz.wave.hiba.Response.ButcheryResponse;
import kz.wave.hiba.Service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final ButcheryFileUploadCertificate butcheryFileUploadCertificate;

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

        if (userRepository.findByPhone(butcheryCreateDTO.getPhone()) == null) {
            User user = new User();
            user.setName(butcheryCreateDTO.getOwner());
            user.setPhone(butcheryCreateDTO.getPhone());
            user.setCreatedAt(Instant.now());

            Role role = roleRepository.findByName("ROLE_BUTCHERY");
            UserRoleId userRoleId = new UserRoleId(user.getId(), role.getId());
            UserRole userRole = new UserRole(userRoleId, user, role);
            userRoleRepository.save(userRole);

            User newUser = userRepository.save(user);
            newButchery.setOwner(newUser);
        } else {
            User user = userRepository.findByPhone(butcheryCreateDTO.getPhone());

            newButchery.setOwner(user);
        }

        newButchery.setName(butcheryCreateDTO.getName());
        newButchery.setAddress(butcheryCreateDTO.getAddress());
        if (butcheryCreateDTO.getLongitude() != null) {
            newButchery.setLongitude(butcheryCreateDTO.getLongitude());
        }
        if (butcheryCreateDTO.getLatitude() != null) {
            newButchery.setLatitude(butcheryCreateDTO.getLatitude());
        }
        newButchery.setCity(city);
        newButchery.setEmail(butcheryCreateDTO.getEmail());
        newButchery.setMeatType(butcheryCreateDTO.getMeatType());
        newButchery.setRegNumber(butcheryCreateDTO.getRegNumber());
        newButchery.setCreatedAt(Instant.now());
        butcheryFileUploadCertificate.uploadDocuments(butcheryCreateDTO.getDocuments(), newButchery);

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

            butcheryFileUploadCertificate.uploadDocuments(butcheryUpdateDTO.getDocuments(), updateButchery);

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
