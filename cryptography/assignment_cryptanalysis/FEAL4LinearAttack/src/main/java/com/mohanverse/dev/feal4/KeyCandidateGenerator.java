package com.mohanverse.dev.feal4;

import java.util.ArrayList;
import java.util.List;

// Generates key candidates for brute-force search
// Key space divided into inner bytes (12 bits) and outer bytes (20 bits)
// This lets us filter inner bytes first, then outer bytes
public class KeyCandidateGenerator {
    
    // Generates all 4096 inner byte candidates (12 bits)
    // Inner bytes: bits 10-15 and 18-23 of the 32-bit key
    public List<Integer> generateInnerByteKeyCandidates() {
        List<Integer> candidates = new ArrayList<>();
        
        // We have 12 bits total: 2^12 = 4096 possibilities
        for (int i = 0; i < 4096; i++) {
            // Extract the two 6-bit parts
            // Lower 6 bits (bits 0-5) go to byte 1 (bits 8-15)
            int lower6Bits = i & 0x3F;  // Mask to get bits 0-5
            
            // Upper 6 bits (bits 6-11) go to byte 2 (bits 16-23)
            int upper6Bits = (i >>> 6) & 0x3F;  // Shift right by 6, then mask
            
            // Construct the inner byte key:
            // - Byte 1 (bits 8-15): lower6Bits
            // - Byte 2 (bits 16-23): upper6Bits
            int innerByteKey = (upper6Bits << 16) | (lower6Bits << 8);
            
            candidates.add(innerByteKey);
        }
        
        return candidates;
    }
    
    // Generates single outer byte candidate (20-bit index, 0-1048575)
    // Matches old solution's logic but with different structure
    public int generateOuterByteCandidate(int index, int innerByteKey) {
        if (index < 0 || index >= 1048576) {
            throw new IllegalArgumentException("Index must be between 0 and 1048575");
        }
        
        int innerByte1 = (innerByteKey >>> 8) & 0xFF;
        int innerByte2 = (innerByteKey >>> 16) & 0xFF;
        
        // Following the old solution's key generation logic
        int a0 = (((index & 0xF) >>> 2) << 6) + innerByte2;
        int a1 = ((index & 0x3) << 6) + innerByte1;
        int b0 = (index >>> 12) & 0xFF;
        int b3 = (index >>> 4) & 0xFF;
        int b1 = b0 ^ a0;
        int b2 = b3 ^ a1;
        
        return (b0 << 24) | (b1 << 16) | (b2 << 8) | b3;
    }
}

