package org.example.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangeUserDataRequest {
    private String username;
    private String email;
}
