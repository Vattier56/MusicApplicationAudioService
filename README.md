# Music Application Audio Service
Audio Service allows to read and change audio file meta informations.

## Table of contents
* [General info](#general-info)
* [Requirements](#requirements)
* [Setup](#setup)

## General info
By default, the service runs at ``` http://localhost:8200 ```

Supported audio file formats:
- Mp3
- Mp4
- Wma
- Aif 
- Wav
- Flac
- Dsf
- Ogg Vorbis

By default service returns object with:

``` song title, band name, album, genre, duration, source ``` 




## Requirements:
- Java version 1.8
- Apache Maven version 3.6.3


## Setup

To run this project, install it locally using maven:

```
$ mvn package
$ cd target
$ java -jar <projectName>
```

## Author
* Piotr Piasecki - [Vattier56](https://github.com/Vattier56)
