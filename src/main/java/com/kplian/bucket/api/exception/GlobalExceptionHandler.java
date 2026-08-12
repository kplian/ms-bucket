package com.kplian.bucket.api.exception;

import com.kplian.bucket.api.service.I18nService;
import com.kplian.bucket.domain.exception.BusinessException;
import com.kplian.bucket.domain.exception.InfrastructureException;
import com.kplian.bucket.domain.exception.SystemException;
// import io.opentelemetry.api.trace.Span;
// import io.opentelemetry.api.trace.StatusCode;
// import io.opentelemetry.api.trace.Tracer;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.Map;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    private static final Logger LOG = Logger.getLogger(GlobalExceptionHandler.class);

    @Inject
    I18nService i18nService;

    // @Inject
    // Tracer tracer;

    @Override
    public Response toResponse(Exception exception) {
        Map<String, Object> errorResponse = new HashMap<>();

        if (exception instanceof BusinessException) {
            BusinessException be = (BusinessException) exception;
            LOG.debugf("Business Exception: %s", be.getMessage());
            errorResponse.put("code", be.getCode());
            errorResponse.put("message", be.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }

        if (exception instanceof InfrastructureException) {
            InfrastructureException ie = (InfrastructureException) exception;
            LOG.error("Infrastructure Exception", ie);
            errorResponse.put("code", ie.getCode());
            errorResponse.put("message", i18nService.get("error.infrastructure.generic", ie.getMessage()));
            
            // recordErrorInOpenTelemetry("infrastructure", ie.getCode(), ie.getMessage(), ie);
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
        }

        if (exception instanceof SystemException) {
            SystemException se = (SystemException) exception;
            LOG.error("System Exception", se);
            errorResponse.put("code", se.getCode());
            errorResponse.put("message", i18nService.get("error.system.generic", se.getMessage()));
            
            // recordErrorInOpenTelemetry("system", se.getCode(), se.getMessage(), se);
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
        }

        // Generic fallback error
        LOG.error("Unhandled Exception", exception);
        errorResponse.put("code", "INTERNAL_SERVER_ERROR");
        errorResponse.put("message", i18nService.get("error.system.unexpected"));

        // recordErrorInOpenTelemetry("system", "INTERNAL_SERVER_ERROR", exception.getMessage(), exception);

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
    }

    // private void recordErrorInOpenTelemetry(String type, String code, String message, Exception exception) {
    //     Span span = tracer.spanBuilder("error-handler").startSpan();
    //     try {
    //         span.setAttribute("error", true);
    //         span.setAttribute("error.type", type);
    //         span.setAttribute("error.code", code);
    //         span.setAttribute("error.message", message != null ? message : "Unknown error");
    //         span.setAttribute("error.class", exception.getClass().getName());
    //         
    //         span.setStatus(StatusCode.ERROR, message);
    //         span.recordException(exception);
    //     } finally {
    //         span.end();
    //     }
    // }
}
