package com.tiendas3b.almacen.shipment.dto;

import com.tiendas3b.almacen.db.dao.Answer;
import com.tiendas3b.almacen.db.dao.Form;
import com.tiendas3b.almacen.db.dao.Question;

import java.util.List;

public class QuestionnairesDto {
    private List<FormDto> form;
    private List<Question> questions;
    private List<Answer> answers;

    public List<FormDto> getForm() {
        return form;
    }

    public void setForms(List<FormDto> forms) {
        this.form = forms;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "QuestionnairesDto{" +
                "forms=" + form +
                ", questions=" + questions +
                ", answers=" + answers +
                '}';
    }
}
