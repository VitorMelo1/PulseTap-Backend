package com.pulsetap.controller;

import com.pulsetap.model.User;
import com.pulsetap.service.AuthService;
import com.pulsetap.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private JwtService jwtService;
    
    // Endpoint para obter perfil do usuário
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String token) {
        try {
            // Remover "Bearer " do token
            String jwt = token.substring(7);
            
            // Extrair email do token
            String email = jwtService.extractUsername(jwt);
            
            // Buscar usuário
            Optional<User> userOptional = authService.findByEmail(email);
            
            if (userOptional.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Usuário não encontrado");
                return ResponseEntity.status(404).body(error);
            }
            
            User user = userOptional.get();
            
            // Criar resposta sem senha
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("email", user.getEmail());
            response.put("firstName", user.getFirstName());
            response.put("lastName", user.getLastName());
            response.put("fullName", user.getFullName());
            response.put("createdAt", user.getCreatedAt());
            response.put("lastLogin", user.getLastLogin());
            response.put("isActive", user.getIsActive());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Token inválido ou expirado");
            return ResponseEntity.status(401).body(error);
        }
    }
    
    // Endpoint para atualizar perfil do usuário
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, String> updateData) {
        try {
            // Remover "Bearer " do token
            String jwt = token.substring(7);
            
            // Extrair email do token
            String email = jwtService.extractUsername(jwt);
            
            // Buscar usuário
            Optional<User> userOptional = authService.findByEmail(email);
            
            if (userOptional.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Usuário não encontrado");
                return ResponseEntity.status(404).body(error);
            }
            
            User user = userOptional.get();
            
            // Atualizar campos se fornecidos
            if (updateData.containsKey("firstName")) {
                user.setFirstName(updateData.get("firstName"));
            }
            if (updateData.containsKey("lastName")) {
                user.setLastName(updateData.get("lastName"));
            }
            
            // Salvar usuário atualizado
            User updatedUser = authService.saveUser(user);
            
            // Criar resposta
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Perfil atualizado com sucesso!");
            response.put("user", updatedUser);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao atualizar perfil: " + e.getMessage());
            return ResponseEntity.status(400).body(error);
        }
    }
    
    // Endpoint para verificar token
    @GetMapping("/verify-token")
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String token) {
        try {
            // Remover "Bearer " do token
            String jwt = token.substring(7);
            
            // Verificar se token é válido
            String email = jwtService.extractUsername(jwt);
            Long userId = jwtService.extractUserId(jwt);
            String fullName = jwtService.extractFullName(jwt);
            
            Map<String, Object> response = new HashMap<>();
            response.put("valid", true);
            response.put("email", email);
            response.put("userId", userId);
            response.put("fullName", fullName);
            response.put("message", "Token válido");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("valid", false);
            error.put("error", "Token inválido ou expirado");
            return ResponseEntity.status(401).body(error);
        }
    }
}
