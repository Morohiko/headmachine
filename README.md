# headmachine

## glassespart
### Required 
    min sdk version 27 (Android 8.1.0, Oreo)
    target sdk version 28 (Android 9, Pie)

### Android Feature/Issue
    For create Socket, need create Assync Tast (something like thread).
    For send info to server (machinepart) need create socket and thread for it.
    For working with OpenCV need create thread.

### Threads/modules
 - thread for tcp/udp socket for receive info from camera
 - thread for convert and record image (OpenCV processing)
 - thread for get Gyroscope data
 - thread for send gyroscope data to server (machinepart)
 - thread for activity

### Activity

#### Debug Activity
    Need for debugging all parts of project separately

#### Config Activity
    Need for chaning configuration of project, like ip, port of local and target
