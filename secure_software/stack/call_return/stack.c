#include<stdio.h>

int sum(int a, int b) {
    int m,n,s;
    m = a;
    n = b;
    s = a + b;
    return s;
}

int main(){
    int x, y,z;
    x = 1;
    y = 2;
    z = sum(x,y);
    printf("%d\n", z);
    return 0;
}
