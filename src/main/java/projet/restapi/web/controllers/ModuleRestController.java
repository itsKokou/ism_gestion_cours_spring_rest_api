package projet.restapi.web.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import projet.restapi.web.dtos.request.ModuleCreateRequestDto;

import java.util.Map;


public interface ModuleRestController {
    @GetMapping("/modules")//End Point
    ResponseEntity<Map<Object, Object>>  listerModule(
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "5") int size
                         );

    @GetMapping("/modules/all")//End Point
    ResponseEntity<Map<Object, Object>>  getAllModule();

    @GetMapping("/modules/{id}")//End Point
    ResponseEntity<Map<Object, Object>>  showModule(@PathVariable Long id);

    @PutMapping("/modules/{id}")
    ResponseEntity<Map<Object, Object>> updateModule(@Valid @RequestBody ModuleCreateRequestDto module,
                                                     @PathVariable Long id, BindingResult bindingResult);

    @PostMapping("/modules")
    ResponseEntity<Map<Object, Object>> saveModule(@Valid @RequestBody ModuleCreateRequestDto module, BindingResult bindingResult);
}
