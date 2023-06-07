package com.example.userservice.DTOs;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@ToString
@Builder
public class UserDTO {
   @ApiModelProperty(hidden = true)
   private Long id;

   @NotBlank(message = "First name is required")
   private String firstName;

   @NotBlank(message = "Last name is required")
   private String lastName;

   public void setFirstName(String firstName) {
      if (firstName == null || firstName.trim().isEmpty()) {
         throw new IllegalArgumentException();
      }

      this.firstName = firstName;
   }

   public void setLastName(String lastName) {
      if (lastName == null || lastName.trim().isEmpty()) {
         throw new IllegalArgumentException();
      }

      this.lastName = lastName;
   }



}