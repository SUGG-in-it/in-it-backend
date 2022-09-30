package com.example.initbackend.questionTag.domain;
import lombok.*;
import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "question_id", nullable = false)
//    private Question question;
//    @ManyToOne
//    @JoinColumn(name = "tag_id", nullable = false)
//    private Tag tag;
    @Column
    private Long questionId;

    @Column
    private Long tagId;

    @Builder
    public QuestionTag(Long questionId, Long tagId) {
        this.questionId = questionId;
        this.tagId = tagId;
    }


}