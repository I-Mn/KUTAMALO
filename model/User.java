package model;

public class User {
    private int id;
    private String username;
    private String email;
    private String phone;
    private String avatar;

    public User(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public User(int id, String username, String email, String phone, String avatar) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.avatar = avatar;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
}
