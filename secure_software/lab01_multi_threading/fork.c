/* C program that forks a child process */
#include <sys/types.h>
#include <unistd.h>
#include <stdio.h>

int
main()
{
  int pid;

  /*
   * After the call to fork we will have two processes running this
   * program. One is the original parent and the other is a new child
   * process. Different values will be returned by fork in the parent
   * and child so you can use the value in pid below to differentiate
   * between the two.
   */
  pid = fork();

  if (pid == 0) {
    printf("Hello from child process %d\n", getpid());
  } else {
    printf("Hello from parent process %d\n", getpid());
  }

  printf("Hello from pid %d\n", pid);

  return (0);
}
