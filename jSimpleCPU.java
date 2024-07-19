package jsimplecpu;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/*
* Create by Slam
*/

public class JSimpleCPU {

    // Definición de registros y memoria
    private int accumulator = 0; // Registro acumulador
    private int programCounter = 0; // Contador de programa
    private Map<Integer, Integer> memory = new HashMap<>(); // Memoria simple
    private Stack<Integer> stack = new Stack<>(); // Pila para manejo de PUSH, POP, DEL
    private boolean carryFlag = false; // Bandera de acarreo
    private boolean overflowFlag = false; // Bandera de desbordamiento
    private int xRegister = 0; // Registro X
    private int yRegister = 0; // Registro Y

    // Definición de instrucciones
    public static final byte BRK = (byte) 0x00; // Detener ejecución
    public static final byte HALT = (byte) 0x00; // Detener ejecución
    public static final byte LOAD = (byte) 0x01; // Cargar valor en el acumulador
    public static final byte STORE = (byte) 0x02; // Almacenar valor del acumulador en memoria
    public static final byte ADD = (byte) 0x03; // Sumar valor a acumulador
    public static final byte SUB = (byte) 0x04; // Restar valor de acumulador
    public static final byte JZ = (byte) 0x06;  // Saltar si acumulador es cero

    public static final byte PUSH = (byte) 0x05; // Empujar valor en la pila
    public static final byte POP = (byte) 0xBB; // Sacar valor de la pila
    public static final byte DEL = (byte) 0xCC; // Eliminar el elemento superior de la pila

    // Nuevos opcodes
    public static final byte ADC = (byte) 0x69; // ADD with carry
    public static final byte AND = (byte) 0x29; // AND - Logical AND
    public static final byte ASL = (byte) 0x0A; // Arithmetic Shift Left
    public static final byte BCC = (byte) 0x90; // Branch if Carry Clear
    public static final byte BCS = (byte) 0xB0; // Branch if Carry Set
    public static final byte BEQ = (byte) 0xF0; // Branch if Equal
    public static final byte BIT = (byte) 0x24; // Bit Test
    public static final byte BMI = (byte) 0x30; // Branch if Minus
    public static final byte BNE = (byte) 0xD0; // Branch if Not Equal
    public static final byte BPL = (byte) 0x10; // Branch if Positive
    public static final byte BVC = (byte) 0x50; // Branch if Overflow Clear
    public static final byte BVS = (byte) 0x70; // Branch if Overflow Set
    public static final byte CLC = (byte) 0x18; // Clear Carry Flag
    public static final byte CLD = (byte) 0xD8; // Clear Decimal Mode
    public static final byte CLI = (byte) 0x58; // Clear Interrupt Disable
    public static final byte CLV = (byte) 0xB8; // Clear Overflow Flag
    public static final byte CMP = (byte) 0xC9; // Compare Accumulator
    public static final byte CPX = (byte) 0xE0; // Compare X Register
    public static final byte CPY = (byte) 0xC0; // Compare Y Register
    public static final byte DEC = (byte) 0xC6; // Decrement Memory
    public static final byte DEX = (byte) 0xCA; // Decrement X Register
    public static final byte DEY = (byte) 0x88; // Decrement Y Register
    public static final byte EOR = (byte) 0x49; // Exclusive OR
    public static final byte INC = (byte) 0xE6; // Increment Memory
    public static final byte INX = (byte) 0xE8; // Increment X Register
    public static final byte INY = (byte) 0xC8; // Increment Y Register
    public static final byte JMP = (byte) 0x4C; // Jump
    public static final byte JSR = (byte) 0x20; // Jump to Subroutine
    public static final byte NOP = (byte) 0xEA; // No Operation
    public static final byte ORA = (byte) 0x09; // Logical Inclusive OR
    public static final byte PHA = (byte) 0x48; // Push Accumulator
    public static final byte PHP = (byte) 0x08; // Push Processor Status
    public static final byte PLA = (byte) 0x68; // Pull Accumulator
    public static final byte PLP = (byte) 0x28; // Pull Processor Status
    public static final byte ROL = (byte) 0x2A; // Rotate Left
    public static final byte ROR = (byte) 0x6A; // Rotate Right
    public static final byte RTI = (byte) 0x40; // Return from Interrupt
    public static final byte RTS = (byte) 0x60; // Return from Subroutine
    public static final byte SBC = (byte) 0xE9; // Subtract with Carry
    public static final byte SEC = (byte) 0x38; // Set Carry Flag
    public static final byte SED = (byte) 0xF8; // Set Decimal Flag
    public static final byte SEI = (byte) 0x78; // Set Interrupt Disable
    public static final byte STA = (byte) 0x85; // Store Accumulator
    public static final byte STX = (byte) 0x86; // Store X Register
    public static final byte STY = (byte) 0x84; // Store Y Register
    public static final byte TAX = (byte) 0xAA; // Transfer Accumulator to X
    public static final byte TAY = (byte) 0xA8; // Transfer Accumulator to Y
    public static final byte TSX = (byte) 0xBA; // Transfer Stack Pointer to X
    public static final byte TXA = (byte) 0x8A; // Transfer X to Accumulator
    public static final byte TXS = (byte) 0x9A; // Transfer X to Stack Pointer
    public static final byte TYA = (byte) 0x98; // Transfer Y to Accumulator
    public static final byte LDA = (byte) 0xA9; // Load Accumulator
    public static final byte LDX = (byte) 0xA2; // Load X Register
    public static final byte LDY = (byte) 0xA0; // Load Y Register
    public static final byte LSR = (byte) 0x4A; // Logical Shift Right

