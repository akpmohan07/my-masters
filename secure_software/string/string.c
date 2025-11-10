#include <wchar.h>
#include <stdio.h>

int main() {
    wchar_t c = L'あ';      // L prefix → wide character literal
    wprintf(L"Character: %lc\n", c);
    wprintf(L"Code point: %x\n", c);
    return 0;
}
