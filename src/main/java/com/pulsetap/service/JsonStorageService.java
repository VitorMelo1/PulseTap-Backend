package com.pulsetap.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pulsetap.model.User;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class JsonStorageService {
    
    private static final String USERS_FILE = System.getProperty("user.dir") + "/users.json";
    private static final String COUNTER_FILE = System.getProperty("user.dir") + "/user_counter.json";
    private final ObjectMapper objectMapper;
    private final AtomicLong userCounter;
    
    public JsonStorageService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        this.userCounter = new AtomicLong(loadUserCounter());
    }
    
    // Salvar usuário
    public User saveUser(User user) {
        try {
            List<User> users = loadUsers();
            
            // Se é um novo usuário, definir ID
            if (user.getId() == null) {
                user.setId(userCounter.incrementAndGet());
            }
            
            // Verificar se já existe usuário com este email
            Optional<User> existingUser = users.stream()
                    .filter(u -> u.getEmail().equals(user.getEmail()))
                    .findFirst();
            
            if (existingUser.isPresent()) {
                // Atualizar usuário existente
                User existing = existingUser.get();
                existing.setFirstName(user.getFirstName());
                existing.setLastName(user.getLastName());
                existing.setPassword(user.getPassword());
                existing.setIsActive(user.getIsActive());
                existing.setUpdatedAt(user.getUpdatedAt());
                existing.setLastLogin(user.getLastLogin());
            } else {
                // Adicionar novo usuário
                users.add(user);
            }
            
            saveUsers(users);
            saveUserCounter(userCounter.get());
            
            return user;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar usuário: " + e.getMessage(), e);
        }
    }
    
    // Buscar usuário por email
    public Optional<User> findByEmail(String email) {
        try {
            List<User> users = loadUsers();
            return users.stream()
                    .filter(user -> user.getEmail().equals(email))
                    .findFirst();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao buscar usuário: " + e.getMessage(), e);
        }
    }
    
    // Buscar usuário por ID
    public Optional<User> findById(Long id) {
        try {
            List<User> users = loadUsers();
            return users.stream()
                    .filter(user -> user.getId().equals(id))
                    .findFirst();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao buscar usuário: " + e.getMessage(), e);
        }
    }
    
    // Listar todos os usuários
    public List<User> findAll() {
        try {
            return loadUsers();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao listar usuários: " + e.getMessage(), e);
        }
    }
    
    // Verificar se email já existe
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }
    
    // Carregar usuários do arquivo JSON
    private List<User> loadUsers() throws IOException {
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        return objectMapper.readValue(file, new TypeReference<List<User>>() {});
    }
    
    // Salvar usuários no arquivo JSON
    private void saveUsers(List<User> users) throws IOException {
        File file = new File(USERS_FILE);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, users);
    }
    
    // Carregar contador de usuários
    private long loadUserCounter() {
        try {
            File file = new File(COUNTER_FILE);
            if (!file.exists()) {
                return 0L;
            }
            
            return objectMapper.readValue(file, Long.class);
        } catch (IOException e) {
            return 0L;
        }
    }
    
    // Salvar contador de usuários
    private void saveUserCounter(long counter) throws IOException {
        File file = new File(COUNTER_FILE);
        objectMapper.writeValue(file, counter);
    }
    
    // Inicializar dados de teste
    public void initializeTestData() {
        try {
            List<User> users = loadUsers();
            if (users.isEmpty()) {
                // Criar alguns usuários de teste
                User testUser1 = new User("admin@pulsetap.com", "Admin", "PulseTap", "123456");
                testUser1.setId(1L);
                
                User testUser2 = new User("user@pulsetap.com", "Usuário", "Teste", "123456");
                testUser2.setId(2L);
                
                users.add(testUser1);
                users.add(testUser2);
                
                saveUsers(users);
                saveUserCounter(2L);
                
                System.out.println("Dados de teste inicializados com sucesso!");
            }
        } catch (IOException e) {
            System.err.println("Erro ao inicializar dados de teste: " + e.getMessage());
        }
    }
}
