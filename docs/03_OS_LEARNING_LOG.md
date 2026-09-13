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