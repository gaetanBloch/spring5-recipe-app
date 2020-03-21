package guru.springframework.spring5recipeapp.commands;

import lombok.*;

/**
 * @author Gaetan Bloch
 * Created on 21/03/2020
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotesCommand {
    private Long id;
    private String recipeNotes;
}
