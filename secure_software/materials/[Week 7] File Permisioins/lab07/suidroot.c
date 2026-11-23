/* Try the UID related system calls */
#define _GNU_SOURCE
#include <sys/param.h>
#include <sys/types.h>
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>

/* Display current real, effective and saved uids */
void
display_uids(void)
{
  uid_t	ruid, euid, suid;

  /* Retrieve real, effective and saved uids */
  if (getresuid(&ruid, &euid, &suid) < 0) {
    perror("getresuid()");
    exit(EXIT_FAILURE);
  }

  printf("Real uid:\t%d\n", (int)ruid);
  printf("Effective uid:\t%d\n", (int)euid);
  printf("Saved uid:\t%d\n", (int)suid);
}

int
main(void)
{
  uid_t r, e, s;

  /* Display uids */
  display_uids();

  /* Pause */
  printf("Hit return to continue...");
  fgetc(stdin);

  return (0);
}
