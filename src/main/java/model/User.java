package model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {

    private Long id;
    private String username;
    private String name;
    private String email;

}
