package commander.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class ActionEngine {

    public ActionResult execute(CommandUnderstanding command) {

        if (command == null) {
            return new ActionResult(
                    false,
                    "No command received."
            );
        }

        if (command.getIntent() == Intent.FILE
                && "LIST".equals(command.getAction())) {

            return listFiles();
        }

        if (command.getIntent() == Intent.APPLICATION
                && "OPEN".equals(command.getAction())) {

            return openApplication(
                    command.getEntity()
            );
        }

        if (command.getIntent() == Intent.GREETING
                && "RESPOND".equals(command.getAction())) {

            return new ActionResult(
                    true,
                    "Hello. I am Sūtrādhār."
            );
        }

        return new ActionResult(
                false,
                "I understand the command, but I cannot execute it yet."
        );
    }

    private ActionResult listFiles() {

        Path currentDirectory =
                Path.of(".").toAbsolutePath().normalize();

        try {

            String files = Files.list(currentDirectory)
                    .map(path -> path.getFileName().toString())
                    .sorted()
                    .collect(Collectors.joining("\n"));

            return new ActionResult(
                    true,
                    "Files and folders:\n" + files
            );

        } catch (IOException e) {

            return new ActionResult(
                    false,
                    "Unable to read the current directory."
            );
        }
    }

    private ActionResult openApplication(String application) {

        if (application == null) {
            return new ActionResult(
                    false,
                    "No application was specified."
            );
        }

        /*
         * We are deliberately not launching applications yet.
         * This keeps Step 6 safe while we build the action architecture.
         */
        return new ActionResult(
                false,
                "Application control is not implemented yet: "
                        + application
        );
    }
}