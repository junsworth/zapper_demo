package com.ap.zapper.controllers;

import com.ap.zapper.models.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathanunsworth on 2017/01/23.
 */

public class PeopleController {

    private static PeopleController instance = null;

    private List<Person> people = null;

    public static PeopleController getInstance() {

        if(instance == null) {
            instance = new PeopleController();
        }

        return instance;
    }

    private PeopleController() {
        people = new ArrayList<>();
    }

    public List<Person> getPeople() {
        return people;
    }

}
