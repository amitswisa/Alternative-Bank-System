package Utils;

import javafx.beans.property.SimpleStringProperty;

public class User {
    private final String defaultUserName;
    private SimpleStringProperty username;

    public User(String usernameFieldContent) {
        this.defaultUserName = "Admin";
        username = new SimpleStringProperty(usernameFieldContent);
    }

    public String getUsername() {
        return username.getValue();
    }

    public SimpleStringProperty getUsernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.setValue(username);
    }

    public String getDefaultUserName() {
        return defaultUserName;
    }
}
