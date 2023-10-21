package com.techelevator.green.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SignupRequest extends UserPatchRequest {
  @NotBlank
  @Size(min = 6, max = 40)
  private String password;
}
