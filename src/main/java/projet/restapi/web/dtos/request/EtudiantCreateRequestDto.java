package projet.restapi.web.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import projet.core.data.entities.Etudiant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EtudiantCreateRequestDto {
    private Long id;
    private String matricule;
    @Email(message = "L'email est obligatoire")
    private String email;
    @Length(min = 4, message = "Minimum 4 caractères")
    private String password;
    @NotBlank(message = "Le nom complet est obligatoire")
    private String nomComplet;
    @NotBlank(message = "Le tuteur est obligatoire")
    private String tuteur;
    private Boolean isArchived;

    public Etudiant toEntity(){
        Etudiant etudiant =  Etudiant.builder()
                .tuteur(tuteur)
                .matricule(matricule)
                .build();
        etudiant.setLogin(email);
        etudiant.setPassword(password);
        etudiant.setNomComplet(nomComplet);
        etudiant.setIsArchived(isArchived);
        return etudiant;
    }
}
