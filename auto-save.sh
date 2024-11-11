#!/bin/bash

# Watch for changes in the current directory (recursively)
while inotifywait -r -e modify,create,delete .; do
  # Add all changes
    git add -A
      
        # Commit with a default message including timestamp
          git commit -m "Auto-commit on $(date '+%Y-%m-%d %H:%M:%S')"
            
              # Optionally, push changes (uncomment the following line to enable automatic pushing)
                # git push
                done
                