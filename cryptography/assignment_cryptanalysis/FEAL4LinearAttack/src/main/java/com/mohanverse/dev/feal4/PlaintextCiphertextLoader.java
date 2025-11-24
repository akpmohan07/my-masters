package com.mohanverse.dev.feal4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Loads plaintext/ciphertext pairs from file
// Uses a Pair object to store each relationship (different from old solution)
public class PlaintextCiphertextLoader {
    
    // Represents a single plaintext-ciphertext pair with left/right halves
    public static class PlaintextCiphertextPair {
        public final int plaintextLeft;
        public final int plaintextRight;
        public final int ciphertextLeft;
        public final int ciphertextRight;
        
        public PlaintextCiphertextPair(int plaintextLeft, int plaintextRight, 
                                      int ciphertextLeft, int ciphertextRight) {
            this.plaintextLeft = plaintextLeft;
            this.plaintextRight = plaintextRight;
            this.ciphertextLeft = ciphertextLeft;
            this.ciphertextRight = ciphertextRight;
        }
    }
    
    private List<PlaintextCiphertextPair> pairs;
    
    public PlaintextCiphertextLoader() {
        this.pairs = new ArrayList<>();
    }
    
    // Loads pairs from file - looks for "Plaintext=" and "Ciphertext=" lines
    // Splits each 64-bit hex value into left and right 32-bit halves
    public void loadPairsFromFile(String filePath) throws IOException {
        pairs.clear();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String currentPlaintext = null;
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                // Skip empty lines
                if (line.isEmpty()) {
                    continue;
                }
                
                // Check if this is a plaintext line
                if (line.startsWith("Plaintext=")) {
                    // Extract hex string after "Plaintext=  " (note the spaces)
                    String hexPart = line.substring(line.indexOf("=") + 1).trim();
                    currentPlaintext = hexPart;
                }
                // Check if this is a ciphertext line
                else if (line.startsWith("Ciphertext=")) {
                    if (currentPlaintext == null) {
                        throw new IOException("Ciphertext found without corresponding plaintext");
                    }
                    
                    // Extract hex string after "Ciphertext="
                    String hexPart = line.substring(line.indexOf("=") + 1).trim();
                    
                    // Parse both hex strings and split into left/right halves
                    int[] plaintextParts = parseHexString(currentPlaintext);
                    int[] ciphertextParts = parseHexString(hexPart);
                    
                    int plaintextLeft = plaintextParts[0];
                    int plaintextRight = plaintextParts[1];
                    int ciphertextLeft = ciphertextParts[0];
                    int ciphertextRight = ciphertextParts[1];
                    
                    pairs.add(new PlaintextCiphertextPair(
                        plaintextLeft, plaintextRight,
                        ciphertextLeft, ciphertextRight
                    ));
                    
                    currentPlaintext = null; // Reset for next pair
                }
            }
        }
    }
    
    // Parses 16-char hex string: first 8 chars = left half, last 8 = right half
    public int[] parseHexString(String hexString) {
        hexString = hexString.trim();
        if (hexString.length() != 16) {
            throw new IllegalArgumentException("Hex string must be exactly 16 characters (64 bits)");
        }
        
        // First 8 hex characters = left half (32 bits)
        String leftHex = hexString.substring(0, 8);
        // Last 8 hex characters = right half (32 bits)
        String rightHex = hexString.substring(8, 16);
        
        int left = (int) Long.parseLong(leftHex, 16);
        int right = (int) Long.parseLong(rightHex, 16);
        
        return new int[]{left, right};
    }
    
    public List<PlaintextCiphertextPair> getPairs() {
        return pairs;
    }
    
    public int getPairCount() {
        return pairs.size();
    }
}

