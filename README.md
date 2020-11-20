

# Hancord
A Discord binding for various Minecraft server plugin API's.

# Features 
#### 1.16+ 	
```+Role Colours```
#### 1.12+ 	
```+Advancements```
#### 1.10+
```+Dynmap intergration```
#### 1.8+ 	
```
+Death messages 
+Discord <-> minecraft chat linking 
+Luckperms intergration (Only Discord->Minecraft)
+A full discord command handler
```


# Setting up Maven (Developers)
For those wishing to hook into this plugin. Or to develop code based on this plugin
```
<repository>
	<id>jitpack.io</id>
	<url>https://jitpack.io</url>
</repository>
```
Replace "VERSION" with the number given by the Jitpack badge</br>
[![](https://jitpack.io/v/Hanro50/Discraft.svg)](https://jitpack.io/#Hanro50/Discraft)
#### API:
The core API, all implementations should intergrate this
```
<dependency>
	<groupId>com.github.Hanro50.Discraft</groupId>
	<artifactId>API</artifactId>
	<version>VERSION</version>
	<scope>provided</scope>
</dependency>
```
#### Server_core:
All server implementations should implement this, regardless of platform
```
<dependency>
	<groupId>com.github.Hanro50.Discraft</groupId>
	<artifactId>Server_core</artifactId>
	<version>VERSION</version>
	<scope>provided</scope>
</dependency>
```
#### Client_Spigot:
All spigot clients should implement this
```
<dependency>
	<groupId>com.github.Hanro50.Discraft</groupId>
	<artifactId>Client_Spigot</artifactId>
	<version>VERSION</version>
	<scope>provided</scope>
</dependency>
```
(Do not implement "Server_Spigot")
