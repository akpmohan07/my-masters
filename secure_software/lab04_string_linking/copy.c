#include <stdio.h>
#include <string.h>
#include <stdlib.h>

int
main(int argc, char *argv[])
{
  char dest[32];

  if (argc != 2) {
    fprintf(stderr, "Usage: %s string\n", argv[0]);
    exit(EXIT_FAILURE);
  }

  if (strlen(argv[1]) <= sizeof (dest)) {
    strcpy(dest, argv[1]);
    printf("%s\n", dest);
  }

  return (0);
}
