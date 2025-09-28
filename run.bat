@echo off
echo ========================================
echo    PulseTap Backend - Iniciando...
echo ========================================
echo.

echo Verificando Java...
java -version
if %errorlevel% neq 0 (
    echo ERRO: Java nao encontrado! Instale Java 17 ou superior.
    pause
    exit /b 1
)

echo.
echo Verificando Maven...
mvn -version
if %errorlevel% neq 0 (
    echo ERRO: Maven nao encontrado! Instale Maven 3.6 ou superior.
    pause
    exit /b 1
)

echo.
echo Compilando projeto...
mvnw.cmd clean compile
if %errorlevel% neq 0 (
    echo ERRO: Falha na compilacao!
    echo Tentando com Maven wrapper...
    mvnw clean compile
    if %errorlevel% neq 0 (
        echo ERRO: Falha na compilacao com wrapper!
        pause
        exit /b 1
    )
)

echo.
echo ========================================
echo    Iniciando servidor...
echo ========================================
echo.
echo API estara disponivel em: http://localhost:8080
echo.
echo Pressione Ctrl+C para parar o servidor
echo.

mvnw.cmd spring-boot:run
if %errorlevel% neq 0 (
    echo Tentando com Maven wrapper...
    mvnw spring-boot:run
)

pause
