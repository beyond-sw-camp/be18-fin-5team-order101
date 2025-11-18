package com.synerge.order101.notification.controller;

import com.synerge.order101.notification.model.service.NotificationSseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sse")
public class SseController {
    private final NotificationSseService sseService;
//    private final JwtTokenProvider jwtTokenProvider; // JWT 쓸 때만

    @GetMapping(value = "/notifications", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@RequestParam(required = false) String token,
                                @RequestHeader(value = "Last-Event-ID", required = false) String lastEventId) {

        String loginId = null;

        // 2) 없으면 쿼리 파라미터의 JWT로 인증 (EventSource는 Authorization 헤더를 못 보냄)
//        if (loginId == null && token != null && !token.isBlank()) {
//            Authentication jwtAuth = jwtTokenProvider.getAuthentication(token);
//            loginId = jwtAuth.getName(); // UserDetails#getUsername() == loginId
//        }

//        if (loginId == null) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No auth for SSE");
//        }
//
//        // (선택) 캐시 방지 헤더는 Spring이 기본 세팅하지만 필요시 필터에서 추가
        return sseService.subscribe(loginId, lastEventId);
    }
}
