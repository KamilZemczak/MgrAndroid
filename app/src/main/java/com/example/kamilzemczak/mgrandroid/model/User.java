package com.example.kamilzemczak.mgrandroid.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "username",
        "name",
        "surname",
        "password",
        "passwordConfirm",
        "date_of_birth",
        "gender",
        "weight",
        "height",
        "favourite"
})
public class User {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("username")
    private String username;
    @JsonProperty("name")
    private String name;
    @JsonProperty("surname")
    private String surname;
    @JsonProperty("password")
    private String password;
    @JsonProperty("passwordConfirm")
    private String passwordConfirm;
    @JsonProperty("dateOfBirth")
    private Date dateOfBirth;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("weight")
    private Integer weight;
    @JsonProperty("height")
    private Integer height;
    @JsonProperty("favourite")
    private String favourite;
    @JsonProperty("editDate")
    private String editDate;

    public User() {

    }

    public User(Integer id, String name, String surname, String username, String password, Date dateOfBirth, String gender, Integer weight, Integer height, String favourite, String editDate) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
        this.favourite = favourite;
        this.editDate = editDate;
    }

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("surname")
    public String getSurname() {
        return surname;
    }

    @JsonProperty("surname")
    public void setSurname(String surname) {
        this.surname = surname;
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    @JsonProperty("username")
    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty("dateOfBirth")
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    @JsonProperty("dateOfBirth")
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @JsonProperty("gender")
    public String getGender() {
        return gender;
    }

    @JsonProperty("gender")
    public void setGender(String gender) {
        this.gender = gender;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("passwordConfirm")
    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    @JsonProperty("passwordConfirm")
    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    @JsonProperty("weight")
    public Integer getWeight() {
        return weight;
    }

    @JsonProperty("weight")
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @JsonProperty("height")
    public Integer getHeight() {
        return height;
    }

    @JsonProperty("height")
    public void setHeight(Integer height) {
        this.height = height;
    }

    @JsonProperty("favourite")
    public String getFavourite() {
        return favourite;
    }

    @JsonProperty("favourite")
    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }

    @JsonProperty("editDate")
    public String getEditDate() {
        return editDate;
    }

    @JsonProperty("editDate")
    public void setEditDate(String editDate) {
        this.editDate = editDate;
    }
}
