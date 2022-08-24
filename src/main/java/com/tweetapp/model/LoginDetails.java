package com.tweetapp.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDetails {

    @NotBlank(message="userName should not be empty")
    @Schema(description = "user name of the user",required = true,example="Sriram2")
    @Pattern( regexp = "[a-zA-Z0-9@.]*$",message = "userName should contain only Alphabets and Numbers")
    private String username;

    @NotBlank(message = "Password should not be empty")
    @Schema(description = "Password of the user",required = true,example="Qwerty123")
    @Size(min=8, message = "minimum 8 Characters required")
    //@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$",message = "password must alteast 8 characters,one Lowercase letter,one Uppercase letter,one numeric value,one special character")
    private String password;

   
}
