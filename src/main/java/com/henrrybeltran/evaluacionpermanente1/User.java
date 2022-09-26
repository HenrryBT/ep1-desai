package com.henrrybeltran.evaluacionpermanente1;

public class User {
    public String name;
    public String lastname;
    public String username;
    public String email;
    public String password;
    public int age;
    public String subscription;

    public User(String name, String lastname, String username, String email, String password, int age, String subscription) {
        this.name = name;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.age = age;
        this.subscription = subscription;
    }
}
