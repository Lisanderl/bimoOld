package com.mycompany.comjava.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.comjava.config.AppConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
public class ActionTest {

    @Test
    public void getSumOfNotActiveActions() {

        assertThat(Action.getSumOfActiveActions()).isEqualTo(0);

    }

    @Test
    public void getSumOfTwoActiveActions() {

        Action.GO_BACK.setActive(true);
        Action.GO_STRAIGHT.setActive(true);
        assertThat(Action.GO_BACK.isActive()).isTrue();
        assertThat(Action.GO_STRAIGHT.isActive()).isTrue();
        assertThat(Action.TURN_LEFT.isActive()).isFalse();
        assertThat(Action.TURN_RIGHT.isActive()).isFalse();
        assertThat(Action.getSumOfActiveActions()).isEqualTo(Action.GO_BACK.getActionCode()
                                                            +Action.GO_STRAIGHT.getActionCode());
    }

    @Test
    public void getSumOfOneActiveAction() {

        Action.GO_STRAIGHT.setActive(true);
        Action.GO_BACK.setActive(false);
        assertThat(Action.getSumOfActiveActions()).isEqualTo(Action.GO_STRAIGHT.getActionCode());
    }
}