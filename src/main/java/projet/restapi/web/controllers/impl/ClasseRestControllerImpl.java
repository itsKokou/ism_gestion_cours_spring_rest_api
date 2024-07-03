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
import projet.core.data.entities.*;
import projet.core.data.entities.Module;
import projet.core.exceptions.EntityNotFoundException;
import projet.core.services.*;
import projet.restapi.web.controllers.ClasseRestController;
import projet.restapi.web.dtos.RestResponse;
import projet.restapi.web.dtos.request.ClasseCreateRequestDto;
import projet.restapi.web.dtos.response.ClasseResponseDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class ClasseRestControllerImpl implements ClasseRestController {
    private final ClasseService classeService;
    private final NiveauService niveauService;
    private final FiliereService filiereService;
    private final ProfesseurService professeurService;
    private final ModuleService moduleService;
    private final SemestreService semestreService;

    @Override
    public ResponseEntity<Map<Object, Object>> listerClasse(int page, int size, Long niveau, Long filiere,Boolean planifier) {
        Niveau niv = niveauService.show(niveau).orElse(null);
        Filiere fil = filiereService.show(filiere).orElse(null);
        Page<Classe> datas = classeService.getAllByNiveauAndFiliereAndPlanned(PageRequest.of(page,size),niv,fil,planifier);
        Page<ClasseResponseDto> dataDtos = datas.map(ClasseResponseDto::toDto);
        Map<Object, Object> model = RestResponse.paginateResponse(
                dataDtos.getContent(), new int[dataDtos.getTotalPages()], dataDtos.getNumber(),dataDtos.hasPrevious(),
                dataDtos.hasNext(), dataDtos.getTotalElements(), dataDtos.getTotalPages(), HttpStatus.OK);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> getAllClasse() {
        Page<Classe> datas = classeService.getAllByNiveauAndFiliereAndPlanned(PageRequest.of(0,1000),null,null,true);
        Page<ClasseResponseDto> dataDtos = datas.map(ClasseResponseDto::toDto);
        Map<Object, Object> model = RestResponse.response(dataDtos.getContent(),HttpStatus.OK);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> getClasseByProfesseurAndModule(Long idModule, Long idProfesseur, Long idSemestre) {
        Professeur prof = professeurService.show(idProfesseur)
                .orElseThrow(()->new EntityNotFoundException("Professeur non trouvé"));
        Module module = moduleService.show(idModule)
                .orElseThrow(()->new EntityNotFoundException("Module non trouvé"));
        Semestre semestre = semestreService.show(idSemestre)
                .orElseThrow(()->new EntityNotFoundException("Semestre non trouvé"));
        Page<Classe> datas = classeService.getByProfesseurAndModuleAndSemestre(prof,module,semestre,PageRequest.of(0,1000));
        Page<ClasseResponseDto> dataDtos = datas.map(ClasseResponseDto::toDto);
        Map<Object, Object> model = RestResponse.response(dataDtos.getContent(),HttpStatus.OK);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> showClasse(Long id) {
        Classe classe = classeService.show(id).orElse(null);
        Map<Object, Object> response;
        if (classe == null){
            response= RestResponse.response(null,HttpStatus.NO_CONTENT);
        }else {
            response= RestResponse.response(ClasseResponseDto.toDto(classe),HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> updateClasse(ClasseCreateRequestDto classe, Long id, BindingResult bindingResult) {
        Map<Object, Object> response;
        if (bindingResult.hasErrors()){
            Map<String, String> errors =new HashMap<>();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            fieldErrors.forEach(fieldError -> errors.put(fieldError.getField(),fieldError.getDefaultMessage()));
            response= RestResponse.response(errors, HttpStatus.NOT_FOUND);
        }else{
            Niveau niveau = niveauService.show(classe.getNiveau())
                    .orElseThrow(()->new EntityNotFoundException("Niveau not found!"));
            Filiere filiere = filiereService.show(classe.getFiliere())
                    .orElseThrow(()->new EntityNotFoundException("Filiere not found!"));
            Classe classeCree = classe.toEntity(niveau,filiere);
            classeCree.setId(id);
            classeService.save(classeCree);
            response= RestResponse.response(classe,HttpStatus.CREATED);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> saveClasse(ClasseCreateRequestDto classe, BindingResult bindingResult) {
        Map<Object, Object> response;
        if (bindingResult.hasErrors()){
            Map<String, String> errors =new HashMap<>();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            fieldErrors.forEach(fieldError -> errors.put(fieldError.getField(),fieldError.getDefaultMessage()));
            response= RestResponse.response(errors, HttpStatus.NOT_FOUND);
        }else{
            try {
                Niveau niveau = niveauService.show(classe.getNiveau())
                        .orElseThrow(()->new EntityNotFoundException("Niveau not found!"));
                Filiere filiere = filiereService.show(classe.getFiliere())
                        .orElseThrow(()->new EntityNotFoundException("Filiere not found!"));
                Classe classeCree = classe.toEntity(niveau,filiere);
                classeService.save(classeCree);
                response= RestResponse.response(classe,HttpStatus.CREATED);
            }catch (Exception e) {
                response= RestResponse.response(classe,HttpStatus.CONFLICT);
                System.out.println(e.getMessage());
            }

        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> PlanifierClasse(Long id) {
        Classe classe = classeService.show(id)
                .orElseThrow(()->new EntityNotFoundException("Classe n'existe pas"));
        classe.setIsPlanned(true);
        classeService.save(classe);
        Map<Object, Object> response = RestResponse.response("Planifié avec succes",HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
