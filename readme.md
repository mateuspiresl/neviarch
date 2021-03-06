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
- One instruction per line.
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

    SET AX 0x1 # Set the value of AX to 1
    SET BX (AX) # Set the value of BX to the value of AX, in this case, 1
    JUMP (AX) # Jump to the memory address specified in the register AX, in this case, to address 0x1

#### Labels

The jump instructions, JUMP, JUMPEQ, JUMPGR and JUMPGREQ, accepts labels instead of instruction address.
To define a label white the name, only letters and underscore, followed by ":".
To use a label, just write the name in the address place.
The label definition must be alone in the line.

    JUMPEQ ax bx End
    End:
    ...

#### Comments

The comments must start with "#" and can be alone of after instructions or labels.

    # This is a comment
    #This is acceptable
    JUMPEQ ax bx End # This is also acceptable
    End: # And this too

### Build

One of the above.

    javac -d bin/ @sources.txt

    python build.py

    python build.py

### Run

To write, compile and execute, use the line below.
This will output the execution and write two files, runtime.nevl with the code
and runtime.nev with the compiled bytes.

    java -cp bin Program

#### Compile

To compile a file, for example, named _main.nevl_, type:

    java -cp bin Program compile main.nevl

This will generate a _main.nev_ file with the compiled program.
To specify the name of the output file, just write it's name in the end,
like below, if you want it to be _program.nev_.

    java -cp bin Program compile main.nevl program.nev

#### Execute

To execute the program, for example, named _program.nev_, use:

    java -cp bin Program execute program.nev

#### Options

##### --log

To see the log of the control unit, write _--log_, at the end.

    java -cp bin Program execute program.nev --log
    
##### --mem x

To change the size of the memory that will be output, write _--mem_, at the end,
followed by the size.

    java -cp bin Program execute program.nev --mem 20

Can be used together with the _--log_ options in any order.

### Code examples

#### Factorial

The program can be found [here](factorial.nevl).

It needs an integer as input. The ouput is the factorial of this value.

#### Selection Sort of positive numbers

The program can be found [here](selectionsort.nevl).

It needs a list of positive integers followed by a negative integer as input.
The program will sort the positive numbers.
