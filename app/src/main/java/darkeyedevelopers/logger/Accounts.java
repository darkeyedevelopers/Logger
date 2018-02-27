package darkeyedevelopers.logger;

import java.util.Objects;

/**
 * Created by onk_r on 26-02-2018.
 */

public class Accounts {
    private String username;
    private String pass;

    public Accounts(String _username,String _pass){
        username = _username;
        pass = _pass;
    }

    public String getPass() {
        return pass;
    }

    public String getUsername() {
        return username;
    }

    public void setPass(String _pass) {
        this.pass = _pass;
    }

    public void setUsername(String _username) {
        this.username = _username;
    }

    public boolean checkPass(String _pass) {
        return this.pass.equals(_pass);
    }

    public boolean equals(Object o) {
        Accounts obj = (Accounts)(o);
        if (this.username == obj.getUsername()) {
            if (this.pass.equals(obj.getPass()))
                return true;
            else
                return false;
        }
        return false;
    }

    @Override
    public String toString() {
        return new String(getUsername() + " " + getPass()+"\n");
    }

}
