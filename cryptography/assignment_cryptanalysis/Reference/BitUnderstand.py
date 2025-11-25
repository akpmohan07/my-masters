def b32(x):
    """Return 32-bit binary string."""
    return format(x, '032b')

def generate12(k):
    """Original FEAL function."""
    return (((k >> 6) & 0x3F) << 16) + ((k & 0x3F) << 8)

def debug_key(k):
    print("\n==============================")
    print(" INPUT NUMBER")
    print("==============================")
    print("Decimal :", k)
    print("Binary  :", b32(k))

    # STEP 1 — Upper 6 bits extraction
    step1 = k >> 6
    print("\nSTEP 1: k >> 6")
    print("Decimal :", step1)
    print("Binary  :", b32(step1))

    step1_masked = step1 & 0x3F
    print("\nSTEP 2: (k >> 6) & 0x3F   (extract upper 6 bits)")
    print("Decimal :", step1_masked)
    print("Binary  :", b32(step1_masked))

    step1_shifted = step1_masked << 16
    print("\nSTEP 3: ((k >> 6) & 0x3F) << 16  (place into bits 16–21)")
    print("Decimal :", step1_shifted)
    print("Binary  :", b32(step1_shifted))

    # STEP 2 — Lower 6 bits extraction
    step2 = k & 0x3F
    print("\nSTEP 4: k & 0x3F   (extract lower 6 bits)")
    print("Decimal :", step2)
    print("Binary  :", b32(step2))

    step2_shifted = step2 << 8
    print("\nSTEP 5: (k & 0x3F) << 8   (place into bits 8–13)")
    print("Decimal :", step2_shifted)
    print("Binary  :", b32(step2_shifted))

    # FINAL RESULT
    final = step1_shifted + step2_shifted
    print("\n==============================")
    print(" FINAL 32-BIT RESULT")
    print("==============================")
    print("Decimal :", final)
    print("Binary  :", b32(final))
    print("==============================\n")

# --------------------------------------------------------
# RUN TEST
# --------------------------------------------------------
k = int(input("Enter an integer to test: "))
debug_key(k)
