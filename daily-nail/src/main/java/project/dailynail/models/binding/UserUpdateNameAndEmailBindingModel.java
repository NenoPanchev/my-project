package project.dailynail.models.binding;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class UserUpdateNameAndEmailBindingModel {
    private String fullName;
    private String email;

    public UserUpdateNameAndEmailBindingModel() {
    }

    @Length(max = 30, message = "Името трябва да бъде не повече от 30 символа")
    public String getFullName() {
        return fullName;
    }

    public UserUpdateNameAndEmailBindingModel setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    @NotEmpty(message = "Полето не може да е празно")
    @Email(message = "Въведете валиден имейл адрес")
    public String getEmail() {
        return email;
    }

    public UserUpdateNameAndEmailBindingModel setEmail(String email) {
        this.email = email;
        return this;
    }
}