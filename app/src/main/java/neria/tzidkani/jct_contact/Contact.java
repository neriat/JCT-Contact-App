package neria.tzidkani.jct_contact;

/**
 * Created by Neria Tzidkani on 13/03/2017.
 */

import java.io.Serializable;
public class Contact implements Serializable {
    public String name;
    public String email;

    private void assignValues(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Contact(String name, String email) {
        assignValues(name, email);
    }

    public Contact() {
        assignValues("", "");
    }

    public boolean contains(String str) {
        return name.contains(str) || email.contains(str);
    }

    @Override
    public String toString() {
        return name + "\n" + email;
    }
}
