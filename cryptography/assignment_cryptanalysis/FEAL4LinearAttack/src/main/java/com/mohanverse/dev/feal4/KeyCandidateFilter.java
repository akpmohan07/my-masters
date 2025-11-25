package com.mohanverse.dev.feal4;

import java.util.List;

// Filters key candidates using linear cryptanalysis approximations
// Linear cryptanalysis uses statistical biases - certain bit combinations have non-random distribution
// Inner bytes: bits 5, 13, 21 (sometimes 15)
// Outer bytes: bits 7, 13, 15, 23, 31
public class KeyCandidateFilter {
    
    private final FealOperations fealOps;
    private final List<PlaintextCiphertextLoader.PlaintextCiphertextPair> pairs;
    
    public KeyCandidateFilter(FealOperations fealOps, 
                                       List<PlaintextCiphertextLoader.PlaintextCiphertextPair> pairs) {
        this.fealOps = fealOps;
        this.pairs = pairs;
    }
    
    // Inner byte approximation for Key0
    // Formula: S5,13,21(L0⊕R0⊕L4) ⊕ S15(L0⊕L4⊕R4) ⊕ S15(F(L0⊕R0⊕K0))
    // Sx = bit at position x
    public int evaluateInnerBytesForKey0(int pairIndex, int candidateKey0) {
        PlaintextCiphertextLoader.PlaintextCiphertextPair pair = pairs.get(pairIndex);
        
        int L0 = pair.plaintextLeft;
        int R0 = pair.plaintextRight;
        int L4 = pair.ciphertextLeft;
        int R4 = pair.ciphertextRight;
        
        // First term: XOR of bits 5, 13, 21 from (L0 ⊕ R0 ⊕ L4)
        int term1 = fealOps.computeXorOfBits(
            fealOps.xorThree(L0, R0, L4),
            new int[]{5, 13, 21}
        );
        
        // Second term: Bit 15 from (L0 ⊕ L4 ⊕ R4)
        int term2 = fealOps.extractBitAtPosition(
            fealOps.xorThree(L0, L4, R4),
            15
        );
        
        // Third term: Bit 15 from F(L0 ⊕ R0 ⊕ K0)
        int fInput = fealOps.xorThree(L0, R0, candidateKey0);
        int fOutput = fealOps.evaluateFFunction(fInput);
        int term3 = fealOps.extractBitAtPosition(fOutput, 15);
        
        // The approximation is the XOR of all three terms
        return term1 ^ term2 ^ term3;
    }
    
    // Outer byte approximation for Key0
    // S13(L0⊕R0⊕L4) ⊕ S7,15,23,31(L0⊕L4⊕R4) ⊕ S7,15,23,31(F(L0⊕R0⊕K0))
    public int evaluateOuterBytesForKey0(int pairIndex, int candidateKey0) {
        PlaintextCiphertextLoader.PlaintextCiphertextPair pair = pairs.get(pairIndex);
        
        int L0 = pair.plaintextLeft;
        int R0 = pair.plaintextRight;
        int L4 = pair.ciphertextLeft;
        int R4 = pair.ciphertextRight;
        
        // First term: Bit 13 from (L0 ⊕ R0 ⊕ L4)
        int term1 = fealOps.extractBitAtPosition(
            fealOps.xorThree(L0, R0, L4),
            13
        );
        
        // Second term: XOR of bits 7, 15, 23, 31 from (L0 ⊕ L4 ⊕ R4)
        int term2 = fealOps.computeXorOfBits(
            fealOps.xorThree(L0, L4, R4),
            new int[]{7, 15, 23, 31}
        );
        
        // Third term: XOR of bits 7, 15, 23, 31 from F(L0 ⊕ R0 ⊕ K0)
        int fInput = fealOps.xorThree(L0, R0, candidateKey0);
        int fOutput = fealOps.evaluateFFunction(fInput);
        int term3 = fealOps.computeXorOfBits(fOutput, new int[]{7, 15, 23, 31});
        
        return term1 ^ term2 ^ term3;
    }
    
    // Inner byte approximation for Key1 (Key0 is known)
    // S5,13,21(L0⊕L4⊕R4) ⊕ S15(F(L0⊕y0⊕K1)) where y0 = F(L0⊕R0⊕K0)
    public int evaluateInnerBytesForKey1(int pairIndex, int candidateKey1, int knownKey0) {
        PlaintextCiphertextLoader.PlaintextCiphertextPair pair = pairs.get(pairIndex);
        
        int L0 = pair.plaintextLeft;
        int R0 = pair.plaintextRight;
        int L4 = pair.ciphertextLeft;
        int R4 = pair.ciphertextRight;
        
        // First term: XOR of bits 5, 13, 21 from (L0 ⊕ L4 ⊕ R4)
        int term1 = fealOps.computeXorOfBits(
            fealOps.xorThree(L0, L4, R4),
            new int[]{5, 13, 21}
        );
        
        // Compute y0 = F(L0 ⊕ R0 ⊕ K0)
        int y0Input = fealOps.xorThree(L0, R0, knownKey0);
        int y0 = fealOps.evaluateFFunction(y0Input);
        
        // Second term: Bit 15 from F(L0 ⊕ y0 ⊕ K1)
        int fInput = fealOps.xorThree(L0, y0, candidateKey1);
        int fOutput = fealOps.evaluateFFunction(fInput);
        int term2 = fealOps.extractBitAtPosition(fOutput, 15);
        
        return term1 ^ term2;
    }
    
