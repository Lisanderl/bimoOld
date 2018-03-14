package com.mycompany.comjava;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetupDto {

    public SetupDto(int speedPwm, int lightPwm, int echoSize, String echoActive) {
        this.s = speedPwm;
        this.l = lightPwm;
        this.e = echoSize;
        v = echoActive;
    }

    private int s; //speedPwm
    private int l; //lightPwm
    private int e; //echoSize
    private String v; //echoActive + or -


}
