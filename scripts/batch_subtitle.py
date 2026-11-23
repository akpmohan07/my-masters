#!/usr/bin/env python3
"""
Batch subtitle generator for video files in a folder.
Finds video files and generates SRT subtitles in the same directory.
"""

import os
import sys
import subprocess
import argparse
from pathlib import Path

# Supported video extensions
VIDEO_EXTENSIONS = {'.mp4', '.avi', '.mkv', '.mov', '.wmv', '.flv', '.webm', '.m4v', '.3gp', '.mpg', '.mpeg'}

def find_video_files(directory):
    """Find all video files in the given directory."""
    video_files = []
    directory = Path(directory)
    
    if not directory.exists():
        print(f"Error: Directory '{directory}' does not exist.")
        return []
    
    for file_path in directory.iterdir():
        if file_path.is_file() and file_path.suffix.lower() in VIDEO_EXTENSIONS:
            video_files.append(file_path)
    
    return sorted(video_files)

def generate_subtitles(video_path, model="base", language="auto", task="transcribe", verbose=False):
    """Generate subtitles for a single video file."""
    video_path = Path(video_path)
    srt_path = video_path.with_suffix('.srt')
    
    # Check if SRT already exists
    if srt_path.exists():
        print(f"SRT file already exists: {srt_path}")
        return srt_path
    
    print(f"Processing: {video_path.name}")
    
    # Build command
    cmd = [
        sys.executable, "-m", "auto_subtitle.cli",
        str(video_path),
        "--model", model,
        "--task", task,
        "--srt_only", "true",
        "--output_dir", str(video_path.parent)
    ]
    
    if language != "auto":
        cmd.extend(["--language", language])
    
    if verbose:
        cmd.extend(["--verbose", "true"])
    
    try:
        # Run the command
        result = subprocess.run(cmd, capture_output=True, text=True, check=True)
        
        if verbose:
            print(f"Output: {result.stdout}")
        
        # Verify SRT file was created
        if srt_path.exists():
            print(f"✅ Generated: {srt_path.name}")
            return srt_path
        else:
            print(f"❌ SRT file not found: {srt_path}")
            return None
            
    except subprocess.CalledProcessError as e:
        print(f"❌ Error processing {video_path.name}:")
        print(f"   Command: {' '.join(cmd)}")
        print(f"   Error: {e.stderr}")
        return None
    except Exception as e:
        print(f"❌ Unexpected error processing {video_path.name}: {e}")
        return None

def main():
    parser = argparse.ArgumentParser(
        description="Batch generate subtitles for video files in a directory",
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
Examples:
  python batch_subtitle.py /path/to/videos
  python batch_subtitle.py /path/to/videos --model medium
  python batch_subtitle.py /path/to/videos --language es --task translate
  python batch_subtitle.py /path/to/videos --verbose
        """
    )
    
    parser.add_argument("directory", help="Directory containing video files")
    parser.add_argument("--model", default="base", 
                       choices=["tiny", "tiny.en", "base", "base.en", "small", "small.en", "medium", "medium.en", "large", "turbo"],
                       help="Whisper model to use (default: base)")
    parser.add_argument("--language", default="auto", 
                       help="Language of the video (default: auto-detect)")
    parser.add_argument("--task", default="transcribe", choices=["transcribe", "translate"],
                       help="Task: transcribe or translate (default: transcribe)")
    parser.add_argument("--verbose", action="store_true",
                       help="Show verbose output")
    parser.add_argument("--skip-existing", action="store_true",
                       help="Skip videos that already have SRT files")
    
    args = parser.parse_args()
    
    # Find video files
    print(f"Scanning directory: {args.directory}")
    video_files = find_video_files(args.directory)
    
    if not video_files:
        print("No video files found.")
        return
    
    print(f"Found {len(video_files)} video file(s):")
    for video in video_files:
        print(f"  - {video.name}")
    
    print(f"\nUsing model: {args.model}")
    print(f"Task: {args.task}")
    if args.language != "auto":
        print(f"Language: {args.language}")
    
    # Process each video
    successful = 0
    failed = 0
    skipped = 0
    
    for video_path in video_files:
        srt_path = video_path.with_suffix('.srt')
        
        # Skip if SRT exists and --skip-existing is set
        if args.skip_existing and srt_path.exists():
            print(f"⏭️  Skipping {video_path.name} (SRT exists)")
            skipped += 1
            continue
        
        print(f"\n{'='*50}")
        result = generate_subtitles(
            video_path, 
            model=args.model, 
            language=args.language, 
            task=args.task,
            verbose=args.verbose
        )
        
        if result:
            successful += 1
        else:
            failed += 1
    
    # Summary
    print(f"\n{'='*50}")
    print(f"Summary:")
    print(f"  ✅ Successful: {successful}")
    print(f"  ❌ Failed: {failed}")
    print(f"  ⏭️  Skipped: {skipped}")
    print(f"  📁 Total: {len(video_files)}")

if __name__ == "__main__":
    main()
