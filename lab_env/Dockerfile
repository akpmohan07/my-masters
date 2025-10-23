# =========================================
# Secure Programming Debian x86 Lab (Final)
# Debian 12 (Bookworm) – 32-bit
# OS       : Debian Bookworm (32-bit)
# Purpose  : For CSC1135 Secure Programming,
#            memory & stack experiments
# Author   : Mohankumar M
# =========================================
FROM debian:bookworm
ARG DEBIAN_FRONTEND=noninteractive

RUN apt update && \
    apt install -y \
        # --- Development Tools ---
        build-essential \
        gcc \
        g++ \
        gdb \
        make \
        cmake \
        pkg-config \
        # --- File / System Utilities ---
        file \
        procps \
        psmisc \
        lsof \
        htop \
        tree \
        wget \
        curl \
        unzip \
        tar \
        less \
        nano \
        vim \
        man-db \
        util-linux \
        gawk \
        # --- Debugging / Analysis ---
        strace \
        ltrace \
        binutils \
        coreutils \
        net-tools \
        iputils-ping \
        dnsutils \
        # --- Network & Security Utilities ---
        netcat-traditional \
        nmap \
        tcpdump \
        iproute2 \
        traceroute \
        # --- Text / Data Tools ---
        grep \
        sed \
        diffutils \
        findutils \
        # --- Monitoring Tools ---
        sysstat \
        iotop \
        # --- Misc / Quality of Life ---
        locales \
        bash-completion && \
    apt clean && rm -rf /var/lib/apt/lists/*

WORKDIR /work

RUN echo "source /etc/bash_completion" >> ~/.bashrc && \
    echo '\n=============================================' \
         '\n Welcome to the Secure Programming x86 Lab' \
         '\n Debian 12 (Bookworm) – 32-bit Environment' \
         '\n Tools: gcc, gdb, strace, nmap, htop, file, sysctl, awk, hexdump, etc.' \
         '\n =============================================\n' \
         '\nTo configure ASLR: sysctl -w kernel.randomize_va_space=0|1|2\n' \
         '\nHappy Debugging :)\n' \
         '\n' > /etc/motd && \
    echo "cat /etc/motd" >> ~/.bashrc

# =========================================
# ASLR configuration (auto-disable if possible)
# =========================================

# Try to disable ASLR automatically at container startup.
# - On Linux (privileged container): works fully.
# - On macOS Docker Desktop: prints a warning only.
RUN echo '\n# Disable ASLR at startup if possible' >> /etc/bash.bashrc && \
    echo 'if [ -w /proc/sys/kernel/randomize_va_space ]; then' >> /etc/bash.bashrc && \
    echo '  echo 0 > /proc/sys/kernel/randomize_va_space 2>/dev/null && echo "!!!Warning!!! [ASLR] Disabled successfully. !!!Warning!!!"' >> /etc/bash.bashrc && \
    echo 'else' >> /etc/bash.bashrc && \
    echo '  echo "[ASLR] Could not disable (restricted by Docker Desktop). Using -fno-pie -no-pie binaries instead or run docker with privileged mode"' >> /etc/bash.bashrc && \
    echo 'fi' >> /etc/bash.bashrc

CMD ["bash"]
