package br.com.hyperativa.cardreader.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "email is required")
    private String email;

    @NotBlank(message = "password is required")
    @Length(min = 8)
    private String password;
}
