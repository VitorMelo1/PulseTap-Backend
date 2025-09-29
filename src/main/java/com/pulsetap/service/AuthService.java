package com.pulsetap.service;

import com.pulsetap.dto.AuthResponse;
import com.pulsetap.dto.LoginRequest;
import com.pulsetap.dto.RegisterRequest;
import com.pulsetap.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    
    @Autowired
    private JsonStorageService jsonStorageService;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // Registrar novo usuário
    public AuthResponse register(RegisterRequest request) {
        System.out.println("=== REGISTER SERVICE DEBUG ===");
        System.out.println("Email: " + request.getEmail());
        System.out.println("Password: " + (request.getPassword() != null ? "[PROTECTED]" : "NULL"));
        System.out.println("Password length: " + (request.getPassword() != null ? request.getPassword().length() : 0));
        
        // Verificar se email já existe
        if (jsonStorageService.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email já está em uso");
        }
        
        // Criar novo usuário
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        System.out.println("Encoded password: " + (encodedPassword != null ? "[ENCODED]" : "NULL"));
        user.setPassword(encodedPassword);
        
        // Salvar usuário
        User savedUser = jsonStorageService.saveUser(user);
        System.out.println("Saved user ID: " + savedUser.getId());
        System.out.println("Saved user password: " + (savedUser.getPassword() != null ? "[SAVED]" : "NULL"));
        
        // Gerar token JWT
        String token = jwtService.generateTokenWithUserInfo(
                savedUser.getEmail(),
                savedUser.getId(),
                savedUser.getFullName()
        );
        
        return new AuthResponse(savedUser, token, "Usuário registrado com sucesso!");
    }
    
    // Fazer login
    public AuthResponse login(LoginRequest request) {
        // Buscar usuário por email
        Optional<User> userOptional = jsonStorageService.findByEmail(request.getEmail());
        
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Credenciais inválidas");
        }
        
        User user = userOptional.get();
        
        // Verificar se usuário está ativo
        if (!user.getIsActive()) {
            throw new RuntimeException("Usuário inativo");
        }
        
        // Verificar senha
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciais inválidas");
        }
        
        // Atualizar último login
        user.updateLastLogin();
        jsonStorageService.saveUser(user);
        
        // Gerar token JWT
        String token = jwtService.generateTokenWithUserInfo(
                user.getEmail(),
                user.getId(),
                user.getFullName()
        );
        
        return new AuthResponse(user, token, "Login realizado com sucesso!");
    }
    
    // Buscar usuário por email
    public Optional<User> findByEmail(String email) {
        return jsonStorageService.findByEmail(email);
    }
    
    // Buscar usuário por ID
    public Optional<User> findById(Long id) {
        return jsonStorageService.findById(id);
    }
    
    // Verificar se email existe
    public boolean existsByEmail(String email) {
        return jsonStorageService.existsByEmail(email);
    }
    
    // Listar todos os usuários
    public java.util.List<User> findAllUsers() {
        return jsonStorageService.findAll();
    }
    
    // Salvar usuário (para atualizações)
    public User saveUser(User user) {
        return jsonStorageService.saveUser(user);
    }
}
