package projet.restapi.security.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import projet.restapi.security.controllers.dtos.AuthenticationRequestDto;

import java.util.Map;

public interface SecurityController {
    @PostMapping("/login")
      public ResponseEntity<Map<Object, Object>> login(@RequestBody AuthenticationRequestDto authenticationRequestDto);

}
