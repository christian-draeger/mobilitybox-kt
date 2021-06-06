# Mobilitybox Kotlin Client

## Installation

### Gradle
```kotlin
implementation("io.github.christian-draeger:mobilitybox-kt:0.1.0-alpha")
```

### Maven
```xml
<dependency>
  <groupId>io.github.christian-draeger</groupId>
  <artifactId>mobilitybox-kt</artifactId>
  <version>0.1.0-alpha</version>
</dependency>
```

## Usage

```kotlin
val response = mobilitybox {
    apiKey = "your-api-key"
    stations {
        searchByName("gransee")
    }
}
```
