package com.tiendas3b.almacen.shipment.dto;

import java.util.List;

public class RelationshipDto {
    private Long questionId;
    private List<Long> answers;

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public List<Long> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Long> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "RelationshipDto{" +
                "questionId=" + questionId +
                ", answers=" + answers +
                '}';
    }
}
