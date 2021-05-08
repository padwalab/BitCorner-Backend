package org.sjsu.bitcornerbackend.util;

import javax.persistence.Embeddable;

@Embeddable
public class Address {

    private String street; // e.g., 100 Main ST
    private String city;
    private String state;
    private String zip;

    public Address() {

    }

    public Address(AddressBuilder addressBuilder) {
        this.street = addressBuilder.street;
        this.city = addressBuilder.city;
        this.state = addressBuilder.state;
        this.zip = addressBuilder.zip;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public static class AddressBuilder {
        private String street;
        private String city;
        private String state;
        private String zip;

        public AddressBuilder() {

        }

        public AddressBuilder setStreet(String street) {
            this.street = street;
            return this;
        }

        public AddressBuilder setCity(String city) {
            this.city = city;
            return this;
        }

        public AddressBuilder setState(String state) {
            this.state = state;
            return this;
        }

        public AddressBuilder setZip(String zip) {
            this.zip = zip;
            return this;
        }

        public Address build() {
            return new Address(this);
        }

    }

}
