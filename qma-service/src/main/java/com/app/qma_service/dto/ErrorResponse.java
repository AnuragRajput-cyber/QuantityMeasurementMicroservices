package com.app.qma_service.dto;

import java.time.OffsetDateTime;

public record ErrorResponse(
        OffsetDateTime timestamp,
        int status,
        String error
) {
}
