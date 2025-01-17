package projet.restapi.security.controllers.dtos;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TokenReponseDto {
    private Long userId;
    private String token;
    private String username;
    private String nomComplet;
    private List<String> roles=new ArrayList<String>();
}
