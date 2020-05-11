package it.polimi.ingsw.common.logging.reader;

import it.polimi.ingsw.common.logging.ILogReader;
import it.polimi.ingsw.common.logging.Logger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileLogReader implements ILogReader {

    private static final String FILE_CHARSET = "UTF-8";
    private static final String LOG_FAIL = "Failed to write log to file, file logging is now disabled";

    private final Path path;

    public FileLogReader(Path path) {
        this.path = path;

        if (Files.isDirectory(path)) {
            throw new IllegalArgumentException();
        }

        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
        } catch (IOException exception) {
            Logger.getInstance().exception(exception);
        }
    }

    @Override
    public void read(String message) {
        try {
            Files.writeString(path, message + System.lineSeparator(), Charset.forName(FILE_CHARSET), StandardOpenOption.APPEND);
        } catch (IOException exception) {
            Logger.getInstance().removeReader(this);
            Logger.getInstance().exception(exception);
            Logger.getInstance().severe(LOG_FAIL);
        }
    }

}
