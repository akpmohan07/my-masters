/* Our first C program makes a single system call to getpid */
#include <sys/types.h>
#include <unistd.h>
#include <stdio.h>

int
main(void)
{
  /* Declare a variable to hold the value returned by getpid */
  int pid;

  /* Make our system call and store the result */
  pid = getppid();
  pid = getppid();

  /* Display the result */
  printf("pid: %d\n", pid);

  return (0);
}
