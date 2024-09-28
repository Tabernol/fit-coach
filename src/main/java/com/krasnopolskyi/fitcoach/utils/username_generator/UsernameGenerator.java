package com.krasnopolskyi.fitcoach.utils.username_generator;

public class UsernameGenerator {

    public static String generateUsername(String firstName, String lastName){
        String adjective = Adjective.getRandomAdjective().name().toLowerCase();
        // adjective + FirstName + FirstLetter of LastName
        return adjective + capitalize(firstName) + lastName.substring(0,1).toUpperCase();
    }

    private static String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }


}
