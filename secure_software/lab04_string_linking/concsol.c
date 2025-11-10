#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdio_ext.h>

int
main(int argc, const char *argv[])
{
  char fullname[32];
  char firstname[32];
  char surname[32];
  size_t room;

  if (argc != 3) {
    fprintf(stderr, "Usage: %s firstname surname\n", argv[0]);
    exit(EXIT_FAILURE);
  }

  /* This code is vulnerable to buffer overflow */
  /* strcpy(fullname, argv[1]); */
  /* strcat(fullname, " "); */
  /* strcat(fullname, argv[2]); */
  /* printf("%s\n", fullname); */

  /* Using an if statement (ignoring potential integer issues) */
  if (strlen(argv[1]) + 1 + strlen(argv[2]) + 1 <= sizeof (fullname)) {
    strcpy(fullname, argv[1]);
    strcat(fullname, " ");
    strcat(fullname, argv[2]);
    printf("%s\n", fullname);
  } else {
    printf("Not enough room\n");
  }

  /* Using strncpy and strncat (strncat null-terminates)  */
  strncpy(fullname, argv[1], sizeof (fullname));
  fullname[sizeof (fullname) - 1] = '\0';
  room = sizeof (fullname) - strlen(fullname) - 1;
  strncat(fullname, " ", room);
  room = sizeof (fullname) - strlen(fullname) - 1;  
  strncat(fullname, argv[2], room);  
  printf("%s\n", fullname);

  /* Using an if statement plus sprintf (ignoring potential integer issues) */
  if (strlen(argv[1]) + 1 + strlen(argv[2]) + 1 <= sizeof (fullname)) {
    sprintf(fullname, "%s %s", argv[1], argv[2]);
    printf("%s\n", fullname);
  } else {
    printf("Not enough room\n");
  }

  /* Using snprintf (the best approach) */
  if (snprintf(fullname, sizeof (fullname),
        "%s %s", argv[1], argv[2]) >= sizeof (fullname)) {
    printf("Ouput truncated\n");
  }
  printf("%s\n", fullname);

  /* Prompting for firstname and surname */
  printf("Please enter your firstname:\n");
  fgets(firstname, sizeof(firstname), stdin);
  /* Discard leftovers in input stream */
  __fpurge(stdin);

  printf("Please enter your surname:\n");
  fgets(surname, sizeof(surname), stdin);
  /* Discard leftovers in input stream */
  __fpurge(stdin);

  /* Now use snprintf */
  if (snprintf(fullname, sizeof (fullname),
        "%s %s", firstname, surname) >= sizeof (fullname)) {
    printf("Ouput truncated\n");
  }
  printf("%s\n", fullname);

  return (0);
}
