#!/bin/bash

# Start a new tmux session
tmux new-session -d -s startserverong

# In the first pane, run the server command
tmux send-keys -t startserverong "cd server && sudo java -jar server.jar" C-m

# Split and run the bungee command in the new pane
tmux split-window -h -t startserverong
tmux send-keys -t startserverong "cd bungee && sudo java -jar bungee.jar" C-m

# Split again, run the online command in the new pane
tmux split-window -v -t startserverong
tmux send-keys -t startserverong "bash online" C-m

# Wait for 3 minutes
sleep 180

# In a new split pane, run the auto-save command in the background
tmux split-window -v -t startserverong
tmux send-keys -t startserverong "./auto-save &" C-m

# Attach to the tmux session to view all panes
tmux attach -t startserverong
