# 🧠 Lightweight Debian 32-bit Linux Lab (Docker Setup)

Note: This is setup is tested for MacOs.

## 🔍 Problem
New macOS(M Series , Silicon Chipset, arm64 ) systems does not support running 32-bit Linux VMs directly.
This causes issues when compiling or debugging 32-bit C programs.

To solve this, we use **Docker** to create a portable 32-bit Linux lab with all tools preinstalled.

---

## ⚙️ Setup Steps

### 1. Install Docker
Download and install **Docker Desktop** (for macOS/Windows) . https://www.docker.com/get-started/



### 2. Build the 32-bit Lab Image (one-time)
Download the file `Dockerfile` into your local, run:
```bash
docker build --platform linux/386 -t debian-x86-32-lab .
```

This  creates  a  lightweight  command-line  Linux  environment  with:

`gcc`,  `gdb`,  `make`,  `file`,  `vim`,  `nano`,  `strace`,  `ltrace`,  `sysctl`,  `htop`,  and  more.
  

---

  

## ▶️ Run the Lab

  

From  your  project  folder:

  

```bash
docker run --rm  -it  --privileged  \
--platform linux/386 \
-v "$PWD":/work -w /work \
debian-x86-32-lab bash
```

`
Your current directory will be mounted to the Docker, You can edit code in your OS code editor, and compile and debug inside the Docker
`

`--privileged - Will disable ASLR.`


✅ You’ll enter a 32-bit Debian shell ready to compile and debug C programs.

---
## 💻 Inside the Lab

  

```bash

# compile with predictable addresses

`gcc  -m32  -O0  -g  -fno-pie  -no-pie  stack.c  -o  stack`

# run program

./stack

# view assembly

objdump -d stack | less

# debug
gdb  ./stack
```
---
## 🧩 Notes

* Files in `/work` are saved on your host system.
* All file changes inside the mounted `work` folder will be saved to the host OS.
---

--Created with the help of AI--
