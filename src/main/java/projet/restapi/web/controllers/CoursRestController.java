package projet.restapi.web.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import projet.core.data.enums.EtatCours;
import projet.restapi.web.dtos.request.CoursCreateRequestDto;

import java.util.Map;


public interface CoursRestController {
    @GetMapping("/cours")//End Point
    ResponseEntity<Map<Object, Object>>  listerCours(
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "5") int size,
                          @RequestParam(required = false) EtatCours etat,
                          @RequestParam(required = false) Long classe,
                          @RequestParam(required = false) Long semestre,
                          @RequestParam(required = false) Long professeur
                         );

    @GetMapping("/cours/{id}")//End Point
    ResponseEntity<Map<Object, Object>>  showCours(@PathVariable Long id);

    @PutMapping("/cours/{id}")
    ResponseEntity<Map<Object, Object>> updateCours(@Valid @RequestBody String msg,
                                                     @PathVariable Long id, BindingResult bindingResult);

    @PostMapping("/cours")
    ResponseEntity<Map<Object, Object>> saveCours(@Valid @RequestBody CoursCreateRequestDto cours, BindingResult bindingResult);
}