    // Ejecución del ciclo de instrucciones
    public void run() {
        boolean running = true;
        while (running) {
            int instruction = memory.getOrDefault(programCounter, 0);
            byte opcode = (byte) (instruction >> 16);
            int operand = instruction & 0xFFFF;

            switch (opcode) {
                case LOAD:
                    accumulator = memory.getOrDefault(operand, 0);
                    break;
                case STORE:
                    memory.put(operand, accumulator);
                    break;
                case ADD:
                    accumulator += memory.getOrDefault(operand, 0);
                    break;
                case SUB:
                    accumulator -= memory.getOrDefault(operand, 0);
                    break;
                case JMP:
                    programCounter = operand - 1; // -1 porque incrementaremos al final
                    break;
                case JZ:
                    if (accumulator == 0) {
                        programCounter = operand - 1;
                    }
                    break;
                case PUSH:
                    stack.push(accumulator);
                    break;
                case POP:
                    if (!stack.isEmpty()) {
                        accumulator = stack.pop();
                    } else {
                        throw new IllegalStateException("Pila vacía, no se puede realizar POP.");
                    }
                    break;
                case DEL:
                    if (!stack.isEmpty()) {
                        stack.pop();
                    } else {
                        throw new IllegalStateException("Pila vacía, no se puede realizar DEL.");
                    }
                    break;
                case ADC:
                    performADC(operand);
                    break;
                case AND:
                    accumulator &= memory.getOrDefault(operand, 0);
                    break;
                case ASL:
                    performASL();
                    break;
                case BCC:
                    if (!carryFlag) {
                        programCounter = operand - 1;
                    }
                    break;
                case BCS:
                    if (carryFlag) {
                        programCounter = operand - 1;
                    }
                    break;
                case BEQ:
                    if (accumulator == 0) {
                        programCounter = operand - 1;
                    }
                    break;
                case BIT:
                    int valueBIT = memory.getOrDefault(operand, 0);
                    boolean zeroFlag = (accumulator & valueBIT) == 0;
                    overflowFlag = (valueBIT & 0x40) != 0;
                    carryFlag = (valueBIT & 0x80) != 0;
                    break;
                case BMI:
                    if (accumulator < 0) {
                        programCounter = operand - 1;
                    }
                    break;
                case BNE:
                    if (accumulator != 0) {
                        programCounter = operand - 1;
                    }
                    break;
                case BPL:
                    if (accumulator >= 0) {
                        programCounter = operand - 1;
                    }
                    break;
                case BVC:
                    if (!overflowFlag) {
                        programCounter = operand - 1;
                    }
                    break;
                case BVS:
                    if (overflowFlag) {
                        programCounter = operand - 1;
                    }
                    break;
                case CLC:
                    carryFlag = false;
                    break;
                case CLD:
                    // No Decimal Mode support
                    break;
                case CLI:
                    // No Interrupt Disable support
                    break;
                case CLV:
                    overflowFlag = false;
                    break;
                case CMP:
                    int valueCMP = memory.getOrDefault(operand, 0);
                    carryFlag = accumulator >= valueCMP;
                    accumulator = (accumulator - valueCMP) & 0xFF;
                    break;
                case CPX:
                    int valueCPX = memory.getOrDefault(operand, 0);
                    carryFlag = (accumulator >= valueCPX);
                    break;
                case CPY:
                    int valueCPY = memory.getOrDefault(operand, 0);
                    carryFlag = (accumulator >= valueCPY);
                    break;
                case DEC:
                    int valueDEC = memory.getOrDefault(operand, 0);
                    valueDEC = (valueDEC - 1) & 0xFF;
                    memory.put(operand, valueDEC);
                    break;
                case DEX:
                    // Decrement X register 
                    int x = memory.getOrDefault(0xFF, 0); // Usar dirección 0xFF para Y
                    x--;
                    memory.put(0xFF, x); // Almacenar el valor decrementado en la dirección 0xFF
                    break;
                case DEY:
                    // Decrement Y register 
                    int y = memory.getOrDefault(0xFF, 0); // Usar dirección 0xFF para Y
                    y--;
                    memory.put(0xFF, y); // Almacenar el valor decrementado en la dirección 0xFF
                    break;
                case EOR:
                    accumulator ^= memory.getOrDefault(operand, 0);
                    break;
                case INC:
                    int valueINC = memory.getOrDefault(operand, 0);
                    valueINC = (valueINC + 1) & 0xFF;
                    memory.put(operand, valueINC);
                    break;
                case INX:
                    // Increment X register 
                    xRegister++;
                    break;
                case INY:
                    // Increment Y register (assumed to be a class variable)
                    yRegister++;
                    break;
                case JSR:
                    stack.push(programCounter + 2);
                    programCounter = operand - 1;
                    break;
                case NOP:
                    // No Operation
                    break;
                case ORA:
                    accumulator |= memory.getOrDefault(operand, 0);
                    break;
                case PHA:
                    stack.push(accumulator);
                    break;
                case PHP:
                    // Push Processor Status (not implemented)
                    stack.push(accumulator);
                    break;
                case PLA:
                    if (!stack.isEmpty()) {
                        accumulator = stack.pop();
                    } else {
                        throw new IllegalStateException("Pila vacía, no se puede realizar PLA.");
                    }
                    break;
                case PLP:
                    // Pull Processor Status (not implemented)
                    if (!stack.isEmpty()) {
                        accumulator = stack.pop();
                    } else {
                        throw new IllegalStateException("Pila vacía, no se puede realizar PLP.");
                    }
                    break;
                case ROL:
                    boolean bit = (accumulator & 0x80) != 0;
                    accumulator = (accumulator << 1) | (carryFlag ? 1 : 0);
                    carryFlag = bit;
                    break;
                case ROR:
                    boolean carry = (accumulator & 0x01) != 0;
                    accumulator = (accumulator >> 1) | (carryFlag ? 0x80 : 0);
                    carryFlag = carry;
                    break;
                case RTI:
                    // Return from Interrupt (not implemented)
                    break;
                case RTS:
                    if (!stack.isEmpty()) {
                        programCounter = stack.pop();
                    } else {
                        throw new IllegalStateException("Pila vacía, no se puede realizar RTS.");
                    }
                    break;
                case SBC:
                    int valueSBC = memory.getOrDefault(operand, 0);
                    int resultSBC = accumulator - valueSBC - (carryFlag ? 0 : 1);
                    carryFlag = resultSBC >= 0;
                    accumulator = resultSBC & 0xFF;
                    break;
                case SEC:
                    carryFlag = true;
                    break;
                case SED:
                    // Set Decimal Flag (not implemented)
                    break;
                case SEI:
                    // Set Interrupt Disable (not implemented)
                    break;
                case STA:
                    memory.put(operand, accumulator);
                    break;
                case STX:
                    // Store X Register (assumed to be a class variable)
                    stack.push(xRegister);
                    break;
                case STY:
                    // Store Y Register 
                    stack.push(yRegister);
                    break;
                case TAX:
                    xRegister = accumulator;
                    break;
                case TAY:
                    yRegister = accumulator;
                    break;
                case TSX:
                    xRegister = stack.size();
                    break;
                case TXA:
                    accumulator = xRegister;
                    break;
                case TXS:
                    stack.setSize(xRegister);
                    break;
                case TYA:
                    accumulator = yRegister;
                    break;
                case LDX:
                    xRegister = memory.getOrDefault(operand, 0);
                    break;
                case LDY:
                    yRegister = memory.getOrDefault(operand, 0);
                    break;
                case LDA:
                    accumulator = memory.getOrDefault(operand, 0);
                    break;

                case LSR:
                    carryFlag = (accumulator & 0x01) != 0;
                    accumulator >>= 1;
                    break;
                case 0x00: // HALT or BRK opcode
                    running = false;
                    break;
                default:
                    System.out.println("Instrucción desconocida: " + opcode);
                //throw new IllegalStateException("Instrucción desconocida: " + opcode);
            }
            programCounter++;
        }
    }

