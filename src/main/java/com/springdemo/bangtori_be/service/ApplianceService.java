// service/ApplianceService.java
package com.springdemo.bangtori_be.service;

import com.springdemo.bangtori_be.dto.ApplianceCommandResponse;
import com.springdemo.bangtori_be.dto.ApplianceDTO;

public interface ApplianceService {
    ApplianceCommandResponse setPower(ApplianceDTO dto);
}
