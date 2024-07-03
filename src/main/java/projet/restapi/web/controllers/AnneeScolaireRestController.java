package projet.restapi.web.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import projet.restapi.web.dtos.request.AnneeScolaireCreateRequestDto;

import java.util.Map;


public interface AnneeScolaireRestController {
    @GetMapping("/annees/scolaires")//End Point
    ResponseEntity<Map<Object, Object>>  listerAnneeScolaire(
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "5") int size
                         );
    @GetMapping("/annees/scolaires/all")//End Point
    ResponseEntity<Map<Object, Object>>  getAllAnneeScolaire();

    @GetMapping("/annees/scolaires/{id}")//End Point
    ResponseEntity<Map<Object, Object>>  showAnneeScolaire(@PathVariable Long id);

    @PutMapping("/annees/scolaires/{id}")
    ResponseEntity<Map<Object, Object>> updateAnneeScolaire(@Valid @RequestBody AnneeScolaireCreateRequestDto annee,
                                                     @PathVariable Long id, BindingResult bindingResult);

    @PostMapping("/annees/scolaires")
    ResponseEntity<Map<Object, Object>> saveAnneeScolaire(@Valid @RequestBody AnneeScolaireCreateRequestDto annee, BindingResult bindingResult);
}
