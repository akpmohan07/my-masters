#define _GNU_SOURCE
#include <sys/types.h>
#include <unistd.h>
#include <errno.h>
#include <stdio.h>
#include <string.h>
#include <dlfcn.h>

char
*strcpy(char *dest, const char *src)
{
  static char * (*func)(char *, const char *) = NULL;

  if (!func) {
    func = dlsym(RTLD_NEXT, "strcpy");
  }

  printf("Copying: %s\n", src);

  return (func(dest, src));
}
