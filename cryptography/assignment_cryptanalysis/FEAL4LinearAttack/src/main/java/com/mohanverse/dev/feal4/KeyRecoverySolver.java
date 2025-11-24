package com.mohanverse.dev.feal4;

import java.util.List;

// Main class for key recovery using linear cryptanalysis
// Recovers keys sequentially: Key0 -> Key1 -> Key2 -> Key3, then derives Key4/Key5
public class KeyRecoverySolver {
    
    private final FealOperations fealOps;
    private final KeyCandidateFilter analyzer;
    private final KeyCandidateGenerator keyGenerator;
    private final List<PlaintextCiphertextLoader.PlaintextCiphertextPair> pairs;
    
    public KeyRecoverySolver(PlaintextCiphertextLoader loader,
                             FealOperations fealOps,
                             KeyCandidateFilter analyzer,
                             KeyCandidateGenerator keyGenerator) {
        this.fealOps = fealOps;
        this.analyzer = analyzer;
        this.keyGenerator = keyGenerator;
        this.pairs = loader.getPairs();
    }
    
    // Tries all Key0 candidates and calls callback for each valid one
    private void tryAllKey0Candidates(java.util.function.Consumer<Integer> callback) {
        List<Integer> innerCandidates = keyGenerator.generateInnerByteKeyCandidates();
        
        moveToNextInnerCandidate:
        for (int innerCandidate : innerCandidates) {
            int firstInnerResult = analyzer.evaluateInnerBytesForKey0(0, innerCandidate);
            for (int i = 1; i < pairs.size(); i++) {
                if (analyzer.evaluateInnerBytesForKey0(i, innerCandidate) != firstInnerResult) {
                    continue moveToNextInnerCandidate;
                }
            }
            
            moveToNextOuterCandidate:
            for (int outerIndex = 0; outerIndex < 1048576; outerIndex++) {
                int candidateKey0 = keyGenerator.generateOuterByteCandidate(outerIndex, innerCandidate);
                
                int firstOuterApprox = analyzer.evaluateOuterBytesForKey0(0, candidateKey0);
                
                for (int i = 1; i < pairs.size(); i++) {
                    int outerResult = analyzer.evaluateOuterBytesForKey0(i, candidateKey0);
                    if (outerResult != firstOuterApprox) {
                        continue moveToNextOuterCandidate;
                    }
                }

                // Found valid Key0 candidate, moving to Key1
                callback.accept(candidateKey0);
            }
        }
    }
    
    // Tries all Key1 candidates and calls callback for each valid one
    private void tryAllKey1Candidates(int key0, java.util.function.Consumer<Integer> callback) {
        List<Integer> innerCandidates = keyGenerator.generateInnerByteKeyCandidates();
        
        moveToNextInnerCandidate:
        for (int innerCandidate : innerCandidates) {
            int firstInnerResult = analyzer.evaluateInnerBytesForKey1(0, innerCandidate, key0);
            for (int i = 1; i < pairs.size(); i++) {
                if (analyzer.evaluateInnerBytesForKey1(i, innerCandidate, key0) != firstInnerResult) {
                    continue moveToNextInnerCandidate;
                }
            }
            
            moveToNextOuterCandidate:
            for (int outerIndex = 0; outerIndex < 1048576; outerIndex++) {
                int candidateKey1 = keyGenerator.generateOuterByteCandidate(outerIndex, innerCandidate);
                
                int firstOuterApprox = analyzer.evaluateOuterBytesForKey1(0, candidateKey1, key0);
                
                for (int i = 1; i < pairs.size(); i++) {
                    int outerResult = analyzer.evaluateOuterBytesForKey1(i, candidateKey1, key0);
                    if (outerResult != firstOuterApprox) {
                        continue moveToNextOuterCandidate;
                    }
                }

                // Found valid Key1 candidate, moving to Key2
                callback.accept(candidateKey1);
            }
        }
    }
    
