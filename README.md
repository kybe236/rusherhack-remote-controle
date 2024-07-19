# Setup
## Server
- **Open Windows->Remote Console**
- **enter ".server 1"**
- **enter ".port {any port you want from 1024–65535}"**
- **ebter ".start" to start the server**
## Client/s
- **Open Windows->Remote Console**
- **enter ".port {the same port as the server}"**
- **enter ".ip {the ip from the server(u can use an vpn if you dont have a public one)}"**
- **enter ".start"**
# Commands
## Window
- **.info**
  gets info about each client ("IP: {the ip of the targe} TIME: {the time of the target}
- **.msg {msg}**
  sends an message as the client
- **.cmd {cmd}**
  sends an command as the client
- **All from the above**
## In Game
### **.mirror {msg/cmd} {mirror} {cancel msg/cmd}**
- **msg/cmd:** msg for message cmd for command settings
- **mirror:** should the message be mirrored and send from the remote client
- **cancel:** should the server not send the msg
### **.remotesend {msg/cmd} {msg/cmd}**
- **msg/cmd:** msg for message cmd for command settings
- **msg/cmd:** the content of the msg/cmd that is going to be sent from all remote client
