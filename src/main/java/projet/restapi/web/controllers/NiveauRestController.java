package projet.restapi.web.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

public interface NiveauRestController {
    @GetMapping("/niveaux")//End Point
    ResponseEntity<Map<Object, Object>> listerNiveau();
    @GetMapping("/niveaux/{id}")//End Point
    ResponseEntity<Map<Object, Object>>  showNiveau(@PathVariable Long id);
}
