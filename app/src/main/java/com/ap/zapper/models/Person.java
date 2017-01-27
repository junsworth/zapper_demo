package com.ap.zapper.models;

/**
 * Created by jonathanunsworth on 2017/01/22.
 */

public class Person {

    private int id;
    private String firstName;
    private String lastName;
    private int age;
    private String favouriteColour;

    public String getFirstName() {
        return firstName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFavouriteColour() {
        return favouriteColour;
    }

    public void setFavouriteColour(String favouriteColour) {
        this.favouriteColour = favouriteColour;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == 0) ? 0 : id);
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((favouriteColour == null) ? 0 : favouriteColour.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object o) {

        if(o == null)
            return false;

        if(getClass() != o.getClass())
            return false;

        if(o instanceof Person) {
            Person other = (Person)o;

            if(id == 0) {
                if(other.id != 0)
                    return false;
            } else if(id != other.id)
                return false;

            if(firstName == null) {
                if(other.firstName != null)
                    return false;
            } else if(!firstName.equals(other.firstName))
                return false;

            if(lastName == null) {
                if(other.lastName != null)
                    return false;
            } else if(!lastName.equals(other.lastName))
                return false;

//            if(favouriteColour == null) {
//                if(other.favouriteColour != null)
//                    return false;
//            } else if(!favouriteColour.equals(other.favouriteColour))
//                return false;

        }

        return true;

    }
}
