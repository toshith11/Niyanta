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
# Phase 1 — Foundation + Sūtrādhār Skeleton

## Status

Phase 1 completed.

## Objective

Build the first working application-layer Sūtrādhār and establish the
architectural boundary between:

Human → Sūtrādhār → System/Application Interfaces → OS → Kernel → Hardware

---

## 1. OS Concepts Learned

### 1.1 Operating System Structure

Learned the basic execution path:

Firmware
→ Bootloader
→ Kernel
→ Userspace
→ Applications

Key understanding:

- Firmware initializes the machine.
- Bootloader loads the operating system kernel.
- Kernel manages CPU, memory, processes, devices, filesystems and networking.
- Applications run in userspace and request OS services through system interfaces.
- Hardware access should be controlled by the kernel/drivers.

### 1.2 User Mode vs Kernel Mode

Learned that applications normally operate in userspace and do not directly
control hardware.

This supports the Niyanta architecture:

Sūtrādhār remains an application-layer cognitive system rather than becoming
part of the kernel.

### 1.3 CPU, RAM and Registers

Learned:

- CPU executes machine instructions.
- RAM stores active program/data state.
- Registers provide fast CPU-local storage.
- Examples of x86-64 registers: RAX, RSP, RBP, RIP.
- Memory addresses allow programs to locate data in RAM.

### 1.4 Compilation and Execution

Understood the basic path:

C/Java source
→ compiler
→ executable/bytecode
→ runtime
→ CPU execution

Java Commander development also introduced:

- `javac`
- JVM
- Java classes
- packages
- enums
- objects
- threads

### 1.5 Assembly and System Calls

Completed basic NASM experiments.

Example system-call experiment:

`mov rax, 60`
`mov rdi, 42`
`syscall`

The program returned exit code 42.

This demonstrated that a userspace program can request a kernel service
through a system call.

### 1.6 Boot Process Experiment

Built a BIOS-style boot sector in Assembly and executed it using QEMU.

Learned:

- 16-bit real mode
- `0x7C00` boot address
- BIOS interrupt calls
- boot-sector structure
- `0xAA55` boot signature
- QEMU as a safe OS-development test environment

Successfully displayed:

`NIYANTA`

from a boot-sector program.

---

## 2. Sūtrādhār Concepts Learned

### 2.1 Commander as Application Layer

Established the principle:

Sūtrādhār is not the kernel.

Sūtrādhār decides what should be accomplished, while the OS/kernel determines
how system resources are actually accessed.

### 2.2 Cognitive Loop

Established the conceptual loop:

OBSERVE
→ UNDERSTAND
→ CONTEXT
→ MEMORY
→ REASON
→ GOAL
→ PLAN
→ AUTHORIZE
→ ACT
→ OBSERVE AGAIN

Phase 1 implemented the early stages of this loop.

### 2.3 Intent Understanding

Built a rule-based understanding layer capable of identifying intents such as:

- GREETING
- QUESTION
- SYSTEM
- FILE
- APPLICATION
- MEMORY
- RESEARCH
- EXIT
- UNKNOWN

Observed the limitations of keyword-based understanding and documented
cases where natural-language variations were not recognized.

### 2.4 Structured Understanding

Extended understanding from simple intent classification to:

Intent
→ Action
→ Entity
→ Parameter
→ Confidence

Example:

`Open Chrome`

becomes:

- Intent = APPLICATION
- Action = OPEN_APPLICATION
- Entity = CHROME

### 2.5 Knowledge Representation

Introduced structured knowledge:

Subject
→ Relation
→ Value

Example:

PROJECT
→ NAME
→ NIYANTA

This establishes the foundation for future relationship-based memory.

---

## 3. Memory System Learned and Implemented

Built a persistent MemoryManager.

Implemented:

- memory storage
- memory retrieval
- persistent storage across process restarts
- duplicate detection/update
- basic memory types
- structured memory records

Example stored record:

`LONG_TERM | PROJECT | NAME | niyanta`

Verified:

