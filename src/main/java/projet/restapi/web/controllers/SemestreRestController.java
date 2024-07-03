package projet.restapi.web.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import projet.restapi.web.dtos.request.SemestreCreateRequestDto;

import java.util.Map;


public interface SemestreRestController {
    @GetMapping("/semestres")//End Point
    ResponseEntity<Map<Object, Object>>  listerSemestre(
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "5") int size
                         );

    @GetMapping("/semestres/all")//End Point
    ResponseEntity<Map<Object, Object>>  getAllSemestre();

    @GetMapping("/semestres/{id}")//End Point
    ResponseEntity<Map<Object, Object>>  showSemestre(@PathVariable Long id);

    @PutMapping("/semestres/{id}")
    ResponseEntity<Map<Object, Object>> updateSemestre(@Valid @RequestBody SemestreCreateRequestDto semestre,
                                                     @PathVariable Long id, BindingResult bindingResult);

    @PostMapping("/semestres")
    ResponseEntity<Map<Object, Object>> saveSemestre(@Valid @RequestBody SemestreCreateRequestDto semestre, BindingResult bindingResult);
}
