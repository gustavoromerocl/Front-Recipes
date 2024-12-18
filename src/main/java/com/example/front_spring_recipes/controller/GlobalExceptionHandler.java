package com.example.front_spring_recipes.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.UUID;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_ATTRIBUTE = "error"; // Definir constante para "error"

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception ex, Model model) {
        // Generar un ID de error único para el seguimiento sin revelar detalles
        String errorId = UUID.randomUUID().toString();
        model.addAttribute(ERROR_ATTRIBUTE, "Se ha producido un error inesperado. Intente nuevamente más tarde.");
        model.addAttribute("errorId", errorId);

        // Registro del error solo para el equipo de desarrollo
        ex.printStackTrace(); // Puedes reemplazarlo con un logger de producción

        return ERROR_ATTRIBUTE; // Enviar a la página de error genérica
    }

    // Endpoint genérico para cualquier ruta que produzca error
    @GetMapping("/error")
    public ResponseEntity<String> handleError() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.TEXT_HTML)
                .body("<html><body><h3>Se ha producido un error inesperado.</h3></body></html>");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleMissingResource(Exception ex, Model model) {
        model.addAttribute(ERROR_ATTRIBUTE, "Resource not found.");
        return ERROR_ATTRIBUTE; // Return a user-friendly error page
    }

}
