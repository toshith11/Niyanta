bits 16

org 0x7c00

start:
    mov ah, 0x0e

    mov al, 'N'
    int 0x10

    mov al, 'I'
    int 0x10

    mov al, 'Y'
    int 0x10

    mov al, 'A'
    int 0x10

    mov al, 'N'
    int 0x10

    mov al, 'T'
    int 0x10

    mov al, 'A'
    int 0x10

    cli
    hlt

times 510-($-$$) db 0
dw 0xaa55
