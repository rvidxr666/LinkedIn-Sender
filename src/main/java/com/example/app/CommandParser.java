package com.example.app;
import org.apache.commons.cli.*;
import org.apache.commons.cli.ParseException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;


public class CommandParser {
    public String[] args;


    public CommandParser(String[] args) {
        this.args = args;
    }

    public HashMap<String,String> parseArguments() {
        Options options = new Options();

        Option email = new Option("e", "email", true, "User email");
        email.setRequired(true);
        options.addOption(email);

        Option password = new Option("p", "password", true, "Password");
        password.setRequired(true);
        options.addOption(password);

        Option department = new Option("d", "department", false, "Department");
        department.setRequired(false);
        options.addOption(department);

        Option message = new Option("m", "message", true, "Message");
        department.setRequired(true);
        options.addOption(message);

        HelpFormatter formatter = new HelpFormatter();
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, this.args);
            HashMap<String, String> userData = this.structUserData(cmd);
            return userData;
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("User Profile Info", options);
            System.exit(1);
        }

        return new HashMap<String, String>();
    }

    private HashMap<String, String> structUserData(CommandLine parser) {
        HashMap<String, String> userData = new HashMap<>();

        for (Option option : parser.getOptions()) {
            if (Objects.equals(option.getLongOpt(), "department") && option.getValue() == null) {
                userData.put(option.getLongOpt(), "HR");
            } else {
                userData.put(option.getLongOpt(), option.getValue());
            }

        }

        return userData;
    }

}
