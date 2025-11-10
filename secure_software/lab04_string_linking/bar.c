extern int answer;

int *p = &answer;

void
multiply(int a, int b)
{
  *p = a * b;
}
