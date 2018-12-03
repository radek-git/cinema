package model;

import org.json.simple.JSONObject;

public class Owner extends Account {

    public Owner(int id, String username, String password) {
        super(id, username, password);
    }

    public Owner(String username, String password) {
        super(username, password);
    }

    public static Owner fromJSON(JSONObject userJSONObject) {
        return new Owner(
                ((Long) userJSONObject.get("id")).intValue(),
                (String) userJSONObject.get("username"),
                (String) userJSONObject.get("password")
        );
    }
}
