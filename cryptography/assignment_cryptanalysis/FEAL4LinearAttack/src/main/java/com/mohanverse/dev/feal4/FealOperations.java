package com.mohanverse.dev.feal4;

// Wrapper around FealProgram with helper methods for linear cryptanalysis
// Adds bit manipulation and round function helpers
public class FealOperations {
    
    // Extracts bit at position (0 = MSB, 31 = LSB)
    public int extractBitAtPosition(int value, int position) {
        if (position < 0 || position > 31) {
            throw new IllegalArgumentException("Bit position must be between 0 and 31");
        }
        return (value >>> (31 - position)) & 1;
    }
    
    // XORs multiple bit positions together - used in linear approximations
    // e.g., bit[5] XOR bit[13] XOR bit[21]
    public int computeXorOfBits(int value, int[] positions) {
        int result = 0;
        for (int pos : positions) {
            result ^= extractBitAtPosition(value, pos);
        }
        return result;
    }
    
    // Helper methods for XOR operations
    public int xorThree(int a, int b, int c) {
        return a ^ b ^ c;
    }
    
    public int xorFour(int a, int b, int c, int d) {
        return a ^ b ^ c ^ d;
    }
    
    // Wrappers for FealProgram functions
    public int packBytes(byte[] bytes, int startIndex) {
        return FealProgram.pack(bytes, startIndex);
    }
    
    public void unpackBytes(int word, byte[] bytes, int startIndex) {
        FealProgram.unpack(word, bytes, startIndex);
    }
    
    public int evaluateFFunction(int input) {
        return FealProgram.f(input);
    }
}