    // Tries all Key2 candidates and calls callback for each valid one
    private void tryAllKey2Candidates(int key0, int key1, java.util.function.Consumer<Integer> callback) {
        List<Integer> innerCandidates = keyGenerator.generateInnerByteKeyCandidates();
        
        moveToNextInnerCandidate:
        for (int innerCandidate : innerCandidates) {
            int firstInnerResult = analyzer.evaluateInnerBytesForKey2(0, innerCandidate, key0, key1);
            for (int i = 1; i < pairs.size(); i++) {
                if (analyzer.evaluateInnerBytesForKey2(i, innerCandidate, key0, key1) != firstInnerResult) {
                    continue moveToNextInnerCandidate;
                }
            }
            
            moveToNextOuterCandidate:
            for (int outerIndex = 0; outerIndex < 1048576; outerIndex++) {
                int candidateKey2 = keyGenerator.generateOuterByteCandidate(outerIndex, innerCandidate);
                
                int firstOuterApprox = analyzer.evaluateOuterBytesForKey2(0, candidateKey2, key0, key1);
                
                for (int i = 1; i < pairs.size(); i++) {
                    int outerResult = analyzer.evaluateOuterBytesForKey2(i, candidateKey2, key0, key1);
                    if (outerResult != firstOuterApprox) {
                        continue moveToNextOuterCandidate;
                    }
                }

                // Found valid Key2 candidate, moving to Key3
                callback.accept(candidateKey2);
            }
        }
    }
    
    // Tries all Key3 candidates and calls callback for each valid one
    private void tryAllKey3Candidates(int key0, int key1, int key2, java.util.function.Consumer<Integer> callback) {
        List<Integer> innerCandidates = keyGenerator.generateInnerByteKeyCandidates();
        
        moveToNextInnerCandidate:
        for (int innerCandidate : innerCandidates) {
            int firstInnerResult = analyzer.evaluateInnerBytesForKey3(0, innerCandidate, key0, key1, key2);
            for (int i = 1; i < pairs.size(); i++) {
                if (analyzer.evaluateInnerBytesForKey3(i, innerCandidate, key0, key1, key2) != firstInnerResult) {
                    continue moveToNextInnerCandidate;
                }
            }
            
            moveToNextOuterCandidate:
            for (int outerIndex = 0; outerIndex < 1048576; outerIndex++) {
                int candidateKey3 = keyGenerator.generateOuterByteCandidate(outerIndex, innerCandidate);
                
                int firstOuterApprox = analyzer.evaluateOuterBytesForKey3(0, candidateKey3, key0, key1, key2);
                
                for (int i = 1; i < pairs.size(); i++) {
                    int outerResult = analyzer.evaluateOuterBytesForKey3(i, candidateKey3, key0, key1, key2);
                    if (outerResult != firstOuterApprox) {
                        continue moveToNextOuterCandidate;
                    }
                }

                // Found valid Key3 candidate, moving to derive Key4 and Key5
                callback.accept(candidateKey3);
            }
        }
    }
    
    // Derives Key4 and Key5 from round keys (Key0-Key3)
    private void deriveKey4AndKey5(int key0, int key1, int key2, int key3, java.util.function.Consumer<int[]> callback) {
        int[] finalKeys = deriveFinalKeys(key0, key1, key2, key3);
        callback.accept(finalKeys);
    }
    
    // Validates the complete key set (Key0-Key5)
    private void validateKeySet(int key0, int key1, int key2, int key3, int key4, int key5, java.util.function.Consumer<int[]> callback) {
        int[] fullKey = new int[]{key0, key1, key2, key3, key4, key5};
        if (validateFullKey(fullKey)) {
            callback.accept(fullKey);
        }
    }
    
