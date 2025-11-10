#include <stdio.h>
#include <string.h>
#include <stdlib.h>

int
main(int argc, char *argv[])
{
  char dest[32];
  char *d = NULL;

  if (argc != 2) {
    fprintf(stderr, "Usage: %s string\n", argv[0]);
    exit(EXIT_FAILURE);
  }

  /* This check ignores the required null-terminating byte */
  /* if (strlen(argv[1]) <= sizeof (dest)) { */

  /* Using an if statement to correct the problem */
  if (strlen(argv[1]) + 1 <= sizeof (dest)) {
    strcpy(dest, argv[1]);
    printf("%s\n", dest);
  } else {
    printf("Not enough room\n");
  }

  /* Using strncpy to correct the problem */
  strncpy(dest, argv[1], sizeof (dest));
  dest[sizeof (dest) - 1] = '\0';
  printf("%s\n", dest);

  /* Using malloc to correct the problem */
  d = malloc(strlen(argv[1]) + 1);
  if (!d) {
    perror("malloc()");
    exit(EXIT_FAILURE);
  }
  strcpy(d, argv[1]);
  printf("%s\n", d);
  free(d);
   
  /* Using strdup to correct the problem */
  d = strdup(argv[1]);
  if (!d) {
    perror("strdup()");
    exit(EXIT_FAILURE);
  }
  printf("%s\n", d);
  free(d);

  /* Using strndup to correct the problem */
  d = strndup(argv[1], 32);
  if (!d) {
    perror("strndup()");
    exit(EXIT_FAILURE);
  }
  printf("%s\n", d);
  free(d);  

  return (0);
}
