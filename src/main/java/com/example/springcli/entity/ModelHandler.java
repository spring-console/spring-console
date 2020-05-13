package com.example.springcli.entity;

import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import org.springframework.boot.cli.command.options.OptionHandler;
import org.springframework.boot.cli.command.status.ExitStatus;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author NetoDevel
 * @since 0.0.1
 */
public class ModelHandler extends OptionHandler {

    @SuppressWarnings("unused")
    private OptionSpec<String> nameEntity;

    @SuppressWarnings("unused")
    private OptionSpec<String> parametersEntity;


    @Override
    protected void options() {
        this.nameEntity = option(Arrays.asList("nameEntity", "n"), "Name of entity").withRequiredArg();
        this.parametersEntity = option(Arrays.asList("parameterEntity", "p"), "Parameters of entity").withRequiredArg();
    }

    @Override
    protected ExitStatus run(OptionSet options) throws Exception {

        System.out.println("I'm in run method");

        return ExitStatus.OK;
    }


}
