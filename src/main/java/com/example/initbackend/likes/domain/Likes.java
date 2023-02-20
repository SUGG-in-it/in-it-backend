package com.example.initbackend.likes.domain;

import com.example.initbackend.question.domain.Question;
import com.example.initbackend.user.domain.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames={"question_id", "user_id"}))
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="question_id")
    private Question questionId;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User userId;

    @CreationTimestamp
    private Timestamp createDate;

    @UpdateTimestamp
    private Timestamp updateDate;

    @Builder
    public Likes(Question question, User user){
        this.questionId = question;
        this.userId = user;
    }

}
