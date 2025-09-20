package com.springdemo.bangtori_be.service;


import com.springdemo.bangtori_be.model.Appliance;

public class ArduinoGatewayImpl implements ArduinoGateway {

    @Override
    public boolean setPower(Appliance.ApplianceType type, boolean on) {
        return false;
    }
}
