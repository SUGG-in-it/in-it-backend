package com.example.initbackend.answer.service;

import com.example.initbackend.answer.domain.Answer;
import com.example.initbackend.answer.dto.CreateAnswerRequestDto;
import com.example.initbackend.answer.repository.AnswerRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Getter
@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    public void createAnswer(CreateAnswerRequestDto createAnswerRequestDto){
        if (isDuplicatedAnswer(createAnswerRequestDto.getUserId(),createAnswerRequestDto.getQuestionId())){
            throw new IllegalArgumentException("This user write answer in this question");
        }

        // ** 유저와 해당 질문이 db에 있는지 확인 해야하는 로직 추가해야함.**

        
        // ********************************************************

        Answer answer = createAnswerRequestDto.toEntity();
        answerRepository.save(answer);
    }

    private boolean isDuplicatedAnswer(Long userId, Long questionId ) {
        return answerRepository.findByUserIdAndQuestionId(userId,questionId).isPresent();
    }
}
