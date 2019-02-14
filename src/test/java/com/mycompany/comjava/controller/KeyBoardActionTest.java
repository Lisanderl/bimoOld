package com.mycompany.comjava.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.comjava.config.AppConfig;
import com.mycompany.comjava.config.KeyBoardAction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
public class KeyBoardActionTest {

    @Test
    public void getSumOfNotActiveActions() {

        assertThat(KeyBoardAction.getSumOfActiveActions()).isEqualTo(0);

    }

    @Test
    public void getSumOfTwoActiveActions() {

        KeyBoardAction.GO_BACK.setActive(true);
        KeyBoardAction.GO_STRAIGHT.setActive(true);
        assertThat(KeyBoardAction.GO_BACK.isActive()).isTrue();
        assertThat(KeyBoardAction.GO_STRAIGHT.isActive()).isTrue();
        assertThat(KeyBoardAction.TURN_LEFT.isActive()).isFalse();
        assertThat(KeyBoardAction.TURN_RIGHT.isActive()).isFalse();
        assertThat(KeyBoardAction.getSumOfActiveActions()).isEqualTo(KeyBoardAction.GO_BACK.getActionCode()
                                                            + KeyBoardAction.GO_STRAIGHT.getActionCode());
    }

    @Test
    public void getSumOfOneActiveAction() {

        KeyBoardAction.GO_STRAIGHT.setActive(true);
        KeyBoardAction.GO_BACK.setActive(false);
        assertThat(KeyBoardAction.getSumOfActiveActions()).isEqualTo(KeyBoardAction.GO_STRAIGHT.getActionCode());
        int j = 0;
        for (int i = 0; i < 4; i++ ){
            int x = i+j+1;
            int y = i+j;
            System.out.println("One = " + y + " Two = " + x);
            j++;
        }
            int xx = j+4;
        for (int i = j; i < xx; i++ ){
            int x = i+j+1;
            int y = i+j;
            System.out.println("One = " + y + " Two = " + x);
            j++;
        }
    }

    @Test
    public void findByButtonTest(){

        assertThat(KeyBoardAction.findActionByButton("W").get()).isEqualTo(KeyBoardAction.GO_STRAIGHT);
        assertThat(KeyBoardAction.findActionByButton("S").get()).isEqualTo(KeyBoardAction.GO_BACK);
        assertThat(KeyBoardAction.findActionByButton("A").get()).isEqualTo(KeyBoardAction.TURN_LEFT);
        assertThat(KeyBoardAction.findActionByButton("D").get()).isEqualTo(KeyBoardAction.TURN_RIGHT);
        assertThat(KeyBoardAction.findActionByButton("Space").get()).isEqualTo(KeyBoardAction.STOP);
        assertThat(KeyBoardAction.findActionByButton("DD")).isEqualTo(Optional.empty());
    }


    @Test
    public void getCorrectJson(){

        assertThat(KeyBoardAction.jsonValue(5)).isEqualTo("{\"A\":5}");

    }



}