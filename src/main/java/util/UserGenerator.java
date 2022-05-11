package util;

import java.util.Locale;

import com.github.javafaker.Faker;

import model.User;

public class UserGenerator {

    private Faker faker;

    public UserGenerator() {
         faker = new Faker();
    }

    public UserGenerator(Locale locale) {
        faker = new Faker(locale);
    }

    public User randomUser() {
        return User.builder()
                .username(faker.name().username())
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .build();
    }

}
