---

```markdown
# 🧠 Secure Programming 32-bit Lab (Lightweight Linux Image)

## 🔍 Problem
Modern macOS (Apple Silicon) systems can’t compile or run 32-bit Linux programs natively.  
This leads to errors like:
```

ld: unknown/unsupported architecture name for: -arch armv4t

````
To solve this, we use a **lightweight 32-bit Debian Linux image** that runs inside Docker — no virtual machine needed.

---

## ⚙️ Setup

### 1. Install Docker
Download and install **Docker Desktop** for macOS or Windows.

---

### 2. Build the Image (one-time)
In the folder containing the provided `Dockerfile`, run:
```bash
docker build --platform linux/386 -t debian-x86-32-lab .
````

This creates a lightweight command-line Linux environment with:
`gcc`, `gdb`, `make`, `file`, `vim`, `nano`, `strace`, `ltrace`, `sysctl`, `htop`, and more.

---

## ▶️ Run the Lab

From your project folder:

```bash
docker run --rm -it --privileged \
  --platform linux/386 \
  -v "$PWD":/work -w /work \
  debian-x86-32-lab bash
```

 --privileged  - Will disable ASLR.
✅ You’ll enter a 32-bit Debian shell ready to compile and debug C programs.

---

## 💻 Inside the Lab

```bash
# compile with predictable addresses
gcc -m32 -O0 -g -fno-pie -no-pie stack.c -o stack

# run program
./stack

# view assembly
objdump -d -Mintel -S stack | less

# debug
gdb ./stack
```

---

## 🧩 Notes

* Files in `/work` are saved on your host system.
* On macOS, you can’t change ASLR with `sysctl` — use `-fno-pie -no-pie` for fixed addresses.
* Exit the lab with `exit` or `Ctrl+D`.

---

**A lightweight command-line Linux image for Secure Programming (CSC1135).**

```

---
```
