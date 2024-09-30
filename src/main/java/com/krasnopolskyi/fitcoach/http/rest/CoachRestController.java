package com.krasnopolskyi.fitcoach.http.rest;

import com.krasnopolskyi.fitcoach.dto.RegistrationResponse;
import com.krasnopolskyi.fitcoach.dto.coach.CoachCreateRequest;
import com.krasnopolskyi.fitcoach.dto.coach.CoachDto;
import com.krasnopolskyi.fitcoach.exception.UserNotFoundException;
import com.krasnopolskyi.fitcoach.service.CoachService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coaches")
public class CoachRestController {

    private final CoachService coachService;

    public CoachRestController(CoachService coachService) {
        this.coachService = coachService;
    }

    @GetMapping()
    public ResponseEntity<List<CoachDto>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(coachService.getAll());
    }

    @GetMapping("/{username}")
    public ResponseEntity<CoachDto> getCoach(@PathVariable("username") String username) throws UserNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(coachService.get(username));
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RegistrationResponse> createCoach(@RequestBody CoachCreateRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(coachService.create(request));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteCoach(@PathVariable("username")String username) {
        return coachService.delete(username) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
