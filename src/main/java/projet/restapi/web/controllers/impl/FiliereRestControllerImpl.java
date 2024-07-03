package projet.restapi.web.controllers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projet.core.data.dto.FiliereResponseDto;
import projet.core.data.dto.NiveauResponseDto;
import projet.core.data.dto.SemestreResponseDto;
import projet.core.data.entities.Filiere;
import projet.core.data.entities.Niveau;
import projet.core.data.entities.Semestre;
import projet.core.services.FiliereService;
import projet.core.services.NiveauService;
import projet.restapi.web.controllers.FiliereRestController;
import projet.restapi.web.controllers.NiveauRestController;
import projet.restapi.web.dtos.RestResponse;

import java.util.Map;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class FiliereRestControllerImpl implements FiliereRestController {
    private final FiliereService filiereService;

    @Override
    public ResponseEntity<Map<Object, Object>> listerFiliere() {
        Page<Filiere> datas = filiereService.getAll(PageRequest.of(0,100));
        Page<FiliereResponseDto> dataDtos = datas.map(FiliereResponseDto::toDto);
        Map<Object,Object> model = RestResponse.response(dataDtos.getContent(), HttpStatus.OK);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> showFiliere(Long id) {
        Filiere filiere = filiereService.show(id).orElse(null);
        Map<Object, Object> response;
        if (filiere == null){
            response= RestResponse.response(null,HttpStatus.NO_CONTENT);
        }else {
            response= RestResponse.response(FiliereResponseDto.toDto(filiere),HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
