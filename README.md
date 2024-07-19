# jSimpleCPU
Implementación de una CPU en Java con manejo de varios tipos de instrucciones.
# Componentes principales
<b>Registros y Memoria:</b>

<b>accumulator:</b> Acumulador para operaciones aritméticas y lógicas.<br>
<b>programCounter:</b> Contador de programa que apunta a la siguiente instrucción a ejecutar.<br>
<b>memory:</b> Memoria principal que utiliza un HashMap para almacenar valores en direcciones de memoria.<br>
<b>stack:</b> Una pila utilizada para operaciones PUSH, POP y DEL.<br>
<b>carryFlag y overflowFlag:</b> Banderas de estado para acarreo y desbordamiento.<br>
<br>
<b>Instrucciones:</b>

Los opcodes están definidos como constantes de tipo byte, representando diversas instrucciones que la CPU puede ejecutar.

<b>Método run():</b>
Ejecuta un bucle hasta que la CPU se detenga (running = false).
Decodifica la instrucción del memory en base al programCounter, descompone en opcode y operando.
Ejecuta la instrucción correspondiente usando un switch en el opcode.

<b>Instrucciones Aritméticas y Lógicas:</b>
ADD, SUB, ADC, SBC, AND, EOR, ORA, etc., realizan operaciones aritméticas y lógicas.
Las instrucciones de desvío (JMP, JZ, BCC, BCS, etc.) modifican el programCounter basado en el estado de los flags y valores del acumulador.

<b>Manejo de Pila: </b>PUSH, POP, DEL gestionan el contenido de la pila.

<b>Métodos de Configuración y Ejecución:</b>

<b>reset():</b> Inicializa el estado de la CPU.

<b>loadProgram(int[] program, Map<Integer, Integer> initialMemory):</b> Carga un programa en la memoria y configura valores iniciales en la memoria.

<b>setMemory(int address, int value), getMemory(int address), getStackTop():</b> Métodos auxiliares para manipular la memoria y la pila.

<b>Método main():</b>

Ejecuta ejemplos de programas simples para probar la funcionalidad de la CPU.

# Pendientes y Mejoras
<b>Documentación:</b>

Agregar comentarios y documentación más detallada sobre cada instrucción y el flujo general de la CPU.

<b>Testeo:</b>

Implementar una suite de pruebas unitarias para verificar que cada instrucción se comporta como se espera.

<b>Optimización:</b>

Revisar si hay oportunidades para optimizar el manejo de memoria y pila, especialmente en operaciones de alto nivel.

<b>Expansión:</b>

Extender la CPU para soportar más instrucciones o características, considerando un diseño modular y extensible desde el principio y no monolítico como ahora.
