# Implementing aspects with Spring AOP

> This project is based on chapter **6.2. Implementing aspects with Spring AOP** from book **Spring Starts here (2021)** by Laurentiu Spilca.

## Create Maven project with Intellij Idea

File > New project > Java

## Add Spring Context dependency

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>6.1.10</version>
</dependency>
```

## Create service

```java
@Service
public class MessageService {
    // create logger
    private Logger logger = Logger.getLogger(MessageService.class.getName());

    // log message
    public void processMessage(final String message) {
        logger.info("Processing message: " + message);
    }
}
```

## Create configuration class

```java
@Configuration
@ComponentScan(basePackages = "org.example")
public class ApplicationConfiguration {
}
```

## Add aspect to log messages before and after processMessage() method call

### Add Spring Aspects dependency

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-aspects</artifactId>
    <version>6.1.10</version>
</dependency>
```

### Enable Spring Aspects in configuration class

```diff
@Configuration
+ @EnableAspectJAutoProxy
@ComponentScan(basePackages = "org.example")
public class ApplicationConfiguration {
}
```

### Create aspect

- use `@Aspect` annotation to mark bean as aspect

```java
@Aspect
@Component
public class LoggingAspect {
}
```

### Add advice to log message before and after processMessage() method call

- explanation of the pointcut expression in `@Around` annotation:

  - `*` - this wildcard symbol indicates any return type of the method. It means the advice applies regardless of what the method returns.  

  - `org.example.MessageService` - This is the fully qualified name of the class containing the method to be advised. It specifies that the target method is within the MessageService class in the org.example package.  

  - `processMessage` - this is the name of the method to be advised. The advice will be applied to methods with this name.  

  - `(..)` - these parentheses with two dots inside signify any number and type of arguments to the method. It means the advice applies to processMessage methods regardless of what parameters they take.

```java
@Aspect
@Component
public class LoggingAspect {
    private Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    @Around("execution(* org.example.MessageService.processMessage(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("Before processing message");
        Object result = joinPoint.proceed();
        logger.info("After processing message");
        return result;
    }
}
```

## Add aspect to log arguments of the methods

### Create custom annotation

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ArgumentsLog {
}
```

### Create aspect

```java
@Aspect
@Component
public class ArgumentsAspect {
    private Logger logger = Logger.getLogger(ArgumentsAspect.class.getName());

    @Before("@annotation(ArgumentsLog)")
    public void logArguments(JoinPoint joinPoint) {
        final String methodName = joinPoint.getSignature().getName();
        final Object [] arguments = joinPoint.getArgs();
        logger.info("Method " + methodName + " with parameters " + Arrays.asList(arguments) + " will execute");
    }
}
```

### Mark method with custom annotation

```diff
@Service
public class MessageService {
    private Logger logger = Logger.getLogger(MessageService.class.getName());

+   @ArgumentsLog
    public void processMessage(final String message) {
        logger.info("Processing message: " + message);
    }
}
```

## Create Spring context

```java
ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
```

## Get bean from Spring context

```java
MessageService messageService = context.getBean(MessageService.class);
```

## Process message

```java
messageService.processMessage("Hello World!");
```

## Add tests

### Add dependency for JUnit

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-engine</artifactId>
    <version>5.11.0-M2</version>
    <scope>test</scope>
</dependency>
```

### Create test to check that application context is created

```java
public class ApplicationTests {

    @Test
    @DisplayName("Checks that Application Context is created")
    public void checkApplicationContextCreated() {
        ApplicationContext context = new AnnotationConfigApplicationContext();

        assertNotNull(context);
    }
}
```
