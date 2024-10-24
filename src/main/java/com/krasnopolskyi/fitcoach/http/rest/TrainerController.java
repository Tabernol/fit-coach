package com.krasnopolskyi.fitcoach.http.rest;

import com.krasnopolskyi.fitcoach.dto.request.TrainerDto;
import com.krasnopolskyi.fitcoach.dto.request.UserCredentials;
import com.krasnopolskyi.fitcoach.dto.response.TrainerProfileDto;
import com.krasnopolskyi.fitcoach.exception.EntityException;
import com.krasnopolskyi.fitcoach.service.TrainerService;
import com.krasnopolskyi.fitcoach.validation.Create;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/trainers")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;


//    @GetMapping()
//    public ResponseEntity<List<CoachDto>> getAll() {
//        return ResponseEntity.status(HttpStatus.OK).body(coachService.getAll());
//    }
//
    @GetMapping("/{username}")
    public ResponseEntity<TrainerProfileDto> getCoach(@PathVariable("username") String username) throws EntityException {
        return ResponseEntity.status(HttpStatus.OK).body(trainerService.findByUsername(username));
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserCredentials> createCoach(@Validated(Create.class) @RequestBody TrainerDto trainerDto) throws EntityException {
        return ResponseEntity.status(HttpStatus.CREATED).body(trainerService.save(trainerDto));
    }
}
