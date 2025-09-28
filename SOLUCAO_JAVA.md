# 🔧 Solução para Erro de Versão Java

## ❌ Problema Identificado
```
java: error: release version 5 not supported
Module pulsetap-backend SDK 23 does not support source version 1.5
```

## ✅ Soluções Implementadas

### 1. **Configuração do Maven Atualizada**
- Adicionei configurações explícitas no `pom.xml`
- Configurado para usar Java 17 (compatível com Spring Boot 3.2.0)
- Adicionado plugin Maven Compiler com versão específica

### 2. **Arquivos de Configuração Criados**
- `.mvn/jvm.config` - Configurações JVM
- `.mvn/wrapper/maven-wrapper.properties` - Wrapper do Maven
- `mvnw.cmd` - Script wrapper para Windows

### 3. **Script de Execução Atualizado**
- `run.bat` agora usa o Maven wrapper
- Fallback para Maven global se wrapper falhar

## 🚀 Como Resolver

### Opção 1: Usar Maven Wrapper (Recomendado)
```bash
# Navegar para o diretório
cd PulseTap-Java

# Usar wrapper do Maven
mvnw.cmd clean compile
mvnw.cmd spring-boot:run
```

### Opção 2: Instalar Maven Globalmente
1. **Baixar Maven:** https://maven.apache.org/download.cgi
2. **Extrair para:** `C:\apache-maven-3.9.6`
3. **Adicionar ao PATH:** `C:\apache-maven-3.9.6\bin`
4. **Reiniciar terminal**
5. **Executar:** `mvn clean compile`

### Opção 3: Usar IDE
1. **Abrir projeto no IntelliJ IDEA ou Eclipse**
2. **Configurar JDK 17** nas configurações do projeto
3. **Executar** `PulseTapApplication.java`

## 🔍 Verificações

### Verificar Java
```bash
java -version
# Deve mostrar Java 17 ou superior
```

### Verificar JAVA_HOME
```bash
echo %JAVA_HOME%
# Deve apontar para instalação do Java 17+
```

### Verificar Maven
```bash
mvn -version
# Deve mostrar Maven 3.6+ com Java 17
```

## 🛠️ Configurações do Projeto

### pom.xml Atualizado
```xml
<properties>
    <java.version>17</java.version>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
```

### Plugin Maven Compiler
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.11.0</version>
    <configuration>
        <source>17</source>
        <target>17</target>
        <release>17</release>
    </configuration>
</plugin>
```

## 🎯 Teste Rápido

### 1. Compilar
```bash
cd PulseTap-Java
mvnw.cmd clean compile
```

### 2. Executar
```bash
mvnw.cmd spring-boot:run
```

### 3. Testar API
```bash
curl http://localhost:8080/api/auth/test
```

## 📞 Se Ainda Não Funcionar

### Verificar Configurações do IDE
1. **IntelliJ IDEA:**
   - File → Project Structure → Project → Project SDK: Java 17
   - File → Settings → Build → Compiler → Java Compiler: 17

2. **Eclipse:**
   - Project → Properties → Java Build Path → Libraries
   - Adicionar JRE 17

### Limpar Cache
```bash
mvnw.cmd clean
mvnw.cmd compile
```

### Verificar Logs
- Verificar mensagens de erro detalhadas
- Confirmar se Java 17 está sendo usado
- Verificar se Maven está configurado corretamente

## ✅ Resultado Esperado

Após aplicar as correções:
- ✅ Compilação sem erros
- ✅ Servidor iniciando na porta 8080
- ✅ API respondendo em `http://localhost:8080/api/auth/test`
- ✅ Usuários de teste funcionando

## 🚀 Próximos Passos

1. ✅ Resolver problema de versão Java
2. 🔄 Testar compilação e execução
3. 📡 Testar APIs
4. 🔗 Integrar com frontend React
5. 📊 Implementar banco de dados real
