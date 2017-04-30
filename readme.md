## Neviarch

Control Unit simulator.

The documentation can be accessed [here](https://mateuspiresl.github.io/neviarch/).

### Registers

AX, BX, CX, DX, EX, FX, GX and HX.

### Memory

Memory of _0xFFFF_ integers, which equals 262140 bytes.

### Instruction

A instruction has 4 bytes.

The first 4 bits specifies the instruction type.<br>
The Following couple of 6 bytes specifies two registers.<br>
And the last 2 bytes specifies an address.

The supported instruction are:

- LOAD
    + Retrieves a value from the memory to a register.
    + Needs the first register where it will be stored and the
      address where the value is located.
- STORE
    + Stores a value in the memory.
    + Needs the first register, with the value, and the address
      which specifies where the value will be stored.
- SET
    + Set a value to a register.
    + Needs the register where the value will be stored and a
      value that has to be located at the address space of the
      instruction.
- ADD
- SUB
- MULT
- DIV
    + Operations of addition, subtraction, multiplication and division, respectively.
    + Needs two registers to make the operation. The result is stored in the first.
- JUMP
    + Jumps unconditionaly to an address.
    + Needs just the address.
- JUMPEQ
    + Jumps to an address if the values from two registers are equal.
    + Needs two registers to compare and the address to jump.
- JUMPGR
    + Jumps to an address if the value from the first register is greater than the
    + value from the second.
    + Needs two registers to compare and the address to jump.
- JUMPGREQ
    + Jumps to an address if the value from the first register is greater than or equal
    + to the value from the second.
    + Needs two registers to compare and the address to jump.
- IN
    + Prompts an integer from user and 
    + Needs the first register to store the value.
- OUT
    + Ouputs an integer from a register.
    + The register must be the first.
- LOADREG
    + Retrieves a value from the memory to a register.
    + The address needs to be specified in the second register, the value will be stored in the first register.
- STOREREG
    + Stores a value in the memory.
    + The first register must have the value to store and the address needs to be specified in the second register.
- SETREG
    + Set a value to a register.
    + The value of the second register will be moved to the first register.

### Assembly

- In the order: instuction, first register (if used), second register (if used) and the address (if used).
- It's case insensitive.
- The address or value at the address space must start with _0x_.

#### Example

Updates the value at _0x1_ to its sum with _0x5F_.

READ ax 0x1
SET bx 0x5F
ADD ax bx
STORE ax 0x1

#### Dereference

Instructions that needs value or address can use values from registers, instead of an hexadecimal,
enclosing the register in parentheses.

##### Example

    SET AX 0x1 # Set the value of AX to 1
    SET BX (AX) # Set the value of BX to the value of AX, in this case, 1
    JUMP (AX) # Jump to the memory address specified in the register AX, in this case, to address 0x1

### Build

One of the above.

    javac -d bin/ @sources.txt

    python build.py

    python build.py

### Run

    java -cp bin Program

