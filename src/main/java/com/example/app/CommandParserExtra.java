package com.example.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.*;

public class CommandParserExtra {
    public String[] args;
    private HashMap<String, String[]> argsParsed;
    private final List<String> flags;


    public CommandParserExtra(String[] args){
        this.args = args;
        this.argsParsed = this.argsInit();
        this.flags = Arrays.asList("-e", "-p", "-d", "-f", "--country", "--city", "-t");
    }
    public HashMap<String, String> parseArguments() {

        if (Arrays.asList(args).contains("--help")) {
            String helpMessage = "";
            System.out.println(helpMessage);
            System.exit(1);
        }

        // Check args
        this.checkArgs();
        // java exec args -e a@gmail.com -p qwerty12 -d HR -f -f -f -f --country Canada --city Toronto -t Recruitment

        // Check if the last element is an empty flag
        if (args[args.length-1].contains("-")) {
            String usageMessage = this.helpMessage();
            System.out.println("The empty flag: " + args[args.length-1] + " was specified!\n" + usageMessage);
            System.exit(1);
        }

        for(int i = 0; i<args.length-1; i++) {
            if (flags.contains(args[i])) {
                String nextVal = args[i+1];

                // Duplicate
                if (argsParsed.containsKey(args[i]) && Objects.equals(argsParsed.get(args[i])[1], "Set")){
                    String usageMessage = this.helpMessage();
                    System.out.println("The flag: "+ args[i] + " was duplicated!\n" + usageMessage);
                    System.exit(1);
                }

                // Empty flag
                if (nextVal.contains("-")){
                    String usageMessage = this.helpMessage();
                    System.out.println("The flag: "+ args[i] + " is empty!\n" + usageMessage);
                    System.exit(1);
                }

                // Adding the flag to the HashMap with the Parameter 'Set'
                if (!flags.contains(nextVal)) {
                    this.argsParsed.put(args[i], new String[] {nextVal, "Set"});
                }
            }
        }

        // Not all args were set
        if (argsParsed.size() != flags.size()) {
            System.out.println("Some of the required flags are missing!\n" + this.helpMessage());
            System.exit(1);
        }

        // File logic
        try {
            String filePath = argsParsed.get("-f")[0];
            String text = this.setText(filePath);
            argsParsed.put("-f", new String[] {text, "Set"});
        } catch (FileNotFoundException e) {
            System.out.println("File specified wasn't found!");
            System.exit(1);
        }

        HashMap<String, String> argsProcessed = this.processOutput();
        return argsProcessed;
    }

    private HashMap<String, String> processOutput() {
        HashMap<String, String> processedArgs = new HashMap<>();
        for (String key : argsParsed.keySet()) {
            processedArgs.put(key, argsParsed.get(key)[0]);
        }
        return processedArgs;
    }


    private void checkArgs() {
        // Checking if all the args exist
        for (String arg : args) {
            if (arg.contains("-") && !flags.contains(arg)) {
                System.out.println(this.helpMessage());
                System.exit(1);
            }
        }
    }

    private String setText(String filePath) throws FileNotFoundException {
        File textFile = new File(filePath);

        // Check if global path exists
        String globalFileText = this.fileToString(textFile);
        if (globalFileText != null) {
            return globalFileText;
        }

        // Otherwise try locally
        String currDirectory = FileSystems.getDefault()
                                          .getPath("")
                                          .toAbsolutePath()
                                          .toString();

        String localAbsolute = currDirectory + "\\" + filePath;
        File textFileLocal = new File(localAbsolute);
        String localFileText = this.fileToString(textFileLocal);

        if (localFileText != null) {
            return localFileText;
        }

        System.out.println("Local file: " + localAbsolute);

        throw new FileNotFoundException();
    }

    private String fileToString(File file) {
        StringBuilder finalString = new StringBuilder();
        if (file.exists()) {
            try {
                Scanner reader = new Scanner(file);
                while (reader.hasNextLine()){
                    finalString.append(reader.nextLine()).append("\n");
                }
                return finalString.toString();
            } catch (FileNotFoundException e) {
                System.out.println("Error occured while searching for a file!");
                return null;
            }
        }
        return null;

    }
    private HashMap<String, String[]> argsInit() {

        HashMap<String, String[]> defaultArgs = new HashMap<>();
        defaultArgs.put("-d", new String[] {"HR", "Default"});
        defaultArgs.put("--country", new String[] {"Canada", "Default"});
        defaultArgs.put("--city", new String[] {"Toronto", "Default"});
        defaultArgs.put("-t", new String[] {"Hello There!", "Default"});

        return defaultArgs;
    }


    private String helpMessage() {
        String message = """
                Usage of the command:
                -e User (Email for logging into the Linkedin Profile) REQUIRED
                -p (User Password for logging into the Linkedin Profile) REQUIRED
                -f (File with the message) REQUIRED
                --country (Target Country) DEFAULT: 'CANADA'
                --city (Target City) DEFAULT: 'TORONTO'
                -d (Target Department) DEFAULT: 'HR'
                -t (Topic of the Message) DEFAULT: 'Hello There!'
                """;

        return message;
    }


}
