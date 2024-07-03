package projet.restapi.web.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface FiliereRestController {
    @GetMapping("/filieres")//End Point
    ResponseEntity<Map<Object, Object>> listerFiliere();
    @GetMapping("/filieres/{id}")//End Point
    ResponseEntity<Map<Object, Object>>  showFiliere(@PathVariable Long id);
}
