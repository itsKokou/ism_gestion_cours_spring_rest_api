package projet.restapi.web.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReinscriptionCreateRequestDto {
    @NotNull
    @Min(value = 1, message = "L'etudiant est obligatoire")
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
    @Min(value = 1, message = "La classe est obligatoire")
    private Long classe;
}
