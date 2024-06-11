package com.MtoM.MtoM.domain.notify.repository;

import com.MtoM.MtoM.domain.notify.domain.Notify;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotifyRepository extends JpaRepository<Notify, Long> {
    List<Notify> findByReceiver(UserDomain receiver);
}