    // Derives Key4 and Key5 from round keys using first pair
    // Based on FEAL-4 encryption equations
    private int[] deriveFinalKeys(int key0, int key1, int key2, int key3) {
        PlaintextCiphertextLoader.PlaintextCiphertextPair pair = pairs.get(0);
        
        int L0 = pair.plaintextLeft;
        int R0 = pair.plaintextRight;
        int L4 = pair.ciphertextLeft;
        int R4 = pair.ciphertextRight;
        
        // Compute y0, y1, y2, y3 (F function outputs for each round)
        int y0 = fealOps.evaluateFFunction(fealOps.xorThree(L0, R0, key0));
        int y1 = fealOps.evaluateFFunction(fealOps.xorThree(L0, y0, key1));
        int y2 = fealOps.evaluateFFunction(fealOps.xorFour(L0, R0, y1, key2));
        int y3 = fealOps.evaluateFFunction(fealOps.xorFour(L0, y0, y2, key3));
        
        // Derive Key4 and Key5
        int key4 = fealOps.xorFour(L0, R0, y1, y3) ^ L4;
        int key5 = fealOps.xorFour(R0, y1, y3, y0) ^ y2 ^ R4;
        
        return new int[]{key4, key5};
    }
    
    // Helper: reconstructs 8-byte array from two 32-bit halves
    private byte[] reconstructBytes(int left, int right) {
        byte[] bytes = new byte[8];
        fealOps.unpackBytes(left, bytes, 0);
        fealOps.unpackBytes(right, bytes, 4);
        return bytes;
    }
    
    // Validates key by decrypting all pairs and checking if they match plaintexts
    private boolean validateFullKey(int[] fullKey) {
        if (fullKey.length != 6) {
            return false;
        }
        
        // Convert int array to format expected by FealProgram
        int[] keyArray = new int[6];
        System.arraycopy(fullKey, 0, keyArray, 0, 6);
        
        // Test each pair
        for (PlaintextCiphertextLoader.PlaintextCiphertextPair pair : pairs) {
            // Reconstruct full byte arrays
            byte[] ciphertextBytes = reconstructBytes(pair.ciphertextLeft, pair.ciphertextRight);
            byte[] plaintextBytes = reconstructBytes(pair.plaintextLeft, pair.plaintextRight);
            
            // Decrypt
            byte[] decryptedBytes = ciphertextBytes.clone();
            FealProgram.decrypt(decryptedBytes, keyArray);
            
            // Compare entire arrays directly
            if (!java.util.Arrays.equals(decryptedBytes, plaintextBytes)) {
                return false;
            }
        }
        
        return true;
    }
    
    // Main recovery method - tries all key combinations using nested method calls
    // Prints all valid keys found
    public int[] recoverAllKeys() {
        final int[][] firstValidKey = new int[1][];
        final int[] combinationCount = new int[1];
        
        tryAllKey0Candidates(key0 -> {
            tryAllKey1Candidates(key0, key1 -> {
                tryAllKey2Candidates(key0, key1, key2 -> {
                    tryAllKey3Candidates(key0, key1, key2, key3 -> {
                        deriveKey4AndKey5(key0, key1, key2, key3, finalKeys -> {
                            validateKeySet(key0, key1, key2, key3, finalKeys[0], finalKeys[1], fullKey -> {
                                combinationCount[0]++;
                                System.out.println(combinationCount[0] + ". " + 
                                    "0x" + Integer.toHexString(key0) + 
                                    "\t0x" + Integer.toHexString(key1) + 
                                    "\t0x" + Integer.toHexString(key2) + 
                                    "\t0x" + Integer.toHexString(key3) + 
                                    "\t0x" + Integer.toHexString(finalKeys[0]) + 
                                    "\t0x" + Integer.toHexString(finalKeys[1]));
                                if (firstValidKey[0] == null) {
                                    firstValidKey[0] = fullKey;
                                }
                            });
                        });
                    });
                });
            });
        });
        
        System.out.println("Total valid key combinations found: " + combinationCount[0]);
        
        if (firstValidKey[0] != null) {
            System.out.println("Key recovery completed. Found at least one valid key combination.");
            return firstValidKey[0];
        } else {
            System.out.println("Key recovery failed - no valid key combination found!");
            return null;
        }
    }
}

