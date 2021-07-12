package com.awesometeam.Invoicify.invoice.utility;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Helper {

    public static String getJSON(String path) throws Exception {
        Path paths = Paths.get(path);
        String pathReturn = new String(Files.readAllBytes(paths));
        return pathReturn;
    }

}
