package com.ap.zapper.services.requests;

import com.ap.zapper.models.Person;

/**
 * Created by jonathanunsworth on 2017/01/23.
 */

public class PersonRequest extends BaseRequest {

    private Person person;

    public PersonRequest(String url, Person person) {
        super(url);
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }
}
