# Overview
This project provides a basic driver interface for the Home Theatre Direct MCA66,
assuming that you have the MCA66 connected to your wifi using their optional
ethernet gateway. This driver may also work with their WiFi gateway, but I have
not tested it.

# Installation
1. Copy the interface and zone driver code into two separate files on your hubitat
2. Create a new virtual device, and point it at the 'HTD MCA66 Amplifier Interface'.
  - Do NOT instantiate the 'HTD MCA66 Amplifier zone.' These will be
  automatically created by the Interface.
3. Edit the preferences pane to point at the IP address of your HTD gateway, and
save the update.
4. You should be able to use the zones after that!

# Open issues / future features
1. State management at start-up is not great, but the driver should self-
correct.
2. The volume control is slow. It relies on the up/down commands and takes a
while to complete.

# Bugs / other issues
Feel free to publish bugs, improvements, or other issues here!
