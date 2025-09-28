package com.pulsetap.config;

import com.pulsetap.service.JsonStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private JsonStorageService jsonStorageService;
    
    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== Inicializando PulseTap Backend ===");
        
        // Inicializar dados de teste
        jsonStorageService.initializeTestData();
        
        System.out.println("=== Backend iniciado com sucesso! ===");
        System.out.println("API disponível em: http://localhost:8080");
        System.out.println("Endpoints disponíveis:");
        System.out.println("  POST /api/auth/register - Registrar usuário");
        System.out.println("  POST /api/auth/login - Fazer login");
        System.out.println("  GET  /api/auth/users - Listar usuários");
        System.out.println("  GET  /api/auth/test - Testar API");
        System.out.println("  GET  /api/user/profile - Perfil do usuário (requer token)");
        System.out.println("  GET  /api/user/verify-token - Verificar token");
        System.out.println("");
        System.out.println("Usuários de teste:");
        System.out.println("  Email: admin@pulsetap.com | Senha: 123456");
        System.out.println("  Email: user@pulsetap.com | Senha: 123456");
    }
}
