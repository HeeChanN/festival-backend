package com.halo.eventer.domain.missing_person.service;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.festival.service.FestivalService;
import com.halo.eventer.domain.manager.Manager;
import com.halo.eventer.domain.manager.repository.ManagerRepository;
import com.halo.eventer.domain.missing_person.MissingPerson;
import com.halo.eventer.domain.missing_person.dto.MissingPersonReqDto;
import com.halo.eventer.domain.missing_person.exception.MissingPersonNotFoundException;
import com.halo.eventer.domain.missing_person.formatter.MissingPersonAlertMessageTemplate;
import com.halo.eventer.domain.missing_person.repository.MissingPersonRepository;

import java.util.List;
import java.util.stream.Collectors;

import com.halo.eventer.infra.common.sms.SmsClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MissingPersonService {

  private final MissingPersonRepository missingPersonRepository;
  private final FestivalRepository festivalRepository;
  private final ManagerRepository managerRepository;
  private final SmsClient smsClient;


  // 실종자 찾기 신청
  public void createMissingPerson(Long festivalId, MissingPersonReqDto missingPersonReqDto) {
    Festival festival = festivalRepository
            .findById(festivalId).orElseThrow(() -> new FestivalNotFoundException(festivalId));
    missingPersonRepository.save(new MissingPerson(missingPersonReqDto, festival));
    notifyManagersAboutMissingPerson(festivalId, missingPersonReqDto);
  }

  public List<MissingPerson> getAllMissingPersonList() {
    return missingPersonRepository.findAll();
  }

  public MissingPerson getMissingPerson(Long id) {
    return missingPersonRepository.findById(id)
        .orElseThrow(() -> new MissingPersonNotFoundException(id));
  }

  @Transactional
  public void updateMissingPerson(Long id, MissingPersonReqDto missingPersonReqDto) {
    MissingPerson person = missingPersonRepository.findById(id)
            .orElseThrow(() -> new MissingPersonNotFoundException(id));
    person.update(missingPersonReqDto);
  }

  @Transactional
  public void checkPopup(Long id, boolean check) {
    MissingPerson person = missingPersonRepository.findById(id)
            .orElseThrow(() -> new MissingPersonNotFoundException(id));
    person.setPopup(check);
  }

  @Transactional
  public void deleteMissingPerson(Long missingId) {
    missingPersonRepository.delete(missingPersonRepository.findById(missingId)
            .orElseThrow(() -> new MissingPersonNotFoundException(missingId)));
  }

  public List<MissingPerson> getPopupList(Long festivalId) {
    return missingPersonRepository.findAllByFestivalIdAndPopup(festivalId, true);
  }

  private void notifyManagersAboutMissingPerson(Long festivalId, MissingPersonReqDto missingPersonReqDto) {
    List<String> phoneNumbers = managerRepository.findManagerByFestivalId(festivalId).stream()
              .map(Manager::getPhoneNo)
              .collect(Collectors.toList());

    if (!phoneNumbers.isEmpty()) {
      MissingPersonAlertMessageTemplate template = new MissingPersonAlertMessageTemplate();
      String message = template.buildMessage(missingPersonReqDto);
      smsClient.send(message, phoneNumbers);
      log.info("실종자 등록 알림 SMS 전송 완료 - 이름: {}, 수신자 수: {}",
          missingPersonReqDto.getName(), phoneNumbers.size());
    }
  }
}
