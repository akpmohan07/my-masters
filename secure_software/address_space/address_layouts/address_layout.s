	.section	__TEXT,__text,regular,pure_instructions
	.build_version macos, 15, 0	sdk_version 15, 0
	.globl	_main                           ; -- Begin function main
	.p2align	2
_main:                                  ; @main
	.cfi_startproc
; %bb.0:
	sub	sp, sp, #64
	.cfi_def_cfa_offset 64
	stp	x29, x30, [sp, #48]             ; 16-byte Folded Spill
	add	x29, sp, #48
	.cfi_def_cfa w29, 16
	.cfi_offset w30, -8
	.cfi_offset w29, -16
	mov	w8, #0
	stur	w8, [x29, #-20]                 ; 4-byte Folded Spill
	stur	wzr, [x29, #-4]
	sub	x8, x29, #8
	str	x8, [sp, #16]                   ; 8-byte Folded Spill
	mov	w8, #5
	stur	w8, [x29, #-8]
	mov	x0, #4
	bl	_malloc
	stur	x0, [x29, #-16]
	ldur	x9, [x29, #-16]
	mov	w8, #20
	str	w8, [x9]
	mov	x9, sp
	adrp	x8, _main@PAGE
	add	x8, x8, _main@PAGEOFF
	str	x8, [x9]
	adrp	x0, l_.str@PAGE
	add	x0, x0, l_.str@PAGEOFF
	bl	_printf
	mov	x9, sp
	adrp	x8, _global_init@PAGE
	add	x8, x8, _global_init@PAGEOFF
	str	x8, [x9]
	adrp	x0, l_.str.1@PAGE
	add	x0, x0, l_.str.1@PAGEOFF
	bl	_printf
	mov	x9, sp
	adrp	x8, _global_uninit@GOTPAGE
	ldr	x8, [x8, _global_uninit@GOTPAGEOFF]
	str	x8, [x9]
	adrp	x0, l_.str.2@PAGE
	add	x0, x0, l_.str.2@PAGEOFF
	bl	_printf
	ldur	x8, [x29, #-16]
	mov	x9, sp
	str	x8, [x9]
	adrp	x0, l_.str.3@PAGE
	add	x0, x0, l_.str.3@PAGEOFF
	bl	_printf
	ldr	x8, [sp, #16]                   ; 8-byte Folded Reload
	mov	x9, sp
	str	x8, [x9]
	adrp	x0, l_.str.4@PAGE
	add	x0, x0, l_.str.4@PAGEOFF
	bl	_printf
	ldur	x0, [x29, #-16]
	bl	_free
	ldur	w0, [x29, #-20]                 ; 4-byte Folded Reload
	ldp	x29, x30, [sp, #48]             ; 16-byte Folded Reload
	add	sp, sp, #64
	ret
	.cfi_endproc
                                        ; -- End function
	.section	__DATA,__data
	.globl	_global_init                    ; @global_init
	.p2align	2, 0x0
_global_init:
	.long	10                              ; 0xa

	.section	__TEXT,__cstring,cstring_literals
l_.str:                                 ; @.str
	.asciz	"Code (main):    %p\n"

l_.str.1:                               ; @.str.1
	.asciz	"Data:           %p\n"

l_.str.2:                               ; @.str.2
	.asciz	"BSS:            %p\n"

	.comm	_global_uninit,4,2              ; @global_uninit
l_.str.3:                               ; @.str.3
	.asciz	"Heap:           %p\n"

l_.str.4:                               ; @.str.4
	.asciz	"Stack:          %p\n"

.subsections_via_symbols
