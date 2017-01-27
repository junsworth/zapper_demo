package com.ap.zapper.controllers;

import com.ap.zapper.models.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathanunsworth on 2017/01/23.
 */

public class PersonController {
    private static PersonController instance = null;

    private Person person = null;

    public static PersonController getInstance() {

        if(instance == null) {
            instance = new PersonController();
        }

        return instance;
    }

    private PersonController() {
        person = new Person();
    }

    public Person getPerson() {
        return person;
    }
}
