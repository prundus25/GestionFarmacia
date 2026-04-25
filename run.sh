#!/bin/bash
# Compila y ejecuta la app de Farmacia Hospitalaria
# Uso: ./run.sh

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"

echo "Compilando..."
mkdir -p bin
javac -encoding UTF-8 -d bin -sourcepath src src/Main.java
if [ $? -ne 0 ]; then
    echo "Error de compilación."
    exit 1
fi
echo "Compilación exitosa. Iniciando..."
echo ""
java -cp bin Main
