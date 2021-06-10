# Mobilitybox Kotlin Client
A Kotlin client implementation for the [mobilitybox](https://developer.themobilitybox.com) API.

## Installation

### Gradle
```kotlin
implementation("io.github.christian-draeger:mobilitybox-kt:0.1.0")
```

### Maven
```xml
<dependency>
  <groupId>io.github.christian-draeger</groupId>
  <artifactId>mobilitybox-kt</artifactId>
  <version>0.1.0</version>
</dependency>
```

## Usage
### Search for Stations
```kotlin
val response = mobilitybox(apiKey = "your-api-key") {
    stations {
        searchByName("gransee")
        // or
        searchById("...")
        // or
        searchByPosition(GeoPosition(.0, .0))
    }
}
```

### Get Departures
```kotlin
val response = mobilitybox(apiKey = "your-api-key") {
    departures {
        get("vesputi-station-OW_67U4PIKCwCBxtnNWwZ2jnsNS2WZA1eNY9MyjyvKs")
    }
}
```
