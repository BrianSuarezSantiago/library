#!/bin/bash
#
# Author: NTT DATA
# Version: 1.0.0
#

# Configuración del script

# Nombre del archivo que contiene los nombres de los repositorios
REPO_FILE="./repos.txt"  # Variable configurable para el archivo de entrada

# URL base del servidor Git y la API
#GIT_SERVER_URL="https://git.tirea.es:8072"  # URL configurable del servidor GIT
GIT_SERVER_URL="http://localhost:3000"

# Ruta específica de la API para crear repositorios en la organización
API_PATH="/api/v1/orgs/Deployment-Platform/repos"  # Ruta de la API
#API_PATH="/api/v1/user/repos"

# Cabeceras HTTP
ACCEPT_HEADER="application/json"
#AUTHORIZATION_HEADER="Basic ZGRpZXR0YTpOb3N0cm9tbzgxLiM="  # Mecanismo Basic para la autorización, utilizamos el token proporcionado por David Dietta
AUTHORIZATION_HEADER="7e8a6542cbb0abbc2bba4a27da10a9df30dbf0dd"
CONTENT_TYPE_HEADER="application/json"

#! http://localhost:3000/api/v1/user/repos  # URL COMPLETA

# Configuración del repositorio
DEFAULT_BRANCH="master"  # Rama predeterminada
AUTO_INIT=false          # Si se inicializa automáticamente el repositorio
PRIVATE=true             # Repositorio privado
TRUST_MODEL="default"    # Modelo de confianza

# Creación de un timestamp único para esta ejecución
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")

# Archivo de log con timestamp único por cada ejecución
LOG_FILE="repo_creation_$TIMESTAMP.log"  # El archivo de log incluye el timestamp en su nombre

# Función para registrar mensajes en el log con timestamp
log_message() {
    local message=$1
    local timestamp=$(date +"%Y-%m-%d %H:%M:%S")
    echo "[$timestamp] $message" >> "$LOG_FILE"
}

# Función para realizar el POST a la API y crear el repositorio
create_repo() {
    local repo_name=$1

    # Datos del cuerpo de la petición JSON
    repo_data=$(cat <<EOF
{
  "auto_init": $AUTO_INIT,
  "default_branch": "$DEFAULT_BRANCH",
  "description": "",
  "gitignores": "",
  "issue_labels": "",
  "license": "",
  "name": "$repo_name",
  "object_format_name": "sha1",
  "private": $PRIVATE,
  "readme": "",
  "template": false,
  "trust_model": "$TRUST_MODEL"
}
EOF
)

    # Ejecución de la solicitud CURL para la creación del repositorio
    response=$(curl -s -o /dev/null -w "%{http_code}" -X POST \
        "$GIT_SERVER_URL$API_PATH" \
        -H "Accept: $ACCEPT_HEADER" \
        -H "Authorization: token $AUTHORIZATION_HEADER" \
        -H "Content-Type: $CONTENT_TYPE_HEADER" \
        -d "$repo_data")

    # Comprobación del código de respuesta HTTP y registro en el log
    if [ "$response" -eq 201 ]; then
        log_message "Repositorio '$repo_name' creado con éxito. Código HTTP: $response"
        echo "Repositorio '$repo_name' creado con éxito."
    else
        log_message "Error al crear el repositorio '$repo_name'. Código HTTP: $response"
        echo "Error al crear el repositorio '$repo_name'."
    fi
}

# Comprobación de si el archivo del repositorio existe
if [ ! -f "$REPO_FILE" ]; then
    log_message "Error: El archivo '$REPO_FILE' no existe."
    echo "Error: El archivo '$REPO_FILE' no existe."
    exit 1
fi

# Lectura de cada línea del archivo, donde cada línea representa un nombre de repositorio
while IFS= read -r repo_name || [[ -n "$repo_name" ]]; do
    # Llamada a la función para crear el repositorio
    create_repo "$repo_name"
done < "$REPO_FILE"

# Mensaje final al log tras concluir el proceso
log_message "Proceso de creación de repositorios completado."
