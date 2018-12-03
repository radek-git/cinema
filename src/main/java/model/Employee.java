package model;

import org.json.simple.JSONObject;

public class Employee extends Account {

    public Employee(int id, String username, String password) {
        super(id, username, password);
    }

    public Employee(String username, String password) {
        super(username, password);
    }

    public static Employee fromJSON(JSONObject userJSONObject) {
        return new Employee(
                ((Long) userJSONObject.get("id")).intValue(),
                (String) userJSONObject.get("username"),
                (String) userJSONObject.get("password")
        );
    }
}
