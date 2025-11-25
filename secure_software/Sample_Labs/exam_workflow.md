# CSC1135 Lab Exam Workflow Guide

## Pre-Exam Preparation (Do This Before Exam Day)

### 1. Practice with Sample Exam
- Complete the sample exam under timed conditions
- Familiarize yourself with the format and question types
- Practice using gdb commands efficiently
- Test your lockbox upload procedure

### 2. Verify Lockbox Access
- Upload your sample exam solution to lockbox
- Verify your DCU credentials work
- Confirm you understand the upload procedure
- Note the upload interface layout

### 3. Prepare Reference Materials (If Allowed)
- Create a paper cheatsheet of gdb commands (if permitted)
- List common patterns for buffer overflows, format strings, etc.
- Note common address calculation formulas

## Exam Day Workflow (100 Minutes Total)

### Phase 1: Initial Setup (0-2 minutes)

**Critical Actions:**
1. Open terminal and navigate to `labexam/` directory
2. Open `exam.txt` in your preferred editor (vim, nano, gedit, etc.)
3. **IMMEDIATELY fill in:**
   - Your name (replace `*` after "Enter your name here:")
   - Your student number (replace `*` after "Enter your student number here:")
   - Copy the lockbox URL from the exam file
4. **SAVE THE FILE** (Ctrl+S or :w in vim)
5. Verify the save was successful

**Time Check:** Should take no more than 2 minutes

---

### Phase 2: Quick Scan (2-7 minutes)

**Read Through All Questions:**
1. Read Section A questions (typically stack/gdb analysis)
2. Read Section B questions (typically exploit questions)
3. Identify which questions look easiest
4. Identify which questions look most challenging
5. Note any questions that require specific executables (q1, q2, q3, etc.)

**Strategy Decision:**
- **If confident:** Start with Section A (builds confidence)
- **If nervous:** Start with easiest questions from either section
- **Mark difficult questions** mentally or with a placeholder

**Time Check:** 5 minutes maximum

---

### Phase 3: Section A - Stack Analysis (7-47 minutes)

**Allocated Time: 40 minutes** (for 40 marks)

#### Question Strategy:
1. **Read question carefully** - understand what's being asked
2. **Set up gdb** - load the executable, set breakpoints
3. **Work systematically** - follow the debugging steps
4. **Document as you go** - copy gdb commands/outputs into answer
5. **Save after each sub-question** (1a, 1b, etc.)

#### Common Patterns:
- **Return address questions:** Use `info frame` to find saved eip
- **Stack frame questions:** Use `info stack` to see all frames
- **Offset calculations:** Use `p/d address1 - address2` then divide by 4 for int arrays
- **Assembly questions:** Use `disassemble function_name`
- **Byte encoding:** Use `x/3bx function_name` to see raw bytes

#### Time Allocation per Question:
- 1a-1e: ~8 minutes each (8 marks each)
- If stuck on one question, move on and return later

**Save Reminder:** Save every 5-10 minutes during this section

---

### Phase 4: Section B - Exploits (47-107 minutes)

**Allocated Time: 60 minutes** (for 60 marks)

#### Question Strategy:
Each exploit question typically has 3 parts:
1. **Locate vulnerability** (3 marks) - Read code, identify unsafe function
2. **Provide fix** (4 marks) - Write safe replacement code
3. **Create exploit** (8 marks) - Craft input to trigger desired behavior

#### Question 2: Buffer Overflow
**Time: ~15 minutes**

1. **Find vulnerability** (2 min):
   - Look for `strcat`, `strcpy`, `gets` without bounds checking
   - Answer format: "Line X: strcat(buffer, argv[1]) without size check"

2. **Provide fix** (3 min):
   - Use `strncat` or `snprintf` with proper bounds
   - Answer format: "strncat(buffer, argv[1], sizeof(buffer) - strlen(buffer) - 1);"

3. **Create exploit** (10 min):
   - Find target function address: `disassemble main` in gdb
   - Test offset: `perl -e 'system "./q2", "A"x38 . "BBBB"'`
   - Check eip when crashes - if 0x42424242, offset is correct
   - Final exploit: `perl -e 'system "./q2", "A"x38 . "\xee\x83\x04\x08"'`

#### Question 3: Array Index Overflow
**Time: ~15 minutes**

1. **Find vulnerability** (2 min):
   - Look for array access without bounds checking
   - Answer: "values[index] = value; without index validation"

2. **Provide fix** (3 min):
   - Add bounds check: `if (index < 0 || index >= ARRAY_SIZE) return;`

3. **Create exploit** (10 min):
   - Find function address: `print &foo` → `print /d 0x8048356`
   - Find offset: `p/d return_addr - values_addr` → divide by 4
   - Exploit: `perl -e 'print "515 134513494\n-1 1\n"' | ./q3`

#### Question 4: Format String
**Time: ~15 minutes**

1. **Find vulnerability** (2 min):
   - Look for `printf(user_input)` without format string
   - Answer: "printf(argv[1]) allows format string injection"

2. **Provide fix** (3 min):
   - Use `printf("%s", argv[1])` or `fputs(argv[1], stdout)`

