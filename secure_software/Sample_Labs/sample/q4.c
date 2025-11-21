#include <stdio.h>
#include <string.h>
#include <stdlib.h>

int rating = -99;

int
main(int argc, char *argv[]) {
  size_t room;
  const int SUCCESS = 0, FAILURE = 1;
  char buffer[128];
  char middle[] = " has a rating: ";

  if (argc != 2) {
    printf("Usage: %s name\n", argv[0]);
    return (FAILURE);
  }

  snprintf(buffer, sizeof (buffer), "%s", argv[1]);

  room = sizeof (buffer) - strlen(buffer) - 1;

  strncat(buffer, middle, room);

  printf(buffer);

  printf("%d\n", rating);

  return (SUCCESS);
}