    // Outer byte approximation for Key1
    // S13(L0⊕L4⊕R4) ⊕ S7,15,23,31(y1) where y1 = F(L0⊕y0⊕K1)
    public int evaluateOuterBytesForKey1(int pairIndex, int candidateKey1, int knownKey0) {
        PlaintextCiphertextLoader.PlaintextCiphertextPair pair = pairs.get(pairIndex);
        
        int L0 = pair.plaintextLeft;
        int R0 = pair.plaintextRight;
        int L4 = pair.ciphertextLeft;
        int R4 = pair.ciphertextRight;
        
        // First term: Bit 13 from (L0 ⊕ L4 ⊕ R4)
        int term1 = fealOps.extractBitAtPosition(
            fealOps.xorThree(L0, L4, R4),
            13
        );
        
        // Compute y0 = F(L0 ⊕ R0 ⊕ K0)
        int y0Input = fealOps.xorThree(L0, R0, knownKey0);
        int y0 = fealOps.evaluateFFunction(y0Input);
        
        // Compute y1 = F(L0 ⊕ y0 ⊕ K1)
        int y1Input = fealOps.xorThree(L0, y0, candidateKey1);
        int y1 = fealOps.evaluateFFunction(y1Input);
        
        // Second term: XOR of bits 7, 15, 23, 31 from y1
        int term2 = fealOps.computeXorOfBits(y1, new int[]{7, 15, 23, 31});
        
        return term1 ^ term2;
    }
    
    // Inner byte approximation for Key2 (Key0, Key1 known)
    // S5,13,21(L0⊕R0⊕L4) ⊕ S15(F(L0⊕R0⊕y1⊕K2))
    public int evaluateInnerBytesForKey2(int pairIndex, int candidateKey2, 
                                                     int knownKey0, int knownKey1) {
        PlaintextCiphertextLoader.PlaintextCiphertextPair pair = pairs.get(pairIndex);
        
        int L0 = pair.plaintextLeft;
        int R0 = pair.plaintextRight;
        int L4 = pair.ciphertextLeft;
        
        // First term: XOR of bits 5, 13, 21 from (L0 ⊕ R0 ⊕ L4)
        int term1 = fealOps.computeXorOfBits(
            fealOps.xorThree(L0, R0, L4),
            new int[]{5, 13, 21}
        );
        
        // Compute y0 = F(L0 ⊕ R0 ⊕ K0)
        int y0Input = fealOps.xorThree(L0, R0, knownKey0);
        int y0 = fealOps.evaluateFFunction(y0Input);
        
        // Compute y1 = F(L0 ⊕ y0 ⊕ K1)
        int y1Input = fealOps.xorThree(L0, y0, knownKey1);
        int y1 = fealOps.evaluateFFunction(y1Input);
        
        // Second term: Bit 15 from F(L0 ⊕ R0 ⊕ y1 ⊕ K2)
        int fInput = fealOps.xorFour(L0, R0, y1, candidateKey2);
        int fOutput = fealOps.evaluateFFunction(fInput);
        int term2 = fealOps.extractBitAtPosition(fOutput, 15);
        
        return term1 ^ term2;
    }
    
    // Outer byte approximation for Key2
    // S13(L0⊕R0⊕L4) ⊕ S7,15,23,31(y2) where y2 = F(L0⊕R0⊕y1⊕K2)
    public int evaluateOuterBytesForKey2(int pairIndex, int candidateKey2,
                                                     int knownKey0, int knownKey1) {
        PlaintextCiphertextLoader.PlaintextCiphertextPair pair = pairs.get(pairIndex);
        
        int L0 = pair.plaintextLeft;
        int R0 = pair.plaintextRight;
        int L4 = pair.ciphertextLeft;
        
        // First term: Bit 13 from (L0 ⊕ R0 ⊕ L4)
        int term1 = fealOps.extractBitAtPosition(
            fealOps.xorThree(L0, R0, L4),
            13
        );
        
        // Compute y0 = F(L0 ⊕ R0 ⊕ K0)
        int y0Input = fealOps.xorThree(L0, R0, knownKey0);
        int y0 = fealOps.evaluateFFunction(y0Input);
        
        // Compute y1 = F(L0 ⊕ y0 ⊕ K1)
        int y1Input = fealOps.xorThree(L0, y0, knownKey1);
        int y1 = fealOps.evaluateFFunction(y1Input);
        
        // Compute y2 = F(L0 ⊕ R0 ⊕ y1 ⊕ K2)
        int y2Input = fealOps.xorFour(L0, R0, y1, candidateKey2);
        int y2 = fealOps.evaluateFFunction(y2Input);
        
        // Second term: XOR of bits 7, 15, 23, 31 from y2
        int term2 = fealOps.computeXorOfBits(y2, new int[]{7, 15, 23, 31});
        
        return term1 ^ term2;
    }
    
