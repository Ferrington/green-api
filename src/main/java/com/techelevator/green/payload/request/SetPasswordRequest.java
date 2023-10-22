package com.techelevator.green.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SetPasswordRequest {
  @NotBlank
  @Size(min = 6, max = 40)
  private String password;
}
