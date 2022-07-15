package AppAdmin;

import javafx.beans.property.SimpleIntegerProperty;

public class AppAdmin {
    private String username;
    private static SimpleIntegerProperty yazInTime;

    public AppAdmin(String username, int yaz) {
        this.username = username;
        yazInTime = new SimpleIntegerProperty(yaz);
    }

    public String getName() {
        return this.username;
    }

    public static int getYazInTimeAsInt() {
        return yazInTime.get();
    }

    public static SimpleIntegerProperty getYazInTime() {
        return yazInTime;
    }

    public void setYaz(int yaz) {
        yazInTime.set(yaz);
    }


}
