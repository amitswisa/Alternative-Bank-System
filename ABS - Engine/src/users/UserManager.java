package users;

import engine.EngineManager;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/*
Adding and retrieving users is synchronized and in that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class UserManager {

    private String admin;
    private final Set<String> usersSet;

    public UserManager() {
        admin = "";
        usersSet = new HashSet<>();
    }

    public synchronized void addUser(String customerName, EngineManager engineManager) {
        usersSet.add(customerName);
        engineManager.addNewCustomer(customerName);
    }

    public synchronized void addAdmin(String customerName, EngineManager engineManager) {
        admin = customerName;
    }

    public synchronized Set<String> getUsers() {
        return Collections.unmodifiableSet(usersSet);
    }

    public boolean isUserExists(String username) {
        return usersSet.contains(username);
    }

    public boolean isAdminExists() {
        if(admin.equals(""))
            return false;

        return true;
    }
}
