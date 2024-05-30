package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.Entities.Butchery;
import kz.wave.hiba.Repository.ButcherRepository;
import kz.wave.hiba.Service.ButcherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ButcherServiceImpl implements ButcherService {

    private final ButcherRepository butcherRepository;

    @Override
    public Long getButcheryIdByButhcerUserId(Long userId) {
        Butchery butchery = butcherRepository.findButcheryByUserId(userId);
        if (butchery == null)
            return null;
        return butchery.getId();
    }
}