STORE
→ shutdown
→ restart
→ RECALL

The memory survived process termination.

---

## 4. System Awareness Learned and Implemented

Created a System Interface separating Commander logic from OS-specific
information gathering.

Implemented awareness of:

- RAM
- CPU
- storage
- running processes
- current time

Linux system information was obtained through OS/JDK interfaces such as:

`/proc/meminfo`

A structured `SystemSnapshot` was also created containing multiple system
measurements.

Example:

RAM + CPU + Storage + Processes
→ SystemSnapshot

This is the first implementation of Sūtrādhār's system perception layer.

---

## 5. Action System Learned and Implemented

Introduced:

- Action Engine
- Action Result
- controlled Action enum

Current action vocabulary includes:

- RESPOND
- SEARCH
- STORE_MEMORY
- RECALL_MEMORY
- LIST_FILES
- OPEN_APPLICATION
- GET_TIME
- GET_MEMORY_STATUS
- GET_CPU_STATUS
- GET_STORAGE_STATUS
- GET_PROCESS_STATUS
- GET_SYSTEM_SNAPSHOT
- EXIT
- NONE

Implemented real filesystem listing.

Example:

`Show me my files`

→ Understanding
→ LIST_FILES
→ ActionEngine
→ filesystem
→ result

---

## 6. Authorization Concept

Introduced an authorization boundary between intelligence and execution.

Basic risk levels:

- READ_ONLY
- CONFIRMATION_REQUIRED
- NOT_ALLOWED

Prototype rule:

Sūtrādhār can reason about an action, but authority to perform consequential
actions is separately controlled.

Example:

`Remember that my project is Niyanta`

→ STORE_MEMORY
→ confirmation required
→ user approves
→ memory is stored

Core principle established:

"Sūtrādhār is autonomous in reasoning but subordinate in authority."

---

## 7. Background Execution

Created the first background service skeleton.

Implemented:

- separate background worker thread
- background service start/stop lifecycle
- daemon execution model
- clean shutdown

Current worker is intentionally minimal.

Future phases will turn this into actual background cognition, task execution,
observation and scheduled work.

---

## 8. Engineering Lessons

### Experiment → Observe → Debug → Improve

Important failures were deliberately tested and documented.

Examples:

- keyword-based intent failures
- incomplete entity extraction
- duplicate memory records
- Java package mismatch
- stale compiled `.class` files
- string actions being replaced by enums
- system snapshot fall-through bug

These failures helped refine the architecture rather than being hidden.

### Source vs Build Artifacts

Established:

Source code → Git/GitHub

Build output (`build/`, `.class`) → local only

### Development Workflow

Established:

VS Code
→ edit source
→ WSL
→ compile/test
→ Git
→ GitHub

---

## 9. Phase 1 Working Architecture

Human
↓
Sūtrādhār / Commander
↓
Understanding
↓
Structured Command
↓
Authorization
↓
Action Engine
↓
System Interface / Memory
↓
Operating System

Parallel:

Sūtrādhār
↓
Background Service

---

## 10. Phase 1 Achievement

The first working Sūtrādhār prototype can now:

- accept human text input
- classify basic intent
- extract actions/entities/parameters
- store and recall persistent knowledge
- inspect system resources
- create a system snapshot
- list files
- execute controlled actions
- require confirmation for selected actions
- run a background worker
- operate as a userspace/application-layer system

Phase 1 established the foundation for the larger Niyanta vision.

---

## 11. Remaining Limitations

Phase 1 does NOT yet provide:

- deep natural-language understanding
- general reasoning
- contextual memory
- task planning
- autonomous background cognition
- real application automation
- voice interaction
- biometric authentication
- advanced AI/LLM integration
- custom Niyanta kernel integration

These belong to later phases.

---

## Phase 1 Conclusion

The project has moved from an OS-learning repository to a working
application-layer prototype of Sūtrādhār.

The next phase will focus on:

UNDERSTANDING
→ CONTEXT
→ MEMORY
→ BACKGROUND COGNITION
→ REASONING
→ PLANNING