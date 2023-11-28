package pl.grzegorz.neat.model.user;

public class UserProfileForm {

    private String username;
    private String name;
    private String surname;
    private String email;


    public String getName() {
        return name;
    }



    public UserProfileForm() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
