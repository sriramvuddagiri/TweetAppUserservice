package com.tweetapp.model;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseForIssue {
   
    String message;
    LocalDateTime timestamp;
    HttpStatus status;
}