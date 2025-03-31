package com.halo.eventer.domain.manager.service;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.festival.service.FestivalService;
import com.halo.eventer.domain.manager.Manager;
import com.halo.eventer.domain.manager.dto.ManagerCreateReqDto;
import com.halo.eventer.domain.manager.repository.ManagerRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final FestivalRepository festivalRepository;

    @Transactional
    public void createManager(Long festivalId, ManagerCreateReqDto managerCreateReqDto) {
        Festival festival = festivalRepository
                .findById(festivalId).orElseThrow(() -> new FestivalNotFoundException(festivalId));
        managerRepository.save(new Manager(managerCreateReqDto.getPhoneNo(), festival));
    }

    public List<Manager> getManagerList(Long festivalId) {
        return managerRepository.findManagerByFestivalId(festivalId);
    }

    public void deleteManager(Long managerId) {
        managerRepository.deleteById(managerId);
    }
}
