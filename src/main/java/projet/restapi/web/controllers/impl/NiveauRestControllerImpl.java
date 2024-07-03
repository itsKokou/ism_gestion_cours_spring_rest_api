package projet.restapi.web.controllers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projet.core.data.dto.NiveauResponseDto;
import projet.core.data.dto.SemestreResponseDto;
import projet.core.data.entities.Niveau;
import projet.core.data.entities.Semestre;
import projet.core.services.NiveauService;
import projet.core.services.SemestreService;
import projet.restapi.web.controllers.NiveauRestController;
import projet.restapi.web.controllers.SemestreRestController;
import projet.restapi.web.dtos.RestResponse;
import projet.restapi.web.dtos.request.SemestreCreateRequestDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class NiveauRestControllerImpl implements NiveauRestController {
    private final NiveauService niveauService;

    @Override
    public ResponseEntity<Map<Object, Object>> listerNiveau() {
        Page<Niveau> datas = niveauService.getAll(PageRequest.of(0,100));
        Page<NiveauResponseDto> dataDtos = datas.map(NiveauResponseDto::toDto);
        Map<Object,Object> model = RestResponse.response(dataDtos.getContent(), HttpStatus.OK);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> showNiveau(Long id) {
        Niveau niveau = niveauService.show(id).orElse(null);
        Map<Object, Object> response;
        if (niveau == null){
            response= RestResponse.response(null,HttpStatus.NO_CONTENT);
        }else {
            response= RestResponse.response(NiveauResponseDto.toDto(niveau),HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
