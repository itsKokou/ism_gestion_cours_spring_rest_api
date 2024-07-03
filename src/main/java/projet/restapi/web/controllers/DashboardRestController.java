package projet.restapi.web.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import projet.restapi.web.dtos.request.SalleCreateRequestDto;

import java.util.Map;


public interface DashboardRestController {
    @GetMapping("/homes/professeur/{id}")//End Point
    ResponseEntity<Map<Object, Object>>  profHome(@PathVariable Long id);

    @GetMapping("/homes/rp/{id}")//End Point
    ResponseEntity<Map<Object, Object>>  rpHome(@PathVariable Long id);

}
