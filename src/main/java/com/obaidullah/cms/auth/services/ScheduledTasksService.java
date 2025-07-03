package com.obaidullah.cms.auth.services;

import com.obaidullah.cms.auth.repositories.ForgotPasswordRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Transactional
public class ScheduledTasksService {

    private final ForgotPasswordRepository forgotPasswordRepository;

    @Autowired
    public ScheduledTasksService(ForgotPasswordRepository forgotPasswordRepository) {
        this.forgotPasswordRepository = forgotPasswordRepository;
    }

    @Scheduled(fixedRate = 3600000)
    public void cleanupExpiredOtps() {
        forgotPasswordRepository.deleteAllByExpirationTimeBefore(new Date());
    }
}