package org.example.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ForgotPasswordRequest {
    private String username;
    private Long pin;
    private String password;
}
