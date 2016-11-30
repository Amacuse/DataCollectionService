package models;

import java.util.ArrayList;
import java.util.List;
/**
 * A DTO for a {@link UserAnswer} which would be populated by
 * 'N/A' values if the user did not give the answers for a not required questions.
 */
public class UsersAnswerDto {

    public List<String> answer = new ArrayList<>();

    public UsersAnswerDto() {

    }
}