    // Inner byte approximation for Key3 (all previous keys known)
    // S5,13,21(L0⊕L4⊕R4) ⊕ S15(L0⊕R0⊕L4) ⊕ S15(F(L0⊕y0⊕y2⊕K3))
    public int evaluateInnerBytesForKey3(int pairIndex, int candidateKey3,
                                                    int knownKey0, int knownKey1, int knownKey2) {
        PlaintextCiphertextLoader.PlaintextCiphertextPair pair = pairs.get(pairIndex);
        
        int L0 = pair.plaintextLeft;
        int R0 = pair.plaintextRight;
        int L4 = pair.ciphertextLeft;
        int R4 = pair.ciphertextRight;
        
        // First term: XOR of bits 5, 13, 21 from (L0 ⊕ L4 ⊕ R4)
        int term1 = fealOps.computeXorOfBits(
            fealOps.xorThree(L0, L4, R4),
            new int[]{5, 13, 21}
        );
        
        // Second term: Bit 15 from (L0 ⊕ R0 ⊕ L4)
        int term2 = fealOps.extractBitAtPosition(
            fealOps.xorThree(L0, R0, L4),
            15
        );
        
        // Compute y0 = F(L0 ⊕ R0 ⊕ K0)
        int y0Input = fealOps.xorThree(L0, R0, knownKey0);
        int y0 = fealOps.evaluateFFunction(y0Input);
        
        // Compute y1 = F(L0 ⊕ y0 ⊕ K1)
        int y1Input = fealOps.xorThree(L0, y0, knownKey1);
        int y1 = fealOps.evaluateFFunction(y1Input);
        
        // Compute y2 = F(L0 ⊕ R0 ⊕ y1 ⊕ K2)
        int y2Input = fealOps.xorFour(L0, R0, y1, knownKey2);
        int y2 = fealOps.evaluateFFunction(y2Input);
        
        // Third term: Bit 15 from F(L0 ⊕ y0 ⊕ y2 ⊕ K3)
        int fInput = fealOps.xorFour(L0, y0, y2, candidateKey3);
        int fOutput = fealOps.evaluateFFunction(fInput);
        int term3 = fealOps.extractBitAtPosition(fOutput, 15);
        
        return term1 ^ term2 ^ term3;
    }
    
    // Outer byte approximation for Key3
    // S13(L0⊕L4⊕R4) ⊕ S7,15,23,31(L0⊕R0⊕L4) ⊕ S7,15,23,31(y3)
    public int evaluateOuterBytesForKey3(int pairIndex, int candidateKey3,
                                                     int knownKey0, int knownKey1, int knownKey2) {
        PlaintextCiphertextLoader.PlaintextCiphertextPair pair = pairs.get(pairIndex);
        
        int L0 = pair.plaintextLeft;
        int R0 = pair.plaintextRight;
        int L4 = pair.ciphertextLeft;
        int R4 = pair.ciphertextRight;
        
        // First term: Bit 13 from (L0 ⊕ L4 ⊕ R4)
        int term1 = fealOps.extractBitAtPosition(
            fealOps.xorThree(L0, L4, R4),
            13
        );
        
        // Second term: XOR of bits 7, 15, 23, 31 from (L0 ⊕ R0 ⊕ L4)
        int term2 = fealOps.computeXorOfBits(
            fealOps.xorThree(L0, R0, L4),
            new int[]{7, 15, 23, 31}
        );
        
        // Compute y0 = F(L0 ⊕ R0 ⊕ K0)
        int y0Input = fealOps.xorThree(L0, R0, knownKey0);
        int y0 = fealOps.evaluateFFunction(y0Input);
        
        // Compute y1 = F(L0 ⊕ y0 ⊕ K1)
        int y1Input = fealOps.xorThree(L0, y0, knownKey1);
        int y1 = fealOps.evaluateFFunction(y1Input);
        
        // Compute y2 = F(L0 ⊕ R0 ⊕ y1 ⊕ K2)
        int y2Input = fealOps.xorFour(L0, R0, y1, knownKey2);
        int y2 = fealOps.evaluateFFunction(y2Input);
        
        // Compute y3 = F(L0 ⊕ y0 ⊕ y2 ⊕ K3)
        int y3Input = fealOps.xorFour(L0, y0, y2, candidateKey3);
        int y3 = fealOps.evaluateFFunction(y3Input);
        
        // Third term: XOR of bits 7, 15, 23, 31 from y3
        int term3 = fealOps.computeXorOfBits(y3, new int[]{7, 15, 23, 31});
        
        return term1 ^ term2 ^ term3;
    }
}

