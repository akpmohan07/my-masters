package com.mohanverse.dev.feal4;

import java.io.IOException;

// Main entry point for FEAL-4 linear cryptanalysis
// Loads pairs, initializes components, runs key recovery, outputs results
public class FEAL4Cryptanalysis {
    
    public static void main(String[] args) {
        System.out.println("=== FEAL-4 Linear Cryptanalysis ===");
        System.out.println("Starting key recovery process...\n");
        
        long startTime = System.currentTimeMillis();
        
        try {
            // Run the cryptanalysis
            int[] recoveredKeys = runCryptanalysis();
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            if (recoveredKeys != null) {
                printResults(recoveredKeys);
                System.out.println("\nTotal time: " + duration + " ms");
                System.out.println("Cryptanalysis completed successfully!");
            } else {
                System.out.println("\nKey recovery failed. Please check the input data.");
                System.out.println("Total time: " + duration + " ms");
            }
            
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    // Main cryptanalysis process
    public static int[] runCryptanalysis() throws IOException {
        // Step 1: Load plaintext/ciphertext pairs
        System.out.println("Step 1: Loading plaintext/ciphertext pairs...");
        PlaintextCiphertextLoader loader = new PlaintextCiphertextLoader();
        
        // Try to load from known.txt (current dir first, then absolute path)
        String filePath = "known.txt";
        try {
            loader.loadPairsFromFile(filePath);
        } catch (IOException e) {
            // Fallback to absolute path if current dir doesn't work
            filePath = "/Users/akpmohan/Workspace/crypt/feal-4-on-crack-v2/known.txt";
            loader.loadPairsFromFile(filePath);
        }
        
        int pairCount = loader.getPairCount();
        System.out.println("  Loaded " + pairCount + " plaintext/ciphertext pairs");
        
        if (pairCount == 0) {
            throw new IOException("No pairs loaded from file");
        }
        
        // Step 2: Initialize helper components
        System.out.println("\nStep 2: Initializing analysis components...");
        FealOperations fealOps = new FealOperations();
        KeyCandidateFilter analyzer = new KeyCandidateFilter(
            fealOps, loader.getPairs()
        );
        KeyCandidateGenerator keyGenerator = new KeyCandidateGenerator();
        System.out.println("  Components initialized");
        
        // Step 3: Create key recovery solver
        System.out.println("\nStep 3: Creating key recovery solver...");
        KeyRecoverySolver solver = new KeyRecoverySolver(
            loader, fealOps, analyzer, keyGenerator
        );
        System.out.println("  Solver ready");
        
        // Step 4: Recover keys
        System.out.println("\nStep 4: Recovering keys using linear cryptanalysis...");
        System.out.println("  This may take several minutes...\n");
        
        int[] recoveredKeys = solver.recoverAllKeys();
        
        return recoveredKeys;
    }
    
    // Prints recovered keys in formatted output
    public static void printResults(int[] keys) {
        if (keys == null || keys.length != 6) {
            System.out.println("Invalid key array");
            return;
        }
        
        System.out.println("\n=== Recovered Keys ===");
        System.out.println("Key0: 0x" + Integer.toHexString(keys[0]));
        System.out.println("Key1: 0x" + Integer.toHexString(keys[1]));
        System.out.println("Key2: 0x" + Integer.toHexString(keys[2]));
        System.out.println("Key3: 0x" + Integer.toHexString(keys[3]));
        System.out.println("Key4: 0x" + Integer.toHexString(keys[4]));
        System.out.println("Key5: 0x" + Integer.toHexString(keys[5]));
        
        System.out.println("\nFormatted output :");
        System.out.print("0x" + Integer.toHexString(keys[0]));
        for (int i = 1; i < keys.length; i++) {
            System.out.print("\t0x" + Integer.toHexString(keys[i]));
        }
        System.out.println();
    }
}