    // Limpiar el estado de la CPU
    public void reset() {
        accumulator = 0;
        programCounter = 0;
        memory.clear();
        stack.clear(); // Limpiar la pila
        carryFlag = false;
        overflowFlag = false;
    }

    public void loadProgram(int[] program, Map<Integer, Integer> initialMemory) {
        // Limpiar el estado de la CPU
        reset();

        // Cargar el nuevo programa
        for (int i = 0; i < program.length; i++) {
            memory.put(i, program[i]);
        }

        // Inicializar la memoria con los valores específicos
        memory.putAll(initialMemory);
        initialMemory.clear();
    }

    public void loadProgram(int[] program) {
        // Limpiar el estado de la CPU
        reset();

        // Cargar el nuevo programa
        for (int i = 0; i < program.length; i++) {
            memory.put(i, program[i]);
        }
    }

    public void setMemory(int address, int value) {
        memory.put(address, value);
    }

    public int getMemory(int address) {
        return memory.getOrDefault(address, 0);
    }

    public int getStackTop() {
        return stack.isEmpty() ? 0 : stack.peek();
    }

    private void performADC(int operand) {
        int valueADC = memory.getOrDefault(operand, 0);
        int resultADC = accumulator + valueADC + (carryFlag ? 1 : 0);
        carryFlag = resultADC > 0xFF;
        accumulator = resultADC & 0xFF;
    }

