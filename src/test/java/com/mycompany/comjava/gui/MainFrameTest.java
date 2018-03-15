package com.mycompany.comjava.gui;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.comjava.SetupDto;
import com.mycompany.comjava.config.AppConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.nio.charset.StandardCharsets;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})

public class MainFrameTest {

    @Test
    public void test() throws JsonProcessingException {

        byte[] x = {73,105,126};
        String lal = new String(x, StandardCharsets.UTF_8);
        String lal2 = "AD1C12";

        assertThat(lal).isEqualTo("Ii~");
        assertThat(lal.contains("i")).isTrue();
        assertThat(lal2.contains("AD1C")).isTrue();
        assertThat(lal.startsWith("I")).isTrue();
        assertThat(lal.startsWith("i", 1)).isTrue();
        assertThat(lal2.replaceFirst("AD1C", "")).isEqualTo("12");

        ObjectMapper objectMapper = new ObjectMapper();
        SetupDto setupDto =  new SetupDto(50,100,20,"+");
        System.out.println(objectMapper.writeValueAsString(setupDto));
        System.out.println("///////////////////////////////////////////////////////////");

    }

}