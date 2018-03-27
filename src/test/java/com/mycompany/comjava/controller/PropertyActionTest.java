package com.mycompany.comjava.controller;

import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.comjava.config.AppConfig;
import com.mycompany.comjava.config.PropertyAction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)


public class PropertyActionTest {

    @Test
    public void getCorrectJson(){

        assertThat(PropertyAction.jsonValue(PropertyAction.ECHO, 5)).isEqualTo("{\"E\":5}");
        String voltageJson = "{\"V\":532}";
        assertThat(voltageJson.replaceAll("[^0-9]", "")).isEqualTo("532");

    }

}
