#include <stdio.h>
#include <string.h>
#include <stdlib.h>

int
main(int argc, const char *argv[])
{
  char fullname[32];

  if (argc != 3) {
    fprintf(stderr, "Usage: %s firstname surname\n", argv[0]);
    exit(EXIT_FAILURE);
  }

  strcpy(fullname, argv[1]);
  strcat(fullname, " ");
  strcat(fullname, argv[2]);
  printf("%s\n", fullname);

  return (0);
}
