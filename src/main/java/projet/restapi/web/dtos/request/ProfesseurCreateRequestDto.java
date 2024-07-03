package projet.restapi.web.dtos.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import projet.core.data.entities.Etudiant;
import projet.core.data.entities.Professeur;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfesseurCreateRequestDto {
    private Long id;
    @Email(message = "L'email est obligatoire")
    private String email;
    @Length(min = 4, message = "Minimum 4 caractères")
    private String password;
    @NotBlank(message = "Le nom complet est obligatoire")
    private String nomComplet;
    @NotBlank(message = "Le téléphone est obligatoire")
    private String portable;
    @NotBlank(message = "Le grade est obligatoire")
    private String grade;
    protected Boolean isPlanned;
    private Boolean isArchived;

    public Professeur toEntity(){
        Professeur professeur =  Professeur.builder()
                .portable(portable)
                .grade(grade)
                .isPlanned(isPlanned)
                .build();
        professeur.setLogin(email);
        professeur.setPassword(password);
        professeur.setNomComplet(nomComplet);
        professeur.setIsArchived(isArchived);
        return professeur;
    }
}
