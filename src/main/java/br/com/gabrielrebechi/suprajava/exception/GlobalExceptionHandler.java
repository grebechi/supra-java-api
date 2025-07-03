//package br.com.gabrielrebechi.suprajava.exception;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//import java.time.LocalDateTime;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<Object> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest request) {
//        String mensagem = "Violação de integridade no banco de dados.";
//
//        Throwable causa = ex.getRootCause();
//        if (causa != null && causa.getMessage() != null) {
//            String erro = causa.getMessage().toLowerCase();
//
//            if (erro.contains("violates foreign key constraint") && erro.contains("produto")) {
//                mensagem = "Não é possível excluir a unidade de medida: ela está vinculada a um ou mais produtos.";
//            } else if (erro.contains("duplicate key")) {
//                mensagem = "Erro: valor já está sendo utilizado e deve ser único.";
//            } else {
//                mensagem = "Violação de integridade: " + causa.getMessage();
//            }
//        }
//
//        return buildErrorResponse(
//                HttpStatus.BAD_REQUEST,
//                mensagem,
//                request.getRequestURI()
//        );
//    }
//
//
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
//        return buildErrorResponse(
//                HttpStatus.BAD_REQUEST,
//                ex.getMessage(),
//                request.getRequestURI()
//        );
//    }
//
////    @ExceptionHandler(Exception.class)
////    public ResponseEntity<Object> handleGeneric(Exception ex, HttpServletRequest request) {
////        return buildErrorResponse(
////                HttpStatus.INTERNAL_SERVER_ERROR,
////                "Erro inesperado. Contate o administrador do sistema.",
////                request.getRequestURI()
////        );
////    }
//
//    private ResponseEntity<Object> buildErrorResponse(HttpStatus status, String mensagem, String path) {
//        Map<String, Object> corpo = new LinkedHashMap<>();
//        corpo.put("timestamp", LocalDateTime.now());
//        corpo.put("status", status.value());
//        corpo.put("erro", status.getReasonPhrase());
//        corpo.put("mensagem", mensagem);
//        corpo.put("path", path);
//
//        return ResponseEntity.status(status).body(corpo);
//    }
//}
