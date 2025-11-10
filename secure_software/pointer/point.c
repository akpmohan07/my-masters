#include <stdio.h>

int main() {
    // 'name' is an array of characters stored on the stack
    char name[] = "Mohan";   

    // 'p' is a pointer to char, pointing to the first element of 'name'
    char *p = name;          

    // Display the string values
    printf("name      = %s\n", name);
    printf("p         = %s\n", p);

    // Display sizes
    printf("\n--- Sizes ---\n");
    printf("sizeof(name) = %lu bytes  // total bytes in the array (includes '\\0')\n", sizeof(name));
    printf("sizeof(p)    = %lu bytes  // size of pointer (4 bytes on 32-bit, 8 on 64-bit)\n", sizeof(p));

    // Display addresses
    printf("\n--- Addresses ---\n");
    printf("&name        = %p  // address of the array variable\n", (void *)&name);
    printf("name (value) = %p  // address of the first element in array\n", (void *)name);
    printf("&p           = %p  // address of the pointer variable 'p'\n", (void *)&p);
    printf("p (value)    = %p  // address stored inside pointer (same as 'name')\n", (void *)p);

    return 0;
}
