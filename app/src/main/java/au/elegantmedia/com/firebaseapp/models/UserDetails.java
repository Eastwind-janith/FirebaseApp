package au.elegantmedia.com.firebaseapp.models;

/**
 * Created by Nisala on 9/20/17.
 */

public class UserDetails {
    private String name;
    private String age;
    private String email;
    private String image;
    private String key;

    public UserDetails() {
    }

    public UserDetails(String name, String age, String email, String image) {

        this.name = name;
        this.age = age;
        this.email = email;
        this.image = image;
    }

    public UserDetails(String name, String age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setValue(UserDetails newUserDetails) {
        image = newUserDetails.image;
        name = newUserDetails.name;
        age = newUserDetails.age;
        email = newUserDetails.email;
    }
}