3. **Create exploit** (10 min):
   - Find variable address: `print &rating`
   - Find stack position: `perl -e 'system "./q4", "AAAA" . "%x%x%x%x%x"'`
   - Look for 41414141 in output to find position
   - Calculate padding: 999 - address_bytes - prefix_length
   - Exploit: `perl -e 'system "./q4", pack("V", 0x804b018) . "%995d%5$n"'`

#### Question 5: Stack Protection
**Time: ~15 minutes**

1. **Stack frame layout** (5 min):
   - Compare `safe` vs `unsafe` executables
   - Use `info frame` in gdb for both
   - Note canary location in protected version

2. **Security benefits** (5 min):
   - Describe how canary prevents overflow
   - Explain frame layout differences

3. **Canary value** (5 min):
   - Find canary: `x/x $ebp-4` or similar
   - Explain why value is effective (usually contains null byte)

**Save Reminder:** Save after completing each question (2, 3, 4, 5)

---

### Phase 5: Final Review and Upload (107-110 minutes)

**Allocated Time: 3 minutes**

#### Final Checks:
1. **Verify all answers:**
   - Check that every `* Answer:` has content (even if partial)
   - Ensure no questions were accidentally skipped
   - Verify name and student number are filled

2. **Format check:**
   - Ensure all separators (`=====`, `---`) are preserved
   - Verify answer markers (`* Answer:`) are present
   - Check that question numbering is correct

3. **Save final version:**
   - Save the file one more time
   - Verify file exists: `ls -l exam.txt`
   - Check file size is reasonable (not 0 bytes)

4. **Upload to Lockbox:**
   - Navigate to lockbox URL from exam file
   - Log in with DCU credentials
   - Upload `exam.txt`
   - **VERIFY upload was successful** (check for confirmation)
   - Note: Upload should complete with at least 1-2 minutes remaining

**Critical:** Do not wait until the last second to upload!

---

## Time Management Summary

| Phase | Time | Activity |
|-------|------|----------|
| Setup | 0-2 min | Fill name, student number, save |
| Scan | 2-7 min | Read all questions |
| Section A | 7-47 min | Stack analysis (40 marks) |
| Section B | 47-107 min | Exploits (60 marks) |
| Review/Upload | 107-110 min | Final check and submission |

**Total: 110 minutes** (10 minutes buffer for the 100-minute exam)

---

## Save Strategy

### Mandatory Save Points:
1. ✅ After filling name/student number
2. ✅ After completing each sub-question (1a, 1b, etc.)
3. ✅ After completing each main question (1, 2, 3, etc.)
4. ✅ Every 10 minutes regardless of progress
5. ✅ Before running any exploit that might crash the system
6. ✅ Before uploading

### Save Frequency:
- **Minimum:** Every 10 minutes
- **Recommended:** Every 5 minutes or after each answer
- **Critical:** Before any risky operation

### Backup Strategy:
If possible, periodically copy the file:
```bash
cp exam.txt exam_backup.txt
```

---

## Answer Format Guidelines

### Standard Answer Format:
```
* Answer: [Your answer here]
```

### Multi-line Answers:
```
* Answer (max 3 lines):
Line 1 of answer
Line 2 of answer  
Line 3 of answer
```

### Answers with Work:
```
* Answer: saved eip = 0x8048390

(gdb) break hanoi
(gdb) run 8
(gdb) info frame
[gdb output here]
```

### Placeholder for Incomplete Answers:
```
* Answer: [TODO - return to this]
```

**Important:** Even partial answers are better than blank answers!

---

## Common Pitfalls to Avoid

1. **Forgetting to save** - Set a timer or save habitually
2. **Spending too long on one question** - Move on if stuck
3. **Not reading question carefully** - Re-read if unsure
4. **Skipping questions** - Leave placeholders, return later
5. **Uploading wrong file** - Verify you're uploading `exam.txt`
6. **Waiting until last minute to upload** - Upload with 5+ minutes remaining
7. **Not testing lockbox before exam** - Do this in advance!
8. **Forgetting name/student number** - Do this first!
9. **Not showing work** - Partial credit for demonstrated understanding
10. **Panicking** - Take deep breath, work systematically

---

## Emergency Procedures

### If System Crashes:
1. Don't panic
2. Restart if needed
3. Check if `exam.txt` still exists
4. If lost, work quickly to recreate answers
5. Inform invigilator if needed

### If Stuck on Question:
1. Leave placeholder: `* Answer: [TODO]`
2. Move to next question
3. Return with fresh perspective
4. Even partial answer is better than blank

### If Running Out of Time:
1. Prioritize questions with most marks
2. Write brief answers for all questions
3. Show work/commands even if incomplete
4. Ensure upload happens before time ends

---

## Success Checklist

Before leaving exam:
- [ ] Name and student number filled
- [ ] All questions attempted (even if partial)
- [ ] File saved multiple times
- [ ] File uploaded to lockbox successfully
- [ ] Upload confirmation received
- [ ] At least 1-2 minutes remaining on clock

Good luck!

