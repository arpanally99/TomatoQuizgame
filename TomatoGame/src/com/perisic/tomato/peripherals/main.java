package com.perisic.tomato.peripherals;



public class main {



    public main(String[] args) {

        // Creating an instance of the LoginData class

        new LoginData();


    }



    private static void registerUser(LoginData loginData, String username, String password) {

        try {

            loginData.registerUser(username, password);

            System.out.println("User registered: " + username);

        } catch (LoginData.UserAlreadyExistsException e) {

            System.out.println("Error: " + e.getMessage());

        }

    }



    private static void checkPassword(LoginData loginData, String username, String password) {

        if (loginData.checkPassword(username, password)) {

            System.out.println("Password is correct for user: " + username);

        } else {

            System.out.println("Incorrect password for user: " + username);

        }

    }

}