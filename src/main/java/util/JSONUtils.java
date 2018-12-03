package util;

import org.json.simple.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class JSONUtils {

    public static void saveJSONToFile(JSONObject jsonObject, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.append(jsonObject.toJSONString()).flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
