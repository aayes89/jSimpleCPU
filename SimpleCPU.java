
package jsimplecpu;

/**
 *
 * @author Slam
 */
import java.util.Stack;

public class SimpleCPU {

    private Stack<Integer> stack; // Pila de sistema
    private boolean zeroFlag; // Bandera de cero
    private boolean carryFlag; // Bandera de acarreo
    private boolean haltFlag; // Bandera de paro (halt)
    private int[] memory; // Memoria
    private int pc; // Contador de programa

    public SimpleCPU(int memorySize) {
        stack = new Stack<>();
        memory = new int[memorySize];
        pc = 0;
        zeroFlag = false;
        carryFlag = false;
        haltFlag = false;
    }

    public void loadMemory(int address, int value) {
        if (address >= 0 && address < memory.length) {
            memory[address] = value;
        }
    }

    public void run() {
        while (!haltFlag && pc >= 0 && pc < memory.length) {
            int instruction = memory[pc];
            execute(instruction);
            pc++;
        }
    }

    private void execute(int instruction) {
        switch (instruction) {
            case 0x01: // mov
                int dest = memory[pc + 1];
                int src = memory[pc + 2];
                memory[dest] = memory[src];
                pc += 2;
                break;
            case 0x02: // push
                stack.push(memory[pc + 1]);
                pc++;
                break;
            case 0x03: // pop
                memory[pc + 1] = stack.pop();
                pc++;
                break;
            case 0x04: // popa
                while (!stack.isEmpty()) {
                    memory[pc++] = stack.pop();
                }
                pc--;
                break;
            case 0x05: // del
                stack.clear();
                break;
            case 0x06: // add
                int a = stack.pop();
                int b = stack.pop();
                int sum = a + b;
                stack.push(sum);
                checkZeroFlag(sum);
                break;
            case 0x07: // sub
                int minuend = stack.pop();
                int subtrahend = stack.pop();
                int difference = minuend - subtrahend;
                stack.push(difference);
                checkZeroFlag(difference);
                break;
            case 0x08: // dec
                int value = stack.pop();
                value--;
                stack.push(value);
                checkZeroFlag(value);
                break;
            case 0x09: // inc
                int valueInc = stack.pop();
                valueInc++;
                stack.push(valueInc);
                checkZeroFlag(valueInc);
                break;
            case 0x0A: // mult
                int multiplicand = stack.pop();
                int multiplier = stack.pop();
                int product = multiplicand * multiplier;
                stack.push(product);
                checkZeroFlag(product);
                break;
            case 0x0B: // div
                int dividend = stack.pop();
                int divisor = stack.pop();
                if (divisor != 0) {
                    int quotient = dividend / divisor;
                    stack.push(quotient);
                    checkZeroFlag(quotient);
                } else {
                    System.out.println("Error: División por cero");
                }
                break;
            case 0x0C: // brk (halt)
                haltFlag = true;
                break;
            case 0x0D: // loop
                int loopCount = stack.pop();
                if (loopCount > 0) {
                    stack.push(loopCount - 1);
                    pc = memory[pc + 1] - 1;
                }
                break;
            case 0x0E: // go
                pc = memory[pc + 1] - 1;
                break;
            default:
                System.out.println("Instrucción no válida: " + instruction);
                break;
        }
    }

    private void checkZeroFlag(int result) {
        zeroFlag = (result == 0);
    }

    public void printState() {
        System.out.println("Pila: " + stack);
        System.out.println("Flags: [Zero: " + zeroFlag + ", Carry: " + carryFlag + ", Halt: " + haltFlag + "]");
        System.out.println("PC: " + pc);
    }
    
    public void reset(){
        
    }

    public static void main(String[] args) {
        SimpleCPU cpu = new SimpleCPU(256);

        // Ejemplo de carga de memoria para operaciones aritméticas
        // Suma: 5 + 10
        cpu.loadMemory(0, 0x02); // push 5
        cpu.loadMemory(1, 5);
        cpu.loadMemory(2, 0x02); // push 10
        cpu.loadMemory(3, 10);
        cpu.loadMemory(4, 0x06); // add
        cpu.loadMemory(5, 0x0C); // brk (halt)

        cpu.run();
        System.out.println("Resultado de la suma: " + cpu.stack.peek());
        cpu.printState();

        // Resetear CPU
        cpu = new SimpleCPU(256);

        // Resta: 15 - 5
        cpu.loadMemory(0, 0x02); // push 5
        cpu.loadMemory(1, 5);
        cpu.loadMemory(2, 0x02); // push 15
        cpu.loadMemory(3, 15);
        cpu.loadMemory(4, 0x07); // sub
        cpu.loadMemory(5, 0x0C); // brk (halt)

        cpu.run();
        System.out.println("Resultado de la resta: " + cpu.stack.peek());
        cpu.printState();

        // Resetear CPU
        cpu = new SimpleCPU(256);

        // Multiplicación: 3 * 4
        cpu.loadMemory(0, 0x02); // push 3
        cpu.loadMemory(1, 3);
        cpu.loadMemory(2, 0x02); // push 4
        cpu.loadMemory(3, 4);
        cpu.loadMemory(4, 0x0A); // mult
        cpu.loadMemory(5, 0x0C); // brk (halt)

        cpu.run();
        System.out.println("Resultado de la multiplicación: " + cpu.stack.peek());
        cpu.printState();

        // Resetear CPU
        cpu = new SimpleCPU(256);

        // División: 20 / 4
        cpu.loadMemory(0, 0x02); // push 20
        cpu.loadMemory(1, 20);
        cpu.loadMemory(2, 0x02); // push 4
        cpu.loadMemory(3, 4);
        cpu.loadMemory(4, 0x0B); // div
        cpu.loadMemory(5, 0x0C); // brk (halt)

        cpu.run();
        System.out.println("Resultado de la división: " + cpu.stack.peek());
        cpu.printState();
    }
}