    private void performASL() {
        carryFlag = (accumulator & 0x80) != 0;
        accumulator <<= 1;
        accumulator &= 0xFF;
    }

    // Método principal para la demostración
    public static void main(String[] args) {
        JSimpleCPU cpu = new JSimpleCPU();
        Map<Integer, Integer> initialMemory = new HashMap<>();
        // Programa de ejemplo: Cargar 10 en el acumulador, sumar 20, almacenar en dirección 0, detenerse
        int[] macroSuma = {
            (LOAD << 16) | 10, // Cargar valor en dirección 10 en el acumulador
            (ADD << 16) | 20, // Sumar valor en dirección 20 al acumulador
            (STORE << 16) | 0, // Almacenar valor del acumulador en dirección 0
            (HALT << 16) // Detener la ejecución
        };

        // Programa de ejemplo: Cargar 10 en el acumulador, restar 20, almacenar en dirección 0, detenerse
        int[] macroResta = {
            (LOAD << 16) | 10, // Cargar valor en la dirección 10 en el acumulador
            (SUB << 16) | 20, // Restar valor en la dirección 20 del acumulador
            (STORE << 16) | 0, // Almacenar el resultado en la dirección 0
            (HALT << 16) // Detener la ejecución
        };

        // Programa de ejemplo: Cargar 10 en el acumulador, empujar a la pila, cargar 20 en el acumulador, sumar el valor superior de la pila, almacenar en dirección 0, detenerse
        int[] macroPushPop = {
            (LOAD << 16) | 10, // Cargar valor en dirección 10 en el acumulador
            (PUSH << 16), // Empujar el valor del acumulador a la pila
            (LOAD << 16) | 20, // Cargar valor 20 en el acumulador
            (POP << 16), // Sacar el valor superior de la pila al acumulador
            (ADD << 16) | 20, // Sumar 20 al acumulador
            (STORE << 16) | 0, // Almacenar el resultado en la dirección 0
            (HALT << 16) // Detener la ejecución
        };

        // Programa de ejemplo: Cargar 10 en el acumulador, empujar a la pila, cargar 20 en el acumulador, sumar el valor superior de la pila, almacenar en dirección 0, detenerse
        int[] macroPushPopNew = {
            (LDA << 16) | 10, // Cargar valor en dirección 10 en el acumulador
            (PHA << 16), // Empujar el valor del acumulador a la pila
            (LDA << 16) | 20, // Cargar valor 20 en el acumulador
            (PLA << 16), // Sacar el valor superior de la pila al acumulador
            (ADC << 16) | 20, // Sumar 20 al acumulador
            (STA << 16) | 0, // Almacenar el resultado en la dirección 0
            (HALT << 16) // Detenerse
        };
        // Programa para multiplicar dos valores
        int[] programMultiply = {
            (LDA << 16) | 10, // Cargar el primer valor (multiplicando) en el acumulador
            (PHA << 16), // Empujar el valor a la pila (este será el primer operando)
            (LDA << 16) | 20, // Cargar el segundo valor (multiplicador) en el acumulador
            (TAY << 16), // Transferir el acumulador a Y (el multiplicador)
            (LDA << 16) | 0, // Inicializar el acumulador a 0 (esto será el resultado)
            (STA << 16) | 30, // Almacenar 0 en la dirección 30 (resultado inicial)

            // Inicio del bucle de multiplicación
            (LDA << 16) | 30, // Cargar el resultado actual en el acumulador
            (ADD << 16) | 10, // Sumar el primer valor (multiplicando)
            (STA << 16) | 30, // Almacenar el nuevo resultado en la dirección 30
            (DEY << 16), // Decrementar Y (contador de multiplicaciones restantes)
            (BNE << 16) | 7, // Si Y no es 0, saltar al inicio del bucle (offset 7 bytes para la instrucción BNE)

            (HALT << 16) // Detener la ejecución
        };
        int[] programMultiply1 = {
            (LDA << 16) | 10, // Cargar el primer valor (multiplicando) en el acumulador
            (PHA << 16), // Empujar el valor a la pila (esto será el primer operando)
            (LDA << 16) | 20, // Cargar el segundo valor (multiplicador) en el acumulador
            (STA << 16) | 0xFF, // Almacenar el multiplicador en la dirección 0xFF (registro Y)
            (LDA << 16) | 10, // Cargar el primer valor (multiplicando) en el acumulador
            (STA << 16) | 0xFE, // Almacenar el primer valor en la dirección 0xFE (registro X)
            (LDA << 16) | 0, // Inicializar el acumulador a 0 (esto será el resultado)
            (STA << 16) | 30, // Almacenar 0 en la dirección 30 (resultado inicial)

            // Inicio del bucle de multiplicación
            (LDA << 16) | 30, // Cargar el resultado actual en el acumulador
            (ADD << 16) | 0xFE, // Sumar el primer valor (multiplicando) desde la dirección 0xFE
            (STA << 16) | 30, // Almacenar el nuevo resultado en la dirección 30
            (LDA << 16) | 0xFF, // Cargar el multiplicador desde la dirección 0xFF
            (DEY << 16), // Decrementar Y (contador de multiplicaciones restantes)
            (BNE << 16) | 7, // Si Y no es 0, saltar al inicio del bucle (offset 7 bytes para la instrucción BNE)

            (HALT << 16) // Detener la ejecución
        };

        // Inicializar memoria con valores específicos
        initialMemory.put(10, 10);  // Valor en dirección 10
        initialMemory.put(20, 20); // Valor en dirección 20
        cpu.loadProgram(macroSuma, initialMemory);
        cpu.run();
        // Verificar el resultado almacenado en la memoria
        System.out.println("Valor en la memoria en dirección 0: " + cpu.memory.get(0));

        // Inicializar memoria con valores específicos
        initialMemory.put(10, 50);  // Valor en dirección 50
        initialMemory.put(20, 15);  // Valor en dirección 15
        cpu.loadProgram(macroResta, initialMemory);
        cpu.run();
        // Verificar el resultado almacenado en la memoria
        System.out.println("Valor en la memoria en dirección 0: " + cpu.memory.get(0));

        // Inicializar memoria con valores específicos
        initialMemory.put(10, 50);  // Valor en dirección 50
        initialMemory.put(20, 15);  // Valor en dirección 15
        cpu.loadProgram(macroPushPop, initialMemory);
        cpu.run();
        // Verificar el resultado almacenado en la memoria
        System.out.println("Valor en la memoria en dirección 0: " + cpu.memory.get(0));

        // Inicializar memoria con valores específicos
        initialMemory.put(10, 54);  // Valor en dirección 54
        initialMemory.put(20, 46);  // Valor en dirección 46
        cpu.loadProgram(macroPushPopNew, initialMemory);
        cpu.run();
        // Verificar el resultado almacenado en la memoria
        System.out.println("Valor en la memoria en dirección 0: " + cpu.memory.get(0));

        // Inicializar memoria con valores específicos
        initialMemory.put(10, 5);  // Valor en dirección 5
        initialMemory.put(20, 15);  // Valor en dirección 15
        cpu.loadProgram(programMultiply1);
        cpu.run();
        // Verificar el resultado almacenado en la memoria
        System.out.println("Valor en la memoria en dirección 0: " + cpu.memory.get(0));
    }

}
