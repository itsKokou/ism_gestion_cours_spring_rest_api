package projet.restapi.web.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import projet.restapi.web.dtos.request.InscriptionCreateRequestDto;
import projet.restapi.web.dtos.request.ReinscriptionCreateRequestDto;

import java.util.Date;
import java.util.Map;


public interface InscriptionRestController {
    @GetMapping("/inscriptions")//End Point
    ResponseEntity<Map<Object, Object>>  listerInscription(
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "5") int size,
                          @RequestParam Long annee,
                          @RequestParam(required = false) Long classe,
                          @RequestParam(required = false) String date
                         );

    @PutMapping("/inscriptions/{id}")
    ResponseEntity<Map<Object, Object>> updateInscription(@Valid @RequestBody String msg,
                                                     @PathVariable Long id, BindingResult bindingResult);

    @PostMapping("/inscriptions")
    ResponseEntity<Map<Object, Object>> saveInscription(@Valid @RequestBody InscriptionCreateRequestDto inscription, BindingResult bindingResult);

    @PostMapping("/inscriptions/reinscription")
    ResponseEntity<Map<Object, Object>> makeReinscription(@Valid @RequestBody ReinscriptionCreateRequestDto reinscription, BindingResult bindingResult);

}
