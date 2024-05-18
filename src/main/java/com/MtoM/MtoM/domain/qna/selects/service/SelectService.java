package com.MtoM.MtoM.domain.qna.selects.service;

import com.MtoM.MtoM.domain.qna.selects.domain.SelectDomain;
import com.MtoM.MtoM.domain.qna.selects.dto.CreateSelectDTO;
import com.MtoM.MtoM.domain.qna.selects.repository.SelectRepository;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.MtoM.MtoM.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SelectService {
    @Autowired
    private SelectRepository selectRepository;

    @Autowired
    private UserRepository userRepository;

    public List<SelectDomain> getAllSelects() {
        return selectRepository.findAll();
    }

    public Optional<SelectDomain> getSelectById(Long id) {

        return selectRepository.findById(id);
    }

    public SelectDomain createSelect(CreateSelectDTO selectDTO) {

        SelectDomain select = new SelectDomain();
        select.setTitle(selectDTO.getTitle());
        select.setOption1(selectDTO.getOption1());
        select.setOption2(selectDTO.getOption2());
        select.setOption1Percent(0);
        select.setOption2Percent(0);

        // userId를 이용하여 사용자를 검색하여 user 필드에 설정
        UserDomain user = userRepository.findById(selectDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        select.setUser(user);

        return selectRepository.save(select);
    }

    // 다른 메서드들은 생략

    public void updateSelect(Long id, CreateSelectDTO selectDTO, String userId) {
        Optional<SelectDomain> existingSelect = selectRepository.findById(id);
        if (existingSelect.isPresent()) {
            SelectDomain select = existingSelect.get();
            if (select.getUser().getId().equals(userId)) {
                select.setTitle(selectDTO.getTitle());
                select.setOption1(selectDTO.getOption1());
                select.setOption2(selectDTO.getOption2());
                selectRepository.save(select);
            } else {
                throw new RuntimeException("권한이 없습니다.");
            }
        } else {
            throw new RuntimeException("해당 선택지가 없습니다.");
        }
    }

    public void deleteSelect(Long id, String userId) {
        Optional<SelectDomain> existingSelect = selectRepository.findById(id);
        if (existingSelect.isPresent()) {
            SelectDomain select = existingSelect.get();
            if (select.getUser().getId().equals(userId)) {
                selectRepository.deleteById(id);
            } else {
                throw new RuntimeException("권한이 없습니다.");
            }
        } else {
            throw new RuntimeException("해당 선택지가 없습니다.");
        }
    }

    public void deleteSelect(Long id) {
        selectRepository.deleteById(id);
    }
}