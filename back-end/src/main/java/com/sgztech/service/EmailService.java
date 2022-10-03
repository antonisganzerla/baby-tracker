package com.sgztech.service;

import com.sgztech.domain.dto.EmailDetailsDTO;

public interface EmailService {

    void sendSimpleMail(EmailDetailsDTO details);

}
