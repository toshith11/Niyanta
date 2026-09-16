# OS Learning Log

## Day 1

### Topics studied

- What happens when a computer starts?
- What is firmware?
- What is a bootloader?
- What is a kernel?
- What is a process?
- What is a system call?

### What I understood

TODO

### Questions

TODO

### Experiments

TODO

### Mistakes / failures

TODO

### Important discoveries

TODO   

## Lesson 1 — Boot Process

### What I learned

A computer does not directly start the operating system
when powered on.

The simplified startup sequence is:

Power
→ CPU reset
→ Firmware
→ Bootloader
→ Kernel
→ Userspace
→ Applications

### Important concepts

- Firmware initializes the platform and starts the boot process.
- A bootloader loads/starts the kernel.
- The kernel manages privileged access to hardware.
- Applications normally operate in user mode.
- System calls provide a controlled interface between
  applications and the kernel.
- Commander is initially designed as a userspace
  intelligence layer.

### Important question

Why does the CPU need firmware/boot code before it
can execute our kernel?

### Next experiment

Create and boot the first Niyanta kernel in QEMU.

## Step 3 Experiment — Rule-Based Understanding

### Goal
Test whether simple keyword rules can understand natural language commands.

### Successful cases

- "Can you tell me the time?" → QUESTION
- "Please open Chrome for me" → APPLICATION
- "Could you show me what's inside my folders?" → FILE

### Failed cases

- "How much free memory do I have?" → UNKNOWN
- "I want to investigate neuromorphic computing" → UNKNOWN

### Observation

Keyword-based classification works for expected phrases
but fails when the user expresses the same intent using
different words.

### Conclusion

Sūtrādhār needs an understanding layer that identifies
meaning rather than relying only on exact keywords.

### Next improvement

Introduce:
- intent patterns
- synonyms
- entity extraction
- confidence

## Step 3.1 Experiment — Pattern-Based Understanding

### Goal

Improve Sūtrādhār's understanding beyond simple keyword matching.

### Successful cases

- "How much free memory do I have?" → SYSTEM / MEMORY
- "Please open Chrome for me" → APPLICATION / CHROME
- "I want to investigate neuromorphic computing" → RESEARCH / topic
- "I want to study operating systems" → RESEARCH / topic
- "What is my battery level?" → SYSTEM / BATTERY

### Failed case

- "Can you tell me the time?" → UNKNOWN

### Observation

The classifier now recognizes synonyms and extracts
basic targets, but it still depends on predefined
sentence patterns.

### New limitation

Intent and target are not sufficient for executing
complex commands.

Example:

"Remember that my project is Niyanta"

is recognized as MEMORY, but the information to store
is not extracted.

### Conclusion

The understanding model should evolve toward:

Input
→ Intent
→ Action
→ Entity
→ Parameters
→ Confidence

### Next objective

Introduce action identification and entity extraction.