package services;

import com.google.inject.Singleton;
import models.*;
import org.joda.time.LocalDate;
import play.data.DynamicForm;
import play.db.jpa.JPA;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class has a bunch of methods for working with users responses.
 */
@Singleton
public class ResponseService {

    public static final String CHECK_BOX_DELIMITER = ", ";

    public ResponseService() {
    }

    /**
     * Remove not active {@link Field} before they will get to a user.
     *
     * @param fields A {@code List} of {@link Field}.
     * @return A {@code List} of {@link Field} where all is not active {@link Field} have been removed.
     */
    public List<Field> removeIsNotActive(List<Field> fields) {
        return fields.stream().
                filter(field -> field.isActive).
                collect(Collectors.toList());
    }


    /**
     * Parse a dynamic form.
     *
     * @param form The form which should be parsed.
     * @return {@link UserAnswer} constructing from the form and ready for persisting.
     */
    public UserAnswer buildUserAnswer(DynamicForm form) {

        //create user answer
        UserAnswer userAnswer = new UserAnswer();

        //retrieve data from the dynamic form
        Map<String, String> data = form.data();

        for (Map.Entry<String, String> entry : data.entrySet()) {

            //the keys in the form have a such view 'field_id;field_type[]'
            //parse this keys and use this data to construct a user answer
            String[] split = entry.getKey().split("[;\\[]");

            Long field_id = Long.valueOf(split[0]);
            Type type = Type.valueOfCustom(split[1]);

            //make a proxy object which will be set to the user answer content
            //for binding with a Field entity
            Field field = JPA.em().getReference(Field.class, field_id);

            UserAnswerContent content = new UserAnswerContent();
            content.user = userAnswer;
            content.field = field;

            //according to the parsed field type choose a correct field in the user answer
            //to store the data
            switch (type) {
                case DATE:
                    //can be empty if it is not required
                    //avoiding to persist an empty line to the DB. Make this check for each data
                    if (!entry.getValue().isEmpty()) {
                        content.dateContent = LocalDate.parse(entry.getValue());
                        userAnswer.userAnswerContent.add(content);
                    }
                    break;
                case SLIDER:
                    //chose the 'Byte' to store a slider value because of the range 0 - 100
                    if (!entry.getValue().isEmpty()) {
                        content.sliderContent = Byte.valueOf(entry.getValue());
                        userAnswer.userAnswerContent.add(content);
                    }
                    break;
                default:
                    if (!entry.getValue().isEmpty()) {
                        content.stringContent = entry.getValue();
                        userAnswer.userAnswerContent.add(content);
                    }
                    break;
            }
        }
        return userAnswer;
    }

    /**
     * Construct a {@link UserAnswer} {@code List}
     *
     * @param fields {@link Field} which is actual now.
     * @param users  A {@link UserAnswer} list from DB
     * @return The {@code List} of {@link UsersAnswerDto} that has been constructed from {@link UserAnswer}
     */
    public List<UsersAnswerDto> buildUserAnswerDto(List<Field> fields, List<UserAnswer> users) {

        //create a result list
        List<UsersAnswerDto> usersAnswers = new ArrayList<>(users.size());

        //go through users list
        for (UserAnswer user : users) {

            //create dto for each one user
            UsersAnswerDto answerDto = new UsersAnswerDto();

            //create a list for convenient iteration
            List<UserAnswerContent> answers = user.userAnswerContent.
                    stream().
                    collect(Collectors.toList());

            //if admin deleted some field
            removeAnswersWithNullField(answers);

            //go through the actual fields
            for (Field field : fields) {

                //need for adding 'N/A'
                boolean isMatch = false;

                //just a bit optimization if the user does not have required answers anymore
                if (answers.size() > 0) {

                    //need this kind of loop because the match answers will be deleted
                    //for not checking them again at the next iteration
                    for (int i = 0; i < answers.size(); i++) {

                        //get answer content
                        UserAnswerContent content = answers.get(i);

                        //is this answer to that field?
                        if (field.id.equals(content.field.id)) {

                            //yes. Looking for a type matching
                            //when the match is found get the content from a particular attribute
                            //and then delete this answer from the answers list
                            switch (field.fieldType) {
                                case DATE:
                                    answerDto.answer.add(content.dateContent.toString());
                                    answers.remove(0);
                                    isMatch = true;
                                    break;
                                case SLIDER:
                                    answerDto.answer.add(content.sliderContent.toString() + "%");
                                    answers.remove(0);
                                    isMatch = true;
                                    break;
                                case CHECK_BOX:
                                    //grouping a check box answers
                                    collectCheckBox(field.id, answers, answerDto);
                                    isMatch = true;
                                    break;
                                default:
                                    answerDto.answer.add(content.stringContent);
                                    answers.remove(0);
                                    isMatch = true;
                                    break;
                            }

                            break;//find the match. Break the answers iteration
                        }
                    }
                }
                //no one answer is match. Add 'N/A'
                if (!isMatch) {
                    answerDto.answer.add("N/A");
                }
            }
            //add DTO to the list
            usersAnswers.add(answerDto);
        }
        return usersAnswers;
    }

    /**
     * If an {@link Admin} delete a {@link Field} a {@link UserAnswerContent} attribute 'field'
     * would be 'null'. So have to discard these answers.
     *
     * @param answers A {@code List} of {@link UserAnswerContent}
     */
    private void removeAnswersWithNullField(List<UserAnswerContent> answers) {
        for (int i = 0; i < answers.size(); ) {
            if (answers.get(i).field == null) {
                answers.remove(i);
            } else {
                i++;
            }
        }
    }

    /**
     * Construct a nice representation of the 'CHECK_BOX' {@link Type}
     *
     * @param field_id  A {@link Field} id which {@link Type} is 'CHECK_BOX'
     * @param answers   A {@code List} of the {@link UserAnswerContent}
     * @param answerDto A {@link UsersAnswerDto} where {@link UserAnswerContent} will be added
     */
    private void collectCheckBox(Long field_id, List<UserAnswerContent> answers, UsersAnswerDto answerDto) {
        String checkBoxStr = "";

        while (answers.size() > 0 && answers.get(0).field.id.equals(field_id)) {
            checkBoxStr += answers.get(0).stringContent + CHECK_BOX_DELIMITER;
            answers.remove(0);
        }

        //use substring for removing 'CHECK_BOX_DELIMITER' at the end of the string
        answerDto.answer.add(checkBoxStr.substring(0, checkBoxStr.length() - 2));
    }
}
