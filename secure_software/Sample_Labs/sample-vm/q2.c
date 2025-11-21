#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>

enum { SIZE = 40 };

/* The magic string */
char *magic = "One string to bring them all, and in the darkness bind them.";

int
main(int argc, char *argv[]) {
  char buffer[SIZE];

  if (argc != 2) {
    printf("Usage: %s name\n", argv[0]);
    return (-1);
  }

  snprintf(buffer, sizeof (buffer), "%s", "Hello ");

  strcat(buffer, argv[1]);

  printf("%s\n", buffer);

  if (geteuid() == 0) {

    printf("%s\n", magic);

  } else {

    printf("Forget it. You do not have access to the magic string.\n");
    return (-1);

  }

  return (0);
}